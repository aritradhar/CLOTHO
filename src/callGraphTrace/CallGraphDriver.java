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

package callGraphTrace;

import java.util.Arrays;

import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.Transform;
import soot.options.Options;
import util.ENV;


public class CallGraphDriver 
{
	
	public static long memory_consumption;
	
	public static void main(String[] className) 
	{

		//String []className = {"StringTest"};
		//String[] className = {"BugTestPack.ApacheStrutsBug.CoolUriServletDispatcher", "StringTest"};
		
		Options.v().set_soot_classpath(ENV.SOOT_CLASS_PATH);				
		Options.v().set_prepend_classpath(true);
		Options.v().setPhaseOption("cg.cha", "on");
		Options.v().set_exclude(Arrays.asList(new String[]{"java", "sun", "java.lang"}));
		Options.v().set_no_bodies_for_excluded(true);
		
		long mem_start = Runtime.getRuntime().freeMemory();
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.myTransform", new CallGraphTrapTracer(className)));
		
		long mem_end = CallGraphTrapTracer.free_mem;
		
		memory_consumption = (mem_start - mem_end)/(1024 * 1024);
		
		Options.v().setPhaseOption("jb", "use-original-names:true");
	     
        String st = "c";
        
        if(st.equalsIgnoreCase("j"))
        	Options.v().set_output_format(Options.output_format_jimple);
        
        if(st.equalsIgnoreCase("c"))
        	Options.v().set_output_format(Options.output_format_class); 
        
        Options.v().set_whole_program(true);
		
		Options.v().set_app(true);
		
		ENV.classReseolver();
		ENV.classReseolverBody();
		
		soot.Main.main(className);
	}
}
