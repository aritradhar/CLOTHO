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
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.util.Chain;
import util.ENV;
import util.Utils;


public class CallGraphTrapTracer extends SceneTransformer
{
	public static long free_mem;

	SootClass[] inputSootClass;

	public CallGraphTrapTracer()
	{

	}

	public CallGraphTrapTracer(String[] inputClass)
	{	
		int i = 0;

		inputSootClass = new SootClass[inputClass.length];
		
		for(String str : inputClass)
		{
			this.inputSootClass[i++] = Scene.v().loadClass(str, SootClass.BODIES);
		}
	}
	@SuppressWarnings({"unused", "rawtypes"})
	@Override
	protected void internalTransform(String phaseName, Map options) 
	{
		//SootClass sClass = Scene.v().loadClassAndSupport("BugTestPack.ApacheStrutsBug.CoolUriServletDispatcher");
		for(SootClass sClass : inputSootClass)
		{
			sClass.setApplicationClass();

			CHATransformer.v().transform();
			//SootMethod src = sClass.getMethodByName("service");
			ArrayList<SootMethod> entryPoints = new ArrayList<SootMethod>();

			//SootMethod rc1 = sClass.getMethodByName("service");
			//entryPoints.add(src1);
			//Scene.v().setEntryPoints(entryPoints);
			CallGraph cg = Scene.v().getCallGraph();
			
			ENV.STAT_CALL_GRAPH_SIZE = cg.size();

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



			/*
			 * Use the TrapFinder code with minor modification
			 * This will bale to plug-in to the current code.
			 * This will allow to go up in the call chain and 
			 * go to the call site and see if it was wrapped 
			 * in some try-catch block or not.
			 */

			List<SootMethod> sMethodList = sClass.getMethods();

			//record all traps from the class
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
		/*
		System.out.println("===================");

		for(int i = 0; i< sMethodList.size(); i++)
		{
			SootMethod sMethod = sMethodList.get(i);
			if(sMethod.getName().startsWith("<"))
				continue;

			//do reverse lookup
			//ArrayList<SootMethod> reverseLookupList = CallGraphDFS.callGraphDFS(cg, sMethod, true);

			System.out.println(sMethod + " Handled in : " + CallGraphDFS.reverseLookupTrapFinder(cg, sMethod, true));


		}*/
		free_mem = Runtime.getRuntime().freeMemory();
	}
}
