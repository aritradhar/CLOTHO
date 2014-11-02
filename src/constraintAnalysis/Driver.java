//*************************************************************************************
//*********************************************************************************** *
//author Aritra Dhar																* *
//MT12004																			* *
//M.TECH CSE																		* * 
//INFORMATION SECURITY																* *
//IIIT-Delhi																		* *	 
//---------------------------------------------------------------------------------	* *																				* *
/////////////////////////////////////////////////									* *
//The program will do the following::::         //									* *
/////////////////////////////////////////////////									* *
//version 1.0																		* *
//*********************************************************************************** * 
//*************************************************************************************

package constraintAnalysis;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import constraintAnalysis.stringRepair.StringRepairConstraintDynamic;
import soot.G;
import soot.Pack;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.Transform;
import soot.jimple.infoflow.taint.SourceSinkResolver;
import soot.options.Options;
import util.ENV;
import util.JarUtils;

public class Driver 
{
	public static void main(String[] args) throws IOException 
	{
		FileWriter fw = new FileWriter("log.txt",true);
		fw.append("Args : \n");
		fw.append(args[0] + "  "+args[1] + "  " + args[2] + "\n");
		
		JarUtils.populateFilesList(new File(args[0]));
		List<String> Files = JarUtils.filesListInDir;
		
		ArrayList<String> classNameList = new ArrayList<>();
		
		for(String files : Files)
		{
			if(!files.endsWith(".class"))
				continue;
			
			String temp = files.replace("C:\\Users\\Aritra\\workspace\\git\\Repair_Spec\\bin\\", "");
			//String temp = files.replace("C:\\Users\\Aritra\\workspace\\git\\Repair_Spec\\test\\", "");
			classNameList.add(temp.replace("\\", ".").replace(".class", ""));
			//System.out.println(className[i++]);
		}
		
		Options.v().set_soot_classpath(ENV.SOOT_CLASS_PATH);				
		Options.v().set_prepend_classpath(true);
		 
		//String[] className = {"StringTest"};
		//String[] className = {"asmbug.Method"};
		//String[] className = {"apacheMathBug.ComplexFormat"};
		String[] className = {"ApacheStrutsBug.CoolUriServletDispatcher"};
		//String[] className = classNameList.toArray(new String[classNameList.size()]);	
		//String []className = {"net.nlanr.jperf.core.IPerfProperties"};
		
		long start = System.currentTimeMillis();
		
        Pack jtp = PackManager.v().getPack("jtp");
        
        jtp.add(new Transform("jtp.constraintcheck", new ConstraintCheck()));
        
        ENV.WriteOption(ENV.CONSTRAINT_ANALYSIS_PHASE_WRITE_OPTION);   
        
        //Options.v().setPhaseOption("jb", "use-original-names:true");
        
        soot.Main.main(className);	           

        long constraint_check_end = System.currentTimeMillis();
        
        fw.append("Constraint analysis time : " + (constraint_check_end - start) + " ms\n");
        /*
        //DEBUG
        //constraint map check
        for(String Key : ConstraintStorageMap.constraintStorageMap.keySet())
        {
        	System.out.println(Key);
            HashMap<Value, ConstraintStorageDataType> cdt = ConstraintStorageMap.constraintStorageMap.get(Key);
        	
            for(Value val : cdt.keySet())
            {
            	System.out.println("String object : " + val);
            	ConstraintStorageDataType CDT = cdt.get(val);
            	System.out.println("Min length : " + CDT.minLength);
            	System.out.println("Max length : " + CDT.maxLength);
            	System.out.println("Prefix : " + CDT.prefix);
            	System.out.println("Contains : " + CDT.contains);
            }	
        }
        */
        
        System.out.println("==================================================================================");
        System.out.println("                        End of constraint analysis                                ");
        System.out.println("==================================================================================");
        
        G.reset();
        
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*
         * Taint analysis
         */
        
        SourceSinkResolver ssr = null;
        if(ENV.TAINT_ANALYSIS_ENABLE)
        {
        	long taint_start = System.currentTimeMillis();
        
        	ssr = new SourceSinkResolver(new String[]{args[1], args[2]}, false);
        	ssr.setAccessPathLength(ENV.INFOFLOW_ACCESS_PATH_LENGTH);
        	ssr.runAnalysis();      
        
        	long taint_end = System.currentTimeMillis();
        
        	fw.append("taint analysis time :" + (taint_end - taint_start) + " ms\n");
        
        	G.reset();
        }
        
        System.out.println("Checking CallGraph for already Handled exception");
        if(ENV.CALL_CHAIN_LOOK_UP_FOR_EXCEPTION_HANDLER)
        {
        	
        }
        
        
        
        long instrument_start = System.currentTimeMillis();
        
        jtp = PackManager.v().getPack("jtp");
        
        if(ENV.TAINT_ANALYSIS_ENABLE)
        {
        	jtp.add(new Transform("jtp.instrument", new StringRepairConstraintDynamic(ssr)));
        }
        
        else
        {
        	jtp.add(new Transform("jtp.instrument", new StringRepairConstraintDynamic()));
        }
        
        Options.v().setPhaseOption("jb", "use-original-names:true");
        
        ENV.classReseolver();
        
        
        Options.v().set_soot_classpath(ENV.SOOT_CLASS_PATH);	
		Options.v().set_prepend_classpath(true);
	
        
		ENV.WriteOption(ENV.INSTRUMRNTATION_PASE_WRITE_OPTION);
		
        Scene.v().addBasicClass("java.util.Iterator",SootClass.SIGNATURES);
   
        soot.Main.main(className);	
        
        long instrument_end = System.currentTimeMillis();
        
        fw.append("Instrumentation time + class flashing in FileSystem : " + (instrument_end - instrument_start) + " ms\n");
        fw.append("Total repair count : " + ENV.REPAIR_COUNT + "\n");
        fw.close();
	}
}
