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
import soot.options.Options;
import util.ENV;
import util.JarUtils;

public class Driver 
{
	public static void main(String[] args) throws IOException 
	{
		JarUtils.populateFilesList(new File(args[0]));
		List<String> Files = JarUtils.filesListInDir;
		
		ArrayList<String> classNameList = new ArrayList<>();
		
		for(String files : Files)
		{
			if(!files.endsWith(".class"))
				continue;
			
			String temp = files.replace("C:\\Users\\Aritra\\workspace\\git\\Repair_Spec\\bin\\", "");
			
			classNameList.add(temp.replace("\\", ".").replace(".class", ""));
			//System.out.println(className[i++]);
		}
		
		Options.v().set_soot_classpath(ENV.SOOT_CLASS_PATH);				
		Options.v().set_prepend_classpath(true);
		 
		
		String[] className = {"StringTest"};
		//String[] className = classNameList.toArray(new String[classNameList.size()]);	
		//String []className = {"net.nlanr.jperf.core.IPerfProperties"};
		
        Pack jtp = PackManager.v().getPack("jtp");
        
        jtp.add(new Transform("jtp.constraintcheck", new ConstraintCheck()));
        
        String st = "j";
        
        if(st.equalsIgnoreCase("j"))
        	Options.v().set_output_format(Options.output_format_jimple);
        
        if(st.equalsIgnoreCase("c"))
        	Options.v().set_output_format(Options.output_format_class);     
        
        Options.v().setPhaseOption("jb", "use-original-names:true");
        
        soot.Main.main(className);	           

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
        
        jtp = PackManager.v().getPack("jtp");
        
        jtp.add(new Transform("jtp.instrument", new StringRepairConstraintDynamic()));
        
        
        Options.v().setPhaseOption("jb", "use-original-names:true");
        
        Scene.v().addBasicClass("constraintAnalysis.ConstraintStorageMapDynamic",SootClass.SIGNATURES);
        Scene.v().addBasicClass("constraintAnalysis.GenerateStringDynamic",SootClass.SIGNATURES);
        Scene.v().addBasicClass("stringrepair.IndexRepair", SootClass.SIGNATURES);       
        
        Options.v().set_soot_classpath(ENV.SOOT_CLASS_PATH);	
		Options.v().set_prepend_classpath(true);
		
        String st1 = "c";
        
        if(st1.equalsIgnoreCase("j"))
        	Options.v().set_output_format(Options.output_format_jimple);
        
        if(st1.equalsIgnoreCase("c"))
        	Options.v().set_output_format(Options.output_format_class);     
        
        Scene.v().addBasicClass("java.util.Iterator",SootClass.SIGNATURES);
        
        soot.Main.main(className);	
        
	}
}
