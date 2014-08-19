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
import java.util.List;
import java.util.Map;

import soot.ArrayType;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
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
	     
	     HashMap<Integer, Local> newArrayExprMap = new HashMap<>();
	     Integer stmtCounter = 0;
	     
	     
	     while(it.hasNext())
	     {
	    	 stmtCounter++;
	    	 Unit unit = it.next();
	    	 Stmt stmt = (Stmt) unit;
	    	 boolean flag = false;
	    	 
	    	 //ignore all identity statement
	    	 if(stmt instanceof IdentityStmt)
	    		 continue;
	    	 
	    	 
	    	 if(stmt instanceof AssignStmt)	    		 
	    	 {
	    		 
	    		 AssignStmt astmt = (AssignStmt) stmt;
	    		 Value rhs = astmt.getRightOp();
	    		 Value lhs = astmt.getLeftOp();
	    		 
	    		 if(rhs instanceof NewArrayExpr)
	    		 {
	    			 newArrayExprMap.put(stmtCounter, (Local)lhs);
	    			 flag = true;
	    		 }
	    		 
	    		 if(rhs instanceof ArrayRef)
	    		 {
	    			 
	    			 ArrayRef aRef = stmt.getArrayRef();
	    			 Value arrayBaseValue = aRef.getBase();
	    			 Value arrayIndexValue = aRef.getIndex();
	    			 
	    			 System.out.println("#####   "+stmt+" "+arrayBaseValue+" "+arrayIndexValue);
	    		 }
	    	 }
	    	 
	    	 if(!flag)
	    	 {
	    		 List<ValueBox> vlist= stmt.getUseAndDefBoxes();
	    		 Iterator<ValueBox> it_valBox= vlist.iterator();
	    		 
	    		 int arrFlag = 0;
	    		 Value tempVal = null;
	    		 
	    		 while(it_valBox.hasNext())
	    		 {
	    			 
	    			 ValueBox vb = it_valBox.next();
	    			 Value v = vb.getValue();
	    			 
	    			 if(vb.toString().contains("LinkedRValueBox"))
	    			 {
	    				 arrFlag++;
	    				 tempVal = v;
	    			 }
	    			 
	    			 if(v.getType() instanceof ArrayType)
	    			 {	    		
	    				 arrFlag++;
	    				 //System.out.println(unit+"   ::  "+v);	
	    				 //System.out.println(vlist);
	    			 }
	    			 
	    			 if(arrFlag == 2)
	    			 {
	    				 //System.out.println(tempVal+"  "+tempVal.getType());
	    			 }
	    		 }
	    	 }
	     }
	     
	     System.out.println(newArrayExprMap.entrySet());
	}

}
