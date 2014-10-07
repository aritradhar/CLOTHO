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

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.PatchingChain;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.Unit;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.util.Chain;


public class CallGraphTrapTracer extends SceneTransformer
{
	@SuppressWarnings({"unused", "rawtypes"})
	@Override
	protected void internalTransform(String phaseName, Map options) 
	{
		SootClass sClass = Scene.v().loadClassAndSupport("StringTest");
		sClass.setApplicationClass();
		
		CHATransformer.v().transform();
		SootMethod src = sClass.getMethodByName("main");
		ArrayList<SootMethod> entryPoints = new ArrayList<SootMethod>();
		SootMethod src1 = sClass.getMethodByName("main");
		entryPoints.add(src1);
		Scene.v().setEntryPoints(entryPoints);
		CallGraph cg = Scene.v().getCallGraph();
		
		try
		{
			FileWriter fw = new FileWriter("CGDump.txt");
			fw.append(cg.toString());
			fw.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		SootMethod testM = sClass.getMethodByName("bar");
		
		System.out.println(CallGraphDFS.callGraphDFS(cg, sClass.getMethodByName("foo"), true) + "\n----");
		
		/*
		 * DEBUG
		Iterator<MethodOrMethodContext> targets1 = new Sources(cg.edgesInto(testM));
		while (targets1.hasNext()) 
		{
			SootMethod tgt = (SootMethod)targets1.next();
			
			if(ExcludeMethod.excludeMethod(tgt))
				continue;
			
			System.out.println(testM + " may called from " + tgt);
		}
		*/
		
		
		/*
		 * Use the TrapFinder code with minor modification
		 * This will bale to plug-in to the current code.
		 * This will allow to go up in the call chain and 
		 * go to the call site and see if it was wrapped 
		 * in some try-catch block or not.
		 */
		
		List<SootMethod> sMethodList = sClass.getMethods();
		for(int i = 0; i< sMethodList.size(); i++)
		{
			SootMethod sMethod = sMethodList.get(i);
			
			String subSignature = sMethod.getSignature();
			//System.out.println("<< Current method : " + subSignature + " >>");
			
			Body jbody = sMethod.retrieveActiveBody();
			
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

				SootClass sc = TrapFindType.getExeptionClassFromUnit(subSignature, unit, pc);
				if(sc!=null)
				{
					System.out.println(subSignature+ "  " + unit + "  "  + sc);
				}
				
				TrapFindType.setUnitTrapInfo(subSignature, unit, pc);
			}
		
		}			
	}
}
