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


import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.ConditionExpr;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.NumericConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.internal.JEqExpr;
import soot.jimple.internal.JGeExpr;
import soot.jimple.internal.JGtExpr;
import soot.jimple.internal.JLeExpr;
import soot.jimple.internal.JNeExpr;
import util.ENV;


public class ConstraintCheck extends BodyTransformer
{
	/*
	 * No longer needed
	 * s
	List<ConditionExpr> constraintList;
	
	//contains the locals in the conditional statement and the 
	//string methods by which they were initialized
	HashMap<Local, SootMethod> conditionalLocals;
	 */
	
	/*
	 * 1 -> == [JEqExpr]
	 * 2 -> != [JNeExpr]
	 * 3 -> >= [JGeExpr]
	 * 4 -> >  [JGtExpr]
	 * 5 -> <= [JLeExpr]
	 * 6 -> <  [JLtExpr]
	 */
	
	private int evaluateCondition(ConditionExpr conditionExpr)
	{
		if(conditionExpr instanceof JEqExpr)
			return 1;
		
		else if(conditionExpr instanceof JNeExpr)
			return 2;
		
		else if(conditionExpr instanceof JGeExpr)
			return 3;
		
		else if(conditionExpr instanceof JGtExpr)
			return 4;
		
		else if(conditionExpr instanceof JLeExpr)
			return 5;
		
		else
			return 6;
		
	}
	
	
	/*
	 * returns Object array
	 * object[0] -> boolean -> constraint checking for string API calls
	 * object[1] -> SootMethod
	 * object[2] -> base
	 * object[3] -> argument list
	 */
	private Object[] checkMethods(Value rhs)
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
					List<Value> args = vInvokeExpr.getArgs();
					
