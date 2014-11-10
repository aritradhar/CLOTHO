
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

import soot.PatchingChain;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Stmt;

class OptimizeDataType
{
	public PatchingChain<Unit> pc;
	public Unit lastUnit;
	
	public OptimizeDataType(PatchingChain<Unit>pc, Unit lasUnit)
	{
		this.pc = pc;
		this.lastUnit = lasUnit;
	}
}

public class OptimizationExlude 
{
	public static HashMap<Value, OptimizeDataType> optMap = new HashMap<>();
	
	
	public static void setMap(PatchingChain<Unit> pc, Stmt stmt, Value value)
	{
		if(!optMap.containsKey(value))
		{
			optMap.put(value, new OptimizeDataType(pc, stmt));
		}
		else
		{
			OptimizeDataType odt = optMap.get(value);
			odt.lastUnit = stmt;
			optMap.put(value, odt);
		}
	}
	
	public static boolean doOptimize(PatchingChain<Unit> pc, Stmt stmt, Value value)
	{
		if(!optMap.containsKey(value))
			return false;
		
		OptimizeDataType odt = optMap.get(value);
		Stmt lastStmt = (Stmt) odt.lastUnit;
		
		Iterator<Unit> it = pc.iterator();
		
		boolean flag = false;
		
		while(it.hasNext())
		{
			Stmt st = (Stmt) it.next();
			if(st.equals(lastStmt))
				flag = true;
			
			if(st.equals(stmt))
				break;
			
			if(flag)
			{
				if(st instanceof AssignStmt)
				{
					Value lhs = ((AssignStmt) st).getLeftOp();
					if(lhs.equals(value))
						return false;
				}
			}
		}
		return true;
		
	}
	
	
}
