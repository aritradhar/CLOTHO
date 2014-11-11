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
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;
import util.ENV;
import util.JarUtils;

public class Driver 
{
	public static CallGraph cg;
	
	public static void main(String[] args) throws IOException 
	{
		FileWriter fw = new FileWriter("log.txt",true);
		//fw.append("Args : \n");
		//fw.append(args[0] + "  "+args[1] + "  " + args[2] + "\n");
		
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
		//String[] className = {"log4jRep"};
		//String[] className = {"BugTestPack.AdobeFlexBuf.OrderedProperties"};
		//String[] className = {"BugTestPack.ApacheHama.HamaBug"};
		//String[] className = {"StringExample"};
		//String[] className = {"BugTestPack.ApacheCommonFileUtils.ApacheBug"};
		//String[] className = {"BugTestPack.asmbug.Method"};
		//String[] className = {"BugTestPack.ApacheCommonLangMathBug.NumberUtils"};
		//String[] className = {"BugTestPack.apacheMathBug.ComplexFormat"};
		//String[] className = {"BugTestPack.ApacheStrutsBug.CoolUriServletDispatcher"};
		//String[] className = {"BugTestPack.eclipseAspectJWeaverBug.WeaverBug"};
		//String[] className = {"BugTestPack.ApacheCommonFileFtpBug.Ftpbug"};
		//String[] className = {"BugTestPack.ApacheXalan.XalanBug"};
		//String[] className = {"BugTestPack.ApacheSOAPbug.SoapBug"};
		//String[] className = {"BugTestPack.ApacheCompressBug.TarArchiveEntry"};
		//String[] className = {"BugTestPack.ApacheCliBug.CliBug"};
		//String[] className = {"BugTestPack.ApacheCliBug.CliBug2_x"};
		//String[] className = {"BugTestPack.ApacheWicket.WicketBug"};
		//String[] className = {"BugTestPack.ApacheTapeStry.TapeStryBug"};
		//String[] className = {"BugTestPack.ApacheJuddiBug.JuddiBug"};
		//String[] className = {"BugTestPack.EclipseAspectWeaverBcelBug.AspectJBcel"};
		//String[] className = {"BugTestPack.ApacheCommonLangMathBug.NumberUtils"};
		//String[] className = {"BugTestPack.ApacheSlingBug.SlingBug"};
		String[] className = {"BugTestPack.ApacheDerbyBug.DerbyBug"};
		//String[] className = classNameList.toArray(new String[classNameList.size()]);	
		//String []className = {"net.nlanr.jperf.core.IPerfProperties"};
		
		fw.append("Class file input :\n");
		for(int i = 0; i < className.length; i++ )
		{
			fw.append(className[i] + "\n");
		}
		
		Pack jtp = null;
		
		if(ENV.CONSTRAINT_ANALYSIS_ENABLE)
		{
			long start = System.currentTimeMillis();
			long constrait_check_free_mem_start = Runtime.getRuntime().freeMemory();
		
        	jtp = PackManager.v().getPack("jtp");
        
        	jtp.add(new Transform("jtp.constraintcheck", new ConstraintCheck()));
        
        	ENV.WriteOption(ENV.CONSTRAINT_ANALYSIS_PHASE_WRITE_OPTION);   
        
        	Options.v().setPhaseOption("jb", "use-original-names:true");
        
        	soot.Main.main(className);	           

        	long constraint_check_end = System.currentTimeMillis();
        
        	if(ENV.PROFILE_ANALYSIS_TIME)
        	{
        		fw.append("Constraint analysis time : " + (constraint_check_end - start) + " ms\n");
        	}
        
        	long constrait_check_free_mem_end = Runtime.getRuntime().freeMemory();
        
        	if(ENV.PROFILE_ANALYSIS_MEMORY)
        	{
        		fw.append("Constraint analysis memory consumption : " + ((constrait_check_free_mem_start - constrait_check_free_mem_end)/(1024*1024)) + " MB\n");
        	}
		}
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
        System.gc();
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*
         * Taint analysis
         */
        
        SourceSinkResolver ssr = null;
        if(ENV.TAINT_ANALYSIS_ENABLE)
        {
        	long taint_start = System.currentTimeMillis();
        	long taint_free_mem_start = Runtime.getRuntime().freeMemory();
        	
        	ssr = new SourceSinkResolver(new String[]{args[1], args[2]}, false);
        	ssr.setAccessPathLength(ENV.INFOFLOW_ACCESS_PATH_LENGTH);
        	ssr.runAnalysis();      
        
        	long taint_end = System.currentTimeMillis();
        	long taint_free_mem_end = Runtime.getRuntime().freeMemory();
        	
        	if(ENV.PROFILE_ANALYSIS_TIME)
        	{
        		fw.append("Taint analysis time :" + (taint_end - taint_start) + " ms\n");
        	}
        	if(ENV.PROFILE_ANALYSIS_MEMORY)
        	{
        		fw.append("Taint analysis memory consumption :" + ((taint_free_mem_start - taint_free_mem_end)/(1024*1024)) + " MB\n");
        	}
        	
        	G.reset();
        	System.gc();
        }
        
        System.out.println("----------Checking CallGraph for already Handled exception----------------");
        if(ENV.CALL_CHAIN_LOOK_UP_FOR_EXCEPTION_HANDLER)
        {
        	long call_graph_start = System.currentTimeMillis();
        	
        	Options.v().set_soot_classpath(ENV.SOOT_CLASS_PATH);
        	
        	try
        	{
        		callGraphTrace.CallGraphDriver.main(className);
        	}
        	catch(Exception ex)
        	{
        		ex.printStackTrace();
        		System.exit(1);
        	}
        	
        	long call_graph_end = System.currentTimeMillis();
        	
        	if(ENV.PROFILE_ANALYSIS_TIME)
        	{
        		fw.append("Call graph analysis time :" + (call_graph_end - call_graph_start) + " ms\n");
        	}
        	
        	if(ENV.PROFILE_ANALYSIS_MEMORY)
        	{
        		fw.append("Call graph analysis memory Consumption  :" + callGraphTrace.CallGraphDriver.memory_consumption + " MB\n");
        	}
        	
        	G.reset();
        	System.gc();
        }
   
        
        
        
        long instrument_start = System.currentTimeMillis();
        long instrument_free_mem_start = Runtime.getRuntime().freeMemory();
        
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
	
        Options.v().keep_line_number();
		
		ENV.WriteOption(ENV.INSTRUMRNTATION_PASE_WRITE_OPTION);
		
        Scene.v().addBasicClass("java.util.Iterator",SootClass.SIGNATURES);
   
        soot.Main.main(className);	
        
        long instrument_end = System.currentTimeMillis();
        long instrument_free_mem_end = Runtime.getRuntime().freeMemory();
        
        if(ENV.PROFILE_ANALYSIS_TIME)
        {
        	fw.append("Instrumentation time + class flashing in FileSystem : " + (instrument_end - instrument_start) + " ms\n");
        }
        
        if(ENV.PROFILE_ANALYSIS_MEMORY)
    	{
    		fw.append("Instrumentation memory Consumption  :" + ((instrument_free_mem_start - instrument_free_mem_end)/(1024*1024)) + " MB\n");
    	}
        
        fw.append("Total repair count : " + ENV.STAT_REPAIR_COUNT + "\n");
        fw.append("Total unit handled : " + ENV.STAT_UNIT_HANDLED + "\n");
        fw.append("Call graph size : " + ENV.STAT_CALL_GRAPH_SIZE + "\n");
        fw.append("Total unit count after repair : " + ENV.STAT_UNIT_POST_REPAIR + "\n");
        fw.append("Total instrumentation : " + (ENV.STAT_UNIT_POST_REPAIR - ENV.STAT_UNIT_HANDLED) + "\n");
        
        System.out.print("Total repair count : " + ENV.STAT_REPAIR_COUNT + "\n");
        System.out.print("Total unit handled : " + ENV.STAT_UNIT_HANDLED + "\n");
        System.out.print("Call graph size : " + ENV.STAT_CALL_GRAPH_SIZE + "\n");
        System.out.print("Total unit count after repair : " + ENV.STAT_UNIT_POST_REPAIR + "\n");
        
        fw.append("=====================================================================================\n");
        fw.close();
        
        
        
        System.out.println("Safe : "  + ENV.TOTAL_SAFE);
        System.out.println("Unsafe : "  + ENV.TOTAL_UNSAFE);
        
        
	}
}