					switch (subSignature) 
					{
					
					case "int length()":
						return new Object[]{true, invokeMethod, base, args};
						
					case "boolean startsWith(java.lang.String)":
						return new Object[]{true, invokeMethod, base, args};
						
					case "boolean startsWith(java.lang.String, int)":
						return new Object[]{true, invokeMethod, base, args};
						
					case "boolean endsWith(java.lang.String)":
						return new Object[]{true, invokeMethod, base, args};
						
					case "boolean contains(java.lang.CharSequence)":
						return new Object[]{true, invokeMethod, base, args};
						
					case "boolean equals(java.lang.Object)":
						return new Object[]{true, invokeMethod, base, args};
						
					case "boolean equalsIgnoreCase(java.lang.String)":
						return new Object[]{true, invokeMethod, base, args};

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
	
	/*
	 * ret Object array structure
	 * object[0] -> boolean -> constraint checking for string API calls
	 * object[1] -> SootMethod
	 * object[2] -> base
	 * object[3] -> argument list
	 */
	
	@SuppressWarnings("unchecked")
	private void populateConstraintMap(IfStmt ifStmt, Object[] ret, Value lhs, Value rhs, String methodSignature)
	{
		
		Value condition = ifStmt.getCondition();

		ConditionExpr condExpr = (ConditionExpr) condition;
		Value op1 = condExpr.getOp1();

		Value op2 = condExpr.getOp2();
		
		if(!(op2 instanceof NumericConstant))
			return;
								
		
		if(op1 == lhs)
		{
			/*
			 *Jimple representation		Actual source code (Map)
			 * 1 -> == [JEqExpr] --------> != (2)
			 * 2 -> != [JNeExpr] --------> == (1)
			 * 3 -> >= [JGeExpr] --------> <  (6)
			 * 4 -> >  [JGtExpr] --------> <= (5)
			 * 5 -> <= [JLeExpr] --------> >  (4)
			 * 6 -> <  [JLtExpr] --------> >= (3)
			 */
			int conditionType = evaluateCondition(condExpr);
			
			switch(conditionType)
			{
				case 2:
					ConstraintStorageMap.updateMaxLength(methodSignature, (Value) ret[2], op2);
					ConstraintStorageMap.updateMinLength(methodSignature, (Value) ret[2], op2);
					break;
					
				case 1:
					Integer val = Integer.parseInt(op2.toString());
					++val;
					Value newOp = IntConstant.v(val);
					
					ConstraintStorageMap.updateMaxLength(methodSignature, (Value) ret[2], newOp);
					ConstraintStorageMap.updateMinLength(methodSignature, (Value) ret[2], newOp);
					break;
					
				case 6:
					ConstraintStorageMap.updateMinLength(methodSignature, (Value) ret[2], op2);
					break;
					
				case 5:
					ConstraintStorageMap.updateMinLength(methodSignature, (Value) ret[2], op2);
					break;
					
				case 4:
					ConstraintStorageMap.updateMaxLength(methodSignature, (Value) ret[2], op2);
					break;
					
				case 3:
					ConstraintStorageMap.updateMaxLength(methodSignature, (Value) ret[2], op2);
					break;
					
			}
			//System.out.println(op2.getType() +" " +op2);
			SootMethod condMethod = (SootMethod) ret[1];
			String condMethodSignature = condMethod.getSignature();
			
			if(condMethodSignature.equals("<java.lang.String: boolean startsWith(java.lang.String)>"))
			{
				List<Value> argList = (List<Value>) ret[3];
				Value arg = argList.get(0);
				if(arg instanceof StringConstant)
					ConstraintStorageMap.updatePrefix(methodSignature,(Value) ret[2], arg);
			}
			
			if(condMethodSignature.equals("<java.lang.String: boolean contains(java.lang.CharSequence)>"))
			{
				List<Value> argList = (List<Value>) ret[3];
				Value arg = argList.get(0);
				if(arg instanceof StringConstant)
					ConstraintStorageMap.updateContains(methodSignature,(Value) ret[2], arg);
			}
			
			if(condMethodSignature.equals("<java.lang.String: boolean equals(java.lang.Object)>") ||
					condMethodSignature.equals("<java.lang.String: boolean equalsIgnoreCase(java.lang.Object)>"))
			{
				List<Value> argList = (List<Value>) ret[3];
				Value arg = argList.get(0);
				if(arg instanceof StringConstant)
					ConstraintStorageMap.updateEquals(methodSignature,(Value) ret[2], arg);
			}
			
			System.out.println(lhs + " : " + condMethodSignature + " : " + ret[2] + " : " + ret[3]);
		}
	}
	
	
			

	@Override
	protected void internalTransform(Body jbody, String phaseName, Map options) 
	{
		SootMethod sm = jbody.getMethod();
	
		System.out.println("Method : "+sm.getName());
		
		PatchingChain<Unit> pc = jbody.getUnits();
		
		Iterator<Unit> it = pc.iterator();
		
		while(it.hasNext())
		{
			Unit unit = it.next();
			Stmt stmt = (Stmt) unit;
			
			/*
			 * Populate the stats here for total no off units scanned
			 */
			ENV.STAT_UNIT_HANDLED++;
			
			if(stmt instanceof AssignStmt)
			{
				AssignStmt ast = (AssignStmt) stmt;

				Value lhs = ast.getLeftOp();
				Value rhs = ast.getRightOp();

				Object []ret = checkMethods(rhs);
				
				if((Boolean)ret[0])
				{
					/*
					 *Internal iteration check with lhs
					 */			
					Iterator<Unit> it_in = pc.iterator();

					
					boolean flag = false;
					
					while(it_in.hasNext())
					{
						Stmt stmt_in = (Stmt) it_in.next();
						
						if(stmt_in != stmt && !flag)
							continue;
						
						else
							flag = true;

						if(stmt_in instanceof IfStmt)
						{
							IfStmt ifStmt = (IfStmt) stmt_in;
							populateConstraintMap(ifStmt, ret, lhs, rhs, jbody.getMethod().getSignature());
						}
					}
				}
			}
		}
		
	}
}
