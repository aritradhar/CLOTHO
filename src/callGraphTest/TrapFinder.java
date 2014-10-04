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

package callGraphTest;
import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Pack;
import soot.PackManager;
import soot.SootMethod;
import soot.Transform;
import soot.Trap;
import soot.options.Options;
import soot.util.Chain;


public class TrapFinder extends BodyTransformer 
{
	public static void main(String[] args) 
	{
		String []className = {"StringTest"};
		  
        Pack jtp = PackManager.v().getPack("jtp");

        jtp.add(new Transform("jtp.instrumenter", new TrapFinder()));
        Options.v().setPhaseOption("jb", "use-original-names:true");
     
        String st = "c";
        
        if(st.equalsIgnoreCase("j"))
        	Options.v().set_output_format(Options.output_format_jimple);
        
        if(st.equalsIgnoreCase("c"))
        	Options.v().set_output_format(Options.output_format_class);     
            
        
        soot.Main.main(className);
        
        System.out.println(TrapFindType.trapFindType.entrySet());
	}

	@Override
	protected void internalTransform(Body jbody, String phaseName, Map options) 
	{
		SootMethod sMethod = jbody.getMethod();		
		
		String subSignature = sMethod.getSubSignature();
		System.out.println("<< Current method : " + subSignature + " >>");
		
		Chain<Trap> traps = jbody.getTraps();
		Iterator<Trap> iTraps = traps.iterator();
		
		while(iTraps.hasNext())
		{
			Trap trap = iTraps.next();
			TrapFindType.insertTrap(subSignature, trap);
		}
	} 

}
