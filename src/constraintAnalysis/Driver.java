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

import soot.G;
import soot.Pack;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;


public class Driver 
{
	public static void main(String[] args) 
	{
		String []className = {"StringTest"};
		  
        Pack jtp = PackManager.v().getPack("jtp");
        
        jtp.add(new Transform("jtp.constraintcheck", new ConstraintCheck()));
        
        String st = "j";
        
        if(st.equalsIgnoreCase("j"))
        	Options.v().set_output_format(Options.output_format_jimple);
        
        if(st.equalsIgnoreCase("c"))
        	Options.v().set_output_format(Options.output_format_class);     
        
        //Options.v().setPhaseOption("jb", "use-original-names:true");
        
        soot.Main.main(className);	        
	}
}
