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
		
		/*
		 * carry out non length() String API calls here
		 */
		if(((List<Value>) ret[3]).size() >0)
		{
			Value v = ((List<Value>) ret[3]).get(0);
		
			SootClass ConstraintStorageMapDynamicClass = Scene.v().loadClass("constraintAnalysis.ConstraintStorageMapDynamic", SootClass.SIGNATURES);
			
			SootClass stringGenerateDynamicClass = Scene.v().loadClass("constraintAnalysis.GenerateStringDynamic", SootClass.SIGNATURES);
			
			SootMethodRef stringGenerateInitMethodRef = stringGenerateDynamicClass.getMethodByName("init").makeRef();
			
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
			
			
			StaticInvokeExpr staticInvokeExpr = 
					Jimple.v().newStaticInvokeExpr(instrMethod, 
							Arrays.asList(new Value[]{ (Value) StringConstant.v(methodSignature), (Value) ret[2], v}));
			
			InvokeStmt invokeStmt = Jimple.v().newInvokeStmt(staticInvokeExpr);
			
			Value baseString = (Value) ret[2];
			
			StaticInvokeExpr stringGeneratCall = Jimple.v().newStaticInvokeExpr(stringGenerateInitMethodRef, 
					Arrays.asList( new Value[]{(Value) StringConstant.v(methodSignature), baseString}));
			
			InvokeStmt invStmt = Jimple.v().newInvokeStmt(stringGeneratCall);
			
			AssignStmt ast = Jimple.v().newAssignStmt(baseString, stringGeneratCall);
			
			dynamicIfStmtInfo.put(ifStmt, new Object[]{unit, invokeStmt});
			
			//System.out.println("%%%% "  + op1 + ret[1] + ret[3] + (v instanceof StringConstant));
		}
		/*
		else
		{
			System.out.println("%%%% "  + op1 + ret[1] + ret[3]);
		}
		*/
		
	}
}
