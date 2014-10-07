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
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.Trap;
import soot.Unit;
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
        
        //System.out.println(TrapFindType.trapFindType.entrySet());
       // System.out.println(TrapFindType.unitTrapMap.entrySet());
	}

	@SuppressWarnings("rawtypes" )
	@Override
	protected void internalTransform(Body jbody, String phaseName, Map options) 
	{
		SootMethod sMethod = jbody.getMethod();		
		
		String subSignature = sMethod.getSignature();
		//System.out.println("<< Current method : " + subSignature + " >>");
		
		Chain<Trap> traps = jbody.getTraps();
		Iterator<Trap> iTraps = traps.iterator();
		
		while(iTraps.hasNext())
		{
			Trap trap = iTraps.next();
			TrapFindType.insertTrap(subSignature, trap);
		}
		
		PatchingChain<Unit> pc = jbody.getUnits();
		Iterator<Unit> it = pc.iterator();
		
		while(it.hasNext())
		{
			Unit unit = it.next();
			/*
			Trap trap = TrapFindType.unitExistsInTrap(subSignature, unit, pc);
			
			if(trap!=null)
				System.out.println(unit + "  "  + trap);
			*/
			SootClass sc = TrapFindType.getExeptionClassFromUnit(subSignature, unit, pc);
			if(sc!=null)
			{
				System.out.println(subSignature+ "  " + unit + "  "  + sc);
			}
			
			TrapFindType.setUnitTrapInfo(subSignature, unit, pc);
		}
	} 

}
