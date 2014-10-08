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

package boundedAnalysis;

import java.util.Iterator;
import java.util.List;

import soot.Local;
import soot.PatchingChain;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Stmt;


public class ConstraintCheck 
{
	public Unit unit;
	public Local local;
	public PatchingChain<Unit> pc;
	
	public ConstraintCheck(Unit unit, soot.Local local, PatchingChain<Unit> pc)
	{
		this.unit = unit;
		this.local = local;
		this.pc = pc;
		
		this.getConstraint();
	}
	
	@SuppressWarnings("unchecked")
	public void getConstraint()
	{
		Iterator<Unit> it = pc.iterator();
		
		while(it.hasNext())
		{
			Unit u = it.next();
			Stmt stmt = (Stmt) u;
			
			List<ValueBox> useDefBox = stmt.getUseAndDefBoxes();
			
			for(ValueBox vb:useDefBox)
			{
				Value value = vb.getValue();
				if(local.equals((Local)value))
				{
					
				}
			}
			
		}
	}
}
