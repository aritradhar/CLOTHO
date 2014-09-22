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
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.HashReversibleGraph;
import soot.toolkits.graph.ReversibleGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.graph.pdg.EnhancedUnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;


@SuppressWarnings("rawtypes")
public class ForwordAnalysis extends ForwardFlowAnalysis
{
	private UnitGraph g;
	private FlowInformation fl;
	/**
	 * @param g
	 * @param fl 
	 */
	@SuppressWarnings("unchecked")
	public ForwordAnalysis(UnitGraph g, FlowInformation fl) 
	{
		super(g);
		this.g = g;
		this.fl = fl;
		
		doAnalysis();
	}@SuppressWarnings("unchecked")
	public ForwordAnalysis(UnitGraph g) 
	{
		super(g);
		this.g = g;
		
		doAnalysis();
	}

	@Override
	protected void flowThrough(Object outValue, Object _unit,
            Object inValue)
	{
		FlowSet inSet = (FlowSet) inValue;
		FlowSet outSet = (FlowSet) outValue;
		Unit unit = (Unit) _unit;
		
		FlowSet out = PopulateOutSet(inSet, unit, outSet);
		outValue = out;
		
		fl.SetFlowInfo(g.getBody().getMethod().getSignature(), unit, out);
		
		/*
		if(out.size() > 0 )
		{
			System.out.println(g.getBody().getMethod());
			System.out.println(unit + "\n" + out);
			System.out.println("--------------------------------------------------");
		}
		*/
		
	}

	@Override
	protected Object newInitialFlow() 
	{
		 return new ArraySparseSet();
	}

	@Override
	protected Object entryInitialFlow() 
	{

		ArraySparseSet ar=new ArraySparseSet();
    	Iterator<Local> itv=g.getBody().getLocals().iterator();
    	while(itv.hasNext())
    		ar.add(itv.next());
    	
    	return ar;
	}

	@Override
	protected void merge(Object in1, Object in2, Object out) 
	{
        FlowSet inSet1 = (FlowSet) in1;
        FlowSet inSet2 = (FlowSet) in2;
        FlowSet outSet = (FlowSet) out;

        inSet1.union(inSet2, outSet);
        System.out.println(outSet);
	}
	@Override
	protected void copy(Object source, Object dest) 
	{
		FlowSet sourceSet = (FlowSet) source;
        FlowSet destSet   = (FlowSet) dest;
            
        sourceSet.copy(destSet);
	}
	
	protected FlowSet PopulateOutSet(FlowSet in, Unit unit, FlowSet out)
	{
		FlowSet outSet = new ArraySparseSet();
		Stmt stmt = (Stmt) unit;
		
		List<ValueBox> used = unit.getUseBoxes();
		List<ValueBox> def = unit.getDefBoxes();
		
		FlowSet usedSet = new ArraySparseSet();
		FlowSet defSet = new ArraySparseSet();
		
				
		Iterator<ValueBox> it_used = used.iterator();
		while(it_used.hasNext())
		{
			usedSet.add(it_used.next());
		}
		
		Iterator<ValueBox>it_def = def.iterator();
		while(it_def.hasNext())
		{
			defSet.add(it_def.next());
		}
		
		
		
		if(stmt instanceof AssignStmt)
		{
			Value rhs = ((AssignStmt) stmt).getRightOp();
			if(rhs instanceof InvokeExpr)
			{
				InvokeExpr invokeExpr = (InvokeExpr) rhs;
				outSet = getFlowSet(in, out, usedSet,  defSet, invokeExpr);
			}
		}
		
		if(stmt instanceof InvokeStmt)
		{
			InvokeExpr invokeExpr = stmt.getInvokeExpr();
			outSet = getFlowSet(in, out, usedSet,  defSet, invokeExpr);
		}
		
		return outSet;
	}
	
	/*
	 * 0 -> others
	 * 1-> index generate
	 * 2 -> index used
	*/
	protected <T extends InvokeExpr> int invokeMethodType(T invokeExpr)
	{
		SootMethod sm = invokeExpr.getMethod();
		if(sm.getName().equals("indexOf") || sm.getName().equals("charAt"))
		{
			return 1;
		}
		else if(sm.getName().equals("substring") || sm.getName().equals("subSequence"))
		{
			return 2;
		}
		else
		{
			return 0;
		}
	}
	
	/*
	 * out sets
	 */
	protected <T extends InvokeExpr> FlowSet getFlowSet(FlowSet in, FlowSet out, FlowSet usedSet, FlowSet defSet, T invokeExpr)
	{
		FlowSet fs = new ArraySparseSet();
		
		if(invokeMethodType(invokeExpr) == 1)
		{
			in.union(defSet, fs);
		}
		
		else if(invokeMethodType(invokeExpr) == 2)
		{
			in.intersection(usedSet, fs);
		}
		
		return fs;
	}
	

}
