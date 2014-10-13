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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.ConditionExpr;
import soot.jimple.IfStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.NumericConstant;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
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
		
		//this.getConstraint();
		this.getConditionalLocals();
	}
	
	/*
	 * returns a boolean and the SootMethod
	 */
	private static Object[] checkMethods(Value rhs)
	{	
		if(rhs instanceof InvokeExpr)
		{
			InvokeExpr invokeExpr = (InvokeExpr) rhs;
			
			if(invokeExpr instanceof VirtualInvokeExpr)
			{
				VirtualInvokeExpr vInvokeExpr = (VirtualInvokeExpr) invokeExpr;
				SootMethod invokeMethod = vInvokeExpr.getMethod();
				Value base = vInvokeExpr.getBase();
				
				if(base.getType().toString().equals("java.lang.String"))
				{
					
					String subSignature = invokeMethod.getSubSignature();
					
					switch (subSignature) 
					{
					
					case "int length()":
						return new Object[]{true,invokeMethod};
						
					case "boolean startsWith(java.langString)":
						return new Object[]{true,invokeMethod};
						
					case "boolean startsWith(java.langString, int)":
						return new Object[]{true,invokeMethod};

					default:
						return new Object[]{false};
					}				
					
				}
				
				else
				{
					return new Object[]{false};
				}
			}
			else
			{
				return new Object[]{false};
			}
		}
		
		return new Object[]{false};
	}
	
	public HashSet<Local> getConditionalLocals()
	{
		HashSet<Local> locSet = new HashSet<>();
		
		Iterator<Unit> it = pc.iterator();
		
		while(it.hasNext())
		{
			Unit unit = it.next();
			Stmt stmt = (Stmt) unit;
			
			//System.out.println(stmt);
			
			if(stmt instanceof AssignStmt)
			{
				AssignStmt ast = (AssignStmt) stmt;
				
				Value lhs = ast.getLeftOp();
				Value rhs = ast.getRightOp();
				
				if((Boolean)checkMethods(rhs)[0])
				{
					/*
					*Internal iteration
					*check with lhs
					*/			
					Iterator<Unit> it_in = pc.iterator();
					
					while(it_in.hasNext())
					{
						Stmt stmt_in = (Stmt) it_in.next();
						
						if(stmt_in instanceof IfStmt)
						{
							IfStmt ifStmt = (IfStmt) stmt_in;
							Value condition = ifStmt.getCondition();
							
							ConditionExpr condExpr = (ConditionExpr) condition;
							Value op1 = condExpr.getOp1();
							
							if(op1 == lhs)
							{
								System.out.println(lhs);
								locSet.add((Local) lhs);
							}
						}
					}
				}
			}
		}
		
		return locSet;
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
