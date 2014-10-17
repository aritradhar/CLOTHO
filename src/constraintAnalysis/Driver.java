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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import constraintAnalysis.stringRepair.StringRepairConstraint;
import soot.G;
import soot.Pack;
import soot.PackManager;
import soot.Transform;
import soot.Value;
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
        }*/
        
        System.out.println("==================================================================================");
        System.out.println("                        End of constraint analysis                                ");
        System.out.println("==================================================================================");
        
        G.reset();
        
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        jtp = PackManager.v().getPack("jtp");
        
        jtp.add(new Transform("jtp.constraintcheck", new StringRepairConstraint()));
        
        
        //Options.v().setPhaseOption("jb", "use-original-names:true");
        
        soot.Main.main(className);	
        
	}
}
