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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ConditionExpr;
import soot.jimple.IfStmt;
import soot.jimple.NumericConstant;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;
import soot.jimple.internal.JEqExpr;
import soot.util.Chain;


public class ConstraintCheck 
{
	public Local local;
	public PatchingChain<Unit> pc;
	List<ConditionExpr> constraintList;
	
	public ConstraintCheck(soot.Local local, PatchingChain<Unit> pc)
	{
		this.local = local;
		this.pc = pc;
		
		constraintList = new ArrayList<>();
		
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
			
			if(stmt instanceof IfStmt)
			{
				IfStmt ifStmt = (IfStmt) stmt;
				Value condition = ifStmt.getCondition();
				
				ConditionExpr condExpr = (ConditionExpr) condition;
				
				//getting op1 (*) op2
				
				Value op1 = condExpr.getOp1();
				Value op2 = condExpr.getOp2();
				
				/*
				if(condExpr instanceof JEqExpr)
					System.out.println("Equal");
				*/
				
				//check for constant present in constraint 
				if(op2 instanceof NumericConstant)
				{
					System.out.println("Num present");
					this.constraintList.add(condExpr);
				}
				
				System.out.println(condExpr + " OP1 : " + op1 + " OP2: " + op2);
			}
			
			
			List<ValueBox> useDefBox = stmt.getUseAndDefBoxes();
			
			for(ValueBox vb:useDefBox)
			{
				Value value = vb.getValue();
				if(value instanceof ParameterRef)
					continue;
				if(!(value instanceof Local))
					continue;
				
				if(local.equals((Local)value))
				{
					//System.out.println(u);
					
					
				}
			}
			
		}
	}
	
	public static void main(String[] args) 
	{
		SootClass sc = Scene.v().loadClassAndSupport("StringTest");
		SootMethod sm = sc.getMethodByName("main");
		
		Body b = sm.retrieveActiveBody();
		PatchingChain<Unit> pc = b.getUnits();
		
		Chain<Local> ls = b.getLocals();
		
		new ConstraintCheck(ls.getLast(), pc);
	}
}
