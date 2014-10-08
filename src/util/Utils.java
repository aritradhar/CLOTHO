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

package util;

import soot.SootMethod;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;

public class Utils 
{
	public static SootMethod getInvokedMethodFromStmt(Stmt stmt)
	{
		if(stmt instanceof AssignStmt)
		{
			AssignStmt ast = (AssignStmt) stmt;
			Value  rhs = ast.getRightOp();
			
			if(rhs instanceof InvokeExpr)
			{
				InvokeExpr invokeExpr = (InvokeExpr) rhs;
				
				return invokeExpr.getMethod();	
			}
		}
		
		else if(stmt instanceof InvokeStmt)
		{
			InvokeExpr invokeExpr = stmt.getInvokeExpr();
			
			return invokeExpr.getMethod();
		}
		
		return null;
	}
}
