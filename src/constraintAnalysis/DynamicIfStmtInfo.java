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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.ConditionExpr;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StringConstant;

public class DynamicIfStmtInfo 
{	
	/*
	 * Object array contains
	 * invoke statement, unit, assign statement for string generation
	 *  before which in instrumentation is to be done
	 */
	public static HashMap<IfStmt, Object[]> dynamicIfStmtInfo = new HashMap<>();
	
	/*
	 * returns Object array
	 * object[0] -> boolean -> constraint checking for string API calls
	 * object[1] -> SootMethod
	 * object[2] -> base
	 * object[3] -> argument list
	 */
	
	
	@SuppressWarnings({ "unchecked", "unused" })
	public static void init(IfStmt ifStmt, Object[] ret, String methodSignature, Unit unit)
	{
		Value condition = ifStmt.getCondition();

		ConditionExpr condExpr = (ConditionExpr) condition;
		Value op1 = condExpr.getOp1();
		Value op2 = condExpr.getOp2();
		
		SootMethod calledMethod = (SootMethod)ret[1];
		
		SootClass ConstraintStorageMapDynamicClass = Scene.v().loadClass("constraintAnalysis.ConstraintStorageMapDynamic", SootClass.SIGNATURES);
		
		SootClass stringGenerateDynamicClass = Scene.v().loadClass("constraintAnalysis.GenerateStringDynamic", SootClass.SIGNATURES);
		
		SootMethodRef stringGenerateInitMethodRef = stringGenerateDynamicClass.getMethodByName("init").makeRef();
		
		/*
		 * carry out non length() String API calls here
		 */
		if(((List<Value>) ret[3]).size() >0)
		{
			Value argument = ((List<Value>) ret[3]).get(0);			
			
			SootMethodRef instrMethod = null;
			
			if(calledMethod.getSignature().equals("<java.lang.String: boolean contains(java.lang.CharSequence)>"))
			{
				instrMethod = ConstraintStorageMapDynamicClass.getMethodByName("updateContains").makeRef();
			}
			
			if(calledMethod.getSignature().equals("<java.lang.String: boolean startsWith(java.lang.String)>"))
			{
				instrMethod = ConstraintStorageMapDynamicClass.getMethodByName("updatePrefix").makeRef();
			}
			
			if(calledMethod.getSignature().equals("<java.lang.String: boolean equals(java.lang.Object)>") || 
					calledMethod.getSignature().equals("<boolean equalsIgnoreCase(java.lang.String)>"))
			{
				instrMethod = ConstraintStorageMapDynamicClass.getMethodByName("updateEquals").makeRef();
			}
			
			if(instrMethod == null)
			{
				return;
			}
			
			StaticInvokeExpr staticInvokeExpr = 
					Jimple.v().newStaticInvokeExpr(instrMethod, 
							Arrays.asList(new Value[]{ (Value) StringConstant.v(methodSignature), (Value) ret[2], argument}));
			
			InvokeStmt invokeStmt = Jimple.v().newInvokeStmt(staticInvokeExpr);
			
			Value baseString = (Value) ret[2];
			
			StaticInvokeExpr stringGeneratCall = Jimple.v().newStaticInvokeExpr(stringGenerateInitMethodRef, 
					Arrays.asList( new Value[]{(Value) StringConstant.v(methodSignature), baseString}));
			
			InvokeStmt invStmt = Jimple.v().newInvokeStmt(stringGeneratCall);
			AssignStmt ast = Jimple.v().newAssignStmt(baseString, stringGeneratCall);
			dynamicIfStmtInfo.put(ifStmt, new Object[]{unit, invokeStmt, ast});
			
			//System.out.println("%%%% "  + op1 + ret[1] + ret[3] + (v instanceof StringConstant));
		}
		
		/*
		 * Carry out length() method call heres
		 */
		

		else if((((List<Value>) ret[3]).size() == 0))
		{
			
			SootMethodRef instrMethodMax = ConstraintStorageMapDynamicClass.getMethodByName("updateMaxLength").makeRef();
			SootMethodRef instrMethodMin = ConstraintStorageMapDynamicClass.getMethodByName("updateMinLength").makeRef();
			
			if(calledMethod.getSignature().equals("<java.lang.String: int length()>"))
			{	
				/*
				 * Do if statement process
				 */
				int conditionType = ConstraintCheckDynamic.evaluateCondition(condExpr);
				//instrMethod = ConstraintStorageMapDynamicClass.getMethodByName("updateContains").makeRef();
				/*
				 *Jimple representation		Actual source code (Map)
				 * 1 -> == [JEqExpr] --------> != (2)
				 * 2 -> != [JNeExpr] --------> == (1)
				 * 3 -> >= [JGeExpr] --------> <  (6)
				 * 4 -> >  [JGtExpr] --------> <= (5)
				 * 5 -> <= [JLeExpr] --------> >  (4)
				 * 6 -> <  [JLtExpr] --------> >= (3)
				 */
				Value baseString = (Value) ret[2];
				
				switch(conditionType)
				{
					case 2:
					{
						StaticInvokeExpr staticInvokeExprMax = 
						Jimple.v().newStaticInvokeExpr(instrMethodMax, 
								Arrays.asList(new Value[]{ (Value) StringConstant.v(methodSignature), (Value) ret[2], op2}));
						
						StaticInvokeExpr staticInvokeExprMin = 
								Jimple.v().newStaticInvokeExpr(instrMethodMin, 
										Arrays.asList(new Value[]{ (Value) StringConstant.v(methodSignature), (Value) ret[2], op2}));
						
						InvokeStmt invokeStmtMax = Jimple.v().newInvokeStmt(staticInvokeExprMax);
						InvokeStmt invokeStmtMin = Jimple.v().newInvokeStmt(staticInvokeExprMin);
						
						StaticInvokeExpr stringGeneratCall = Jimple.v().newStaticInvokeExpr(stringGenerateInitMethodRef, 
								Arrays.asList( new Value[]{(Value) StringConstant.v(methodSignature), baseString}));
						
						InvokeStmt invStmt = Jimple.v().newInvokeStmt(stringGeneratCall);
						
						AssignStmt ast = Jimple.v().newAssignStmt(baseString, stringGeneratCall);

						dynamicIfStmtInfo.put(ifStmt, new Object[]{unit, invokeStmtMax, invokeStmtMin, ast});
						break;
					}
						
					case 1:
					{
						Integer val = Integer.parseInt(op2.toString());
						++val;
						Value newOp = IntConstant.v(val);
						
						StaticInvokeExpr staticInvokeExprMax = 
								Jimple.v().newStaticInvokeExpr(instrMethodMax, 
										Arrays.asList(new Value[]{ (Value) StringConstant.v(methodSignature), (Value) ret[2], op2}));
								
						StaticInvokeExpr staticInvokeExprMin = 
								Jimple.v().newStaticInvokeExpr(instrMethodMin, 
										Arrays.asList(new Value[]{ (Value) StringConstant.v(methodSignature), (Value) ret[2], op2}));
								
						InvokeStmt invokeStmtMax = Jimple.v().newInvokeStmt(staticInvokeExprMax);
						InvokeStmt invokeStmtMin = Jimple.v().newInvokeStmt(staticInvokeExprMin);
								
						StaticInvokeExpr stringGeneratCall = Jimple.v().newStaticInvokeExpr(stringGenerateInitMethodRef, 
									Arrays.asList( new Value[]{(Value) StringConstant.v(methodSignature), baseString}));
								
						InvokeStmt invStmt = Jimple.v().newInvokeStmt(stringGeneratCall);
								
						AssignStmt ast = Jimple.v().newAssignStmt(baseString, stringGeneratCall);

						dynamicIfStmtInfo.put(ifStmt, new Object[]{unit, invokeStmtMax, invokeStmtMin, ast});
						break;
					}
					
					case 6:
					{
						StaticInvokeExpr staticInvokeExprMin = 
								Jimple.v().newStaticInvokeExpr(instrMethodMin, 
										Arrays.asList(new Value[]{ (Value) StringConstant.v(methodSignature), (Value) ret[2], op2}));
						InvokeStmt invokeStmtMin = Jimple.v().newInvokeStmt(staticInvokeExprMin);
						
						StaticInvokeExpr stringGeneratCall = Jimple.v().newStaticInvokeExpr(stringGenerateInitMethodRef, 
								Arrays.asList( new Value[]{(Value) StringConstant.v(methodSignature), baseString}));
							
						InvokeStmt invStmt = Jimple.v().newInvokeStmt(stringGeneratCall);
							
						AssignStmt ast = Jimple.v().newAssignStmt(baseString, stringGeneratCall);
						
						dynamicIfStmtInfo.put(ifStmt, new Object[]{unit, invokeStmtMin, ast});
						break;
					}
					
					case 5:
					{
						StaticInvokeExpr staticInvokeExprMin = 
								Jimple.v().newStaticInvokeExpr(instrMethodMin, 
										Arrays.asList(new Value[]{ (Value) StringConstant.v(methodSignature), (Value) ret[2], op2}));
						InvokeStmt invokeStmtMin = Jimple.v().newInvokeStmt(staticInvokeExprMin);
						
						StaticInvokeExpr stringGeneratCall = Jimple.v().newStaticInvokeExpr(stringGenerateInitMethodRef, 
								Arrays.asList( new Value[]{(Value) StringConstant.v(methodSignature), baseString}));
							
						InvokeStmt invStmt = Jimple.v().newInvokeStmt(stringGeneratCall);
							
						AssignStmt ast = Jimple.v().newAssignStmt(baseString, stringGeneratCall);
						
						dynamicIfStmtInfo.put(ifStmt, new Object[]{unit, invokeStmtMin, ast});
						break;
					}
					
					case 4:
					{
						StaticInvokeExpr staticInvokeExprMax = 
								Jimple.v().newStaticInvokeExpr(instrMethodMax, 
										Arrays.asList(new Value[]{ (Value) StringConstant.v(methodSignature), (Value) ret[2], op2}));
						InvokeStmt invokeStmtMax = Jimple.v().newInvokeStmt(staticInvokeExprMax);
						
						StaticInvokeExpr stringGeneratCall = Jimple.v().newStaticInvokeExpr(stringGenerateInitMethodRef, 
								Arrays.asList( new Value[]{(Value) StringConstant.v(methodSignature), baseString}));
							
						InvokeStmt invStmt = Jimple.v().newInvokeStmt(stringGeneratCall);
							
						AssignStmt ast = Jimple.v().newAssignStmt(baseString, stringGeneratCall);
						
						dynamicIfStmtInfo.put(ifStmt, new Object[]{unit, invokeStmtMax, ast});
						break;
					}
					
					case 3:
					{
						StaticInvokeExpr staticInvokeExprMax = 
								Jimple.v().newStaticInvokeExpr(instrMethodMax, 
										Arrays.asList(new Value[]{ (Value) StringConstant.v(methodSignature), (Value) ret[2], op2}));
						InvokeStmt invokeStmtMax = Jimple.v().newInvokeStmt(staticInvokeExprMax);
						
						StaticInvokeExpr stringGeneratCall = Jimple.v().newStaticInvokeExpr(stringGenerateInitMethodRef, 
								Arrays.asList( new Value[]{(Value) StringConstant.v(methodSignature), baseString}));
							
						InvokeStmt invStmt = Jimple.v().newInvokeStmt(stringGeneratCall);
							
						AssignStmt ast = Jimple.v().newAssignStmt(baseString, stringGeneratCall);
						
						dynamicIfStmtInfo.put(ifStmt, new Object[]{unit, invokeStmtMax, ast});
						break;
					}
				}
			}
			
		}
		/*
		else
		{
			System.out.println("%%%% "  + op1 + ret[1] + ret[3]);
		}
		*/
		
	}
}
