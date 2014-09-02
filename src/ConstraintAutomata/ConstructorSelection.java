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

package ConstraintAutomata;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.NewExpr;
import soot.jimple.Stmt;

public class ConstructorSelection extends BodyTransformer
{

	protected void internalTransform(Body jbody, String phaseName, Map options) 
	{
		SootMethod smethod = jbody.getMethod();
	    System.out.println("<<<<<<<< Current Method : "+ smethod.getName() +" >>>>>>>>");
	     
	    PatchingChain<Unit> ch=jbody.getUnits();     	     
	    Iterator<Unit> it = ch.snapshotIterator();
	     
	    Stmt try_start_stmt = null, try_end_stmt = null;

	    if(smethod.getName().equals("<init>"))
	    {
	    	List<Type> constructorParameterTypeList = smethod.getParameterTypes();
	    	Integer parameterCount = constructorParameterTypeList.size();
	    	//System.out.println(jbody);
	    	System.out.println(parameterCount);
	    }
	     
   	 	while(it.hasNext())
	    {
   	 		Unit unit = it.next();
	    	Stmt stmt = (Stmt) unit; 
	    	
	    	if(stmt instanceof AssignStmt)
	    	{
	    		AssignStmt ast = (AssignStmt) stmt;
	    		Value rhsValue = ast.getRightOp();
	    		if(rhsValue instanceof NewExpr)
	    			System.out.println(stmt);
	    	}
	    }
		
	}

}
