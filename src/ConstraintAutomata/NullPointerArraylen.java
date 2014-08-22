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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import profile.InstrumManager;
import profile.UtilInstrum;
import soot.Body;
import soot.BodyTransformer;
import soot.IntType;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LengthExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.Stmt;

public class NullPointerArraylen extends BodyTransformer
{
	protected void internalTransform(Body jbody, String phaseName, Map options) 
	{
		SootMethod smethod = jbody.getMethod();
	     System.out.println("<<<<<<<< Current Method : "+ smethod.getName() +" >>>>>>>>");
	     
	     PatchingChain<Unit> ch=jbody.getUnits();     	     
	     Iterator<Unit> it = ch.snapshotIterator();
	     
	     Stmt try_start_stmt = null, try_end_stmt = null;
	     
	     /*
	      * Test
	      * 
	     NewExpr newE = Jimple.v().newNewExpr(RefType.v("Test1"));
    	 AssignStmt a = Jimple.v().newAssignStmt(UtilInstrum.getCreateLocal(jbody, "<Obj>", RefType.v("Test1")), newE);
	     System.out.println(a);
	     */
	     
    	 while(it.hasNext())
	     {
	    	 Unit unit = it.next();
	    	 Stmt stmt = (Stmt) unit;
	    	 
	    	 if(stmt instanceof IdentityStmt)
	    		 continue;
	    	 
	    	 //System.out.println("## "+stmt);
	    	 
	    	 if(stmt instanceof AssignStmt)
	    	 {
	    		 AssignStmt ast = (AssignStmt) stmt;
	    		 Value val_rhs = ast.getRightOp();
	    		 
	    		 Value val_lhs = ast.getLeftOp();
	    		 
	    		 if(val_rhs instanceof LengthExpr)
	    		 {
	    			 LengthExpr lenExpr = (LengthExpr) val_rhs;    			 
	    			 Value baseArray = lenExpr.getOp();
	    			 
	    			 try_start_stmt = stmt;
	    			 try_end_stmt = (Stmt) ch.getSuccOf(stmt);
	    			 
	    			 List<Stmt> probe = new ArrayList<Stmt>();
	    	    	 SootClass thrwCls = Scene.v().getSootClass("java.lang.NullPointerException");
	    	    	 Stmt sGotoLast = Jimple.v().newGotoStmt(try_end_stmt);
	    	    	 probe.add(sGotoLast);
	    	    	 
	    	    	 Local lException1 = UtilInstrum.getCreateLocal(jbody, "<ex1>", RefType.v(thrwCls));
	    	    	 Stmt sCatch = Jimple.v().newIdentityStmt(lException1, Jimple.v().newCaughtExceptionRef());
	    	    	 probe.add(sCatch);
	    	    	 
	    	    	 //catch block
	    	    	 
	    	    	 //set the new size	    	 	    	    	 
	    	    	 Value ARRAY_SIZE = IntConstant.v(10);
	    	    	 
	    	    	 //base array reinitialize
	    	    	 
	    	    	 NewArrayExpr newArray = Jimple.v().newNewArrayExpr(IntType.v(), ARRAY_SIZE);
	    	    	 AssignStmt arrayAssign = Jimple.v().newAssignStmt(baseArray, newArray);	    	    	 
	    	    	 
	    	    	 probe.add(arrayAssign);
	    	    	 
	    	    	 //recalculate the length
	    	    	 LengthExpr newLenExpr = Jimple.v().newLengthExpr(baseArray);
	    	    	 AssignStmt lengthAssign = Jimple.v().newAssignStmt(val_lhs, newLenExpr);	
	    	    	 
	    	    	 probe.add(lengthAssign);
	    	    	 
	    	    	 InstrumManager.v().insertRightBeforeNoRedirect(ch, probe, try_end_stmt);
		    			
	    	    	 // insert trap (catch)
	    	    	 jbody.getTraps().add(Jimple.v().newTrap(thrwCls, try_start_stmt, sGotoLast, sCatch));
	    	    	 jbody.validate();
	    		 }
	    		 
	    		 Value arr_val = null;
	    		 
	    		 if(val_lhs instanceof ArrayRef || val_rhs instanceof ArrayRef)
	    		 {
	    			 if(val_lhs instanceof ArrayRef)
	    			 {
	    				 arr_val = ast.getLeftOp();	    			 
	    			 }
	    			 
	    			 else if(val_rhs instanceof ArrayRef)
	    			 {
	    				 arr_val = ast.getRightOp();	
	    			 }
	    			 
	    			 ArrayRef arf = (ArrayRef) arr_val;
	    			 Value baseArray = arf.getBase();
	    			 //System.out.println(baseArray);
	    			 
	    			 Stmt copy_stmt = (Stmt) stmt.clone();
	    			 
	    			 try_start_stmt = stmt;
	    			 try_end_stmt = (Stmt) ch.getSuccOf(stmt);
	    			 
	    			 List<Stmt> probe = new ArrayList<Stmt>();
	    	    	 SootClass thrwCls = Scene.v().getSootClass("java.lang.NullPointerException");
	    	    	 Stmt sGotoLast = Jimple.v().newGotoStmt(try_end_stmt);
	    	    	 probe.add(sGotoLast);
	    	    	 
	    	    	 Local lException1 = UtilInstrum.getCreateLocal(jbody, "<ex1>", RefType.v(thrwCls));
	    	    	 Stmt sCatch = Jimple.v().newIdentityStmt(lException1, Jimple.v().newCaughtExceptionRef());
	    	    	 probe.add(sCatch);
	    	    	 
	    	    	 //catch block
	    	    	 
	    	    	 //set the new size	    	 	    	    	 
	    	    	 Value ARRAY_SIZE = IntConstant.v(10);
	    	    	 
	    	    	 //base array reinitialize
	    	    	 
	    	    	 NewArrayExpr newArray = Jimple.v().newNewArrayExpr(IntType.v(), ARRAY_SIZE);
	    	    	 AssignStmt arrayAssign = Jimple.v().newAssignStmt(baseArray, newArray);	    	    	 
	    	    	 
	    	    	 probe.add(arrayAssign);
	    	    	 //probe.add(copy_stmt);
	    	    	 
	    	    	 InstrumManager.v().insertRightBeforeNoRedirect(ch, probe, try_end_stmt);
		    			
	    	    	 // insert trap (catch)
	    	    	 jbody.getTraps().add(Jimple.v().newTrap(thrwCls, try_start_stmt, sGotoLast, sCatch));
	    	    	 jbody.validate();
	    		 }
	    		 
	    		 
	    	 }
	     }
	}
}
