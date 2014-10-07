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
import soot.Transform;
import soot.options.Options;


public class CallGraphDriver 
{
	public static void main(String[] args) 
	{

		String []className = {"StringTest"};

		PackManager.v().getPack("wjtp").add(new Transform("wjtp.myTransform", new CallGraphTrapTracer()));
		
		Options.v().setPhaseOption("jb", "use-original-names:true");
	     
        String st = "c";
        
        if(st.equalsIgnoreCase("j"))
        	Options.v().set_output_format(Options.output_format_jimple);
        
        if(st.equalsIgnoreCase("c"))
        	Options.v().set_output_format(Options.output_format_class); 
        
        Options.v().set_whole_program(true);
		Options.v().set_exclude(Arrays.asList(new String[]{"java", "sun", "java.lang"}));
		Options.v().set_app(true);

		soot.Main.main(className);
	}
}
