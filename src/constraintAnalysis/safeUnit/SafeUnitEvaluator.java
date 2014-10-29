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


package constraintAnalysis.safeUnit;

import java.util.HashMap;
import java.util.Iterator;

import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.infoflow.taint.SourceSinkResolver;


public class SafeUnitEvaluator 
{
	private PatchingChain<Unit> pc;
	private SootMethod sMethod;
	private SourceSinkResolver ssr;
	/*
	 * If a unit is safe to patching or not
	 * true -> safe
	 * false -> unsafe
	*/
	private HashMap<Unit, Boolean> unitSafeMap;
	
	public SafeUnitEvaluator(SourceSinkResolver ssr, PatchingChain<Unit>pc, SootMethod sMethod)
	{
		unitSafeMap = new HashMap<>();
		this.ssr = ssr;
		this.pc = pc;
		this.sMethod = sMethod;
		this.init();
	}
	
	
	private void init()
	{
		Iterator<Unit> it = this.pc.iterator();
		int counter = 0;
		while(it.hasNext())
		{
			Unit unit = it.next();
			this.unitSafeMap.put(unit, ssr.isSafe(unit, sMethod, counter++));
		}
	}
	
	public boolean isSafe(Unit unit)
	{
		return this.unitSafeMap.containsKey(unit) ? this.unitSafeMap.get(unit) : false;
	}
}
