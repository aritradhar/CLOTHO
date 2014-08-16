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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import soot.ArrayType;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.Stmt;


public class ArrayLookAhead extends BodyTransformer
{

	protected void internalTransform(Body jbody, String phaseName, Map options) 
	{
		 SootMethod smethod = jbody.getMethod();
	     System.out.println("<<<<<<<< Current Method : "+smethod.getName()+" >>>>>>>>");
	     
	     PatchingChain<Unit> ch=jbody.getUnits();     	     
	     Iterator<Unit> it = ch.snapshotIterator();
	     
	     HashMap<Integer, Value> stmtCounterMap = new HashMap<>();
	     Integer stmtCounter = 0;
	     
	     
	     while(it.hasNext())
	     {
	    	 stmtCounter++;
	    	 Unit unit = it.next();
	    	 Stmt stmt = (Stmt) unit;
	    	 boolean flag = false;
	    	 
	    	 if(stmt instanceof AssignStmt)
	    		 
	    	 {
	    		 AssignStmt astmt = (AssignStmt) stmt;
	    		 Value rhs = astmt.getRightOp();
	    		 Value lhs = astmt.getLeftOp();
	    		 
	    		 if(rhs instanceof NewArrayExpr)
	    		 {
	    			 stmtCounterMap.put(stmtCounter, lhs);
	    			 flag = true;
	    		 }
	    	 }
	    	 
	    	 if(!flag)
	    	 {
	    		 Iterator<ValueBox> it_valBox= stmt.getUseAndDefBoxes().iterator();
	    		 
	    		 while(it_valBox.hasNext())
	    		 {
	    			 ValueBox vb = it_valBox.next();
	    			 
	    			 Value v = vb.getValue();
	    			 if(v.getType() instanceof ArrayType)
	    				 System.out.println(unit+"   ::  "+v);
	    		 }
	    	 }
	     }
	     
	     System.out.println(stmtCounterMap.entrySet());
	}

}
