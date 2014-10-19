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

package constraintAnalysis.stringRepair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import constraintAnalysis.ConstraintStorageDataType;
import constraintAnalysis.ConstraintStorageMap;
import constraintAnalysis.DynamicIfStmtInfo;
import constraintAnalysis.GenerateString;
import polyglot.ast.Assign;
import profile.InstrumManager;
import profile.UtilInstrum;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.VirtualInvokeExpr;

public class StringRepairConstraintDynamic extends BodyTransformer
{
	
	private <T extends InvokeExpr> List<Stmt> constraintCheckPatchProbe(Body jbody, Value lhs, T InvokeExpr)
	{
		List<Stmt> probe = new ArrayList<Stmt>();
		
		VirtualInvokeExpr virtualInvokeExpr = (InvokeExpr instanceof VirtualInvokeExpr) ? (VirtualInvokeExpr)InvokeExpr : null;

		StaticInvokeExpr staticInvokeExpr = (InvokeExpr instanceof StaticInvokeExpr) ? (StaticInvokeExpr)InvokeExpr : null;
		
		Type stringType = RefType.v("java.lang.String");
		
		if(InvokeExpr instanceof VirtualInvokeExpr && lhs.getType() == stringType)
		{
			Value baseString = virtualInvokeExpr.getBase();
			
			String methodSignature = jbody.getMethod().getSignature();
				
			HashMap<Value, ConstraintStorageDataType> CSDTmap = ConstraintStorageMap.constraintStorageMap.get(methodSignature);
			
			/*
			 * it can not retrieve the value even the hashcode is same
			 * CSDTmap.get(baseString);
			 * ConstraintStorageDataType CSDT = CSDTmap.get(baseString);
			*/
			ConstraintStorageDataType CSDT = ConstraintStorageMap.CSDTget(lhs, CSDTmap);
			
			/*
			 * Sanity check
			 */
			if(CSDT == null)
				return null;
			
			String generatedString = GenerateString.init(methodSignature, lhs, CSDT);
			
			
			AssignStmt patchAssign = Jimple.v().newAssignStmt(lhs, StringConstant.v(generatedString));
			
			probe.add(patchAssign);
		
		}
		
		return probe;
	}

	private <T extends InvokeExpr> Body  makePatchProbe(PatchingChain<Unit> ch ,
			Body jbody, Stmt try_start_stmt, Stmt try_end_stmt, Value lhs, T InvokeExpr)
	{
		List<Stmt> probe = new ArrayList<Stmt>();
		 
		 SootMethod sMethod = InvokeExpr.getMethod();
		
		 /*
		  * Sanity check
		  */
		 if(sMethod == null)
			 return null;
		 
		 //System.out.println(sMethod.getSubSignature());
		 
	
		 SootClass thrwCls = null; 
		 //VirtualInvokeExpr virtualInvokeExpr = (InvokeExpr instanceof VirtualInvokeExpr) ? (VirtualInvokeExpr)InvokeExpr : null;

		 //StaticInvokeExpr staticInvokeExpr = (InvokeExpr instanceof StaticInvokeExpr) ? (StaticInvokeExpr)InvokeExpr : null;
		
		 boolean containsAPIcall = false;
		 //Debug
		 //System.out.println(sMethod);
		 
		 if(sMethod.getSubSignature().equals("char charAt(int)") || sMethod.getSubSignature().equals("int codePointAt(int)") 
				 || sMethod.getSubSignature().equals("int codePointBefore(int)") 
				 || sMethod.getSubSignature().equals("int codePointCount(int,int)")
				 || sMethod.getSubSignature().equals("int offsetByCodePoints(int,int)") 
				 || sMethod.getSubSignature().equals("void getChars(int,int,char[],int)")
				 || sMethod.getSubSignature().equals("void getBytes(int,int,byte[],int)")
				 || sMethod.getSubSignature().equals("java.lang.String substring(int)")
				 || sMethod.getSubSignature().equals("java.lang.String substring(int,int)")
				 || sMethod.getSubSignature().equals("java.lang.CharSequence subSequence(int,int)")
				 || sMethod.getSubSignature().equals("java.lang.String valueOf(char[],int,int)")
				 )
		 {
			 thrwCls = Scene.v().getSootClass("java.lang.IndexOutOfBoundsException");
			 containsAPIcall = true;
		 }
		 else
		 {
			 return null;
		 }
		 
		 Stmt sGotoLast = Jimple.v().newGotoStmt(try_end_stmt);
    	 probe.add(sGotoLast);
    	 
    	 //prepare for catch block
    	 Double d = Math.ceil(Math.random()*100000000);
    	 Local lException1 = UtilInstrum.getCreateLocal(jbody, "<ex" + d.toString().replace(".", "") + ">", RefType.v(thrwCls));
    	 Stmt sCatch = Jimple.v().newIdentityStmt(lException1, Jimple.v().newCaughtExceptionRef());
    	 probe.add(sCatch);
    	 
    	 /*
    	  * Catch block will contains the patching code based on the API call
    	  */
    	 if(containsAPIcall)
    	 {
    		 probe.addAll(constraintCheckPatchProbe(jbody, lhs, InvokeExpr));
    	 }
    	 
    	 
    	 
    	 InstrumManager.v().insertRightBeforeNoRedirect(ch, probe, try_end_stmt);
		 //instr
		 jbody.getTraps().add(Jimple.v().newTrap(thrwCls, try_start_stmt, sGotoLast, sCatch));
    	 jbody.validate(); 
		 
		 
		 return jbody;
	}
	
	
	@Override
	@SuppressWarnings("rawtypes")
	protected void internalTransform(Body body, String phaseName, Map options) 
	{
		SootMethod sMethod = body.getMethod();		
        
		if(sMethod.getName().startsWith("<"))
			return;
		
		System.out.println("---- Current Method : " + sMethod.getName() + " ----");
        
        String methodSignature = sMethod.getSignature();
		
        
		PatchingChain<Unit> pc= body.getUnits();
		
		Iterator<Unit> it = pc.snapshotIterator();
		
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
												
				//search for the variables in the conditional expression in the if statement
				
				Object []ret = constraintAnalysis.ConstraintCheckDynamic.checkMethods(rhs);
				
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
							DynamicIfStmtInfo.init(ifStmt, ret, methodSignature, unit);
						}
					}
				}
				
				
				
				
				
				//For string API all the calls would be virtual invoke expression
				if(rhs instanceof VirtualInvokeExpr)
				{
					
					//System.out.println(stmt);
					VirtualInvokeExpr virtualInvokeExpr = (VirtualInvokeExpr) rhs;
					
					Body b = makePatchProbe(pc, body, stmt, (Stmt)pc.getSuccOf(stmt), lhs, virtualInvokeExpr);
					
					if(b == null)
						continue;
					
					
				}
				
				if(rhs instanceof StaticInvokeExpr)
				{
					StaticInvokeExpr staticInvokeExpr = (StaticInvokeExpr) rhs;
					
					Body b = makePatchProbe(pc, body, stmt, (Stmt)pc.getSuccOf(stmt), lhs, staticInvokeExpr);
					
					if(b == null)
						continue;
				}
			}
			
			if(stmt instanceof InvokeStmt)
			{
				InvokeExpr invokeExpr = stmt.getInvokeExpr();
				
				if(invokeExpr instanceof VirtualInvokeExpr)
				{
					//System.out.println(stmt);
					
					VirtualInvokeExpr virtualInvokeExpr = (VirtualInvokeExpr) invokeExpr;
					
					Body b = makePatchProbe(pc, body, stmt, (Stmt)pc.getSuccOf(stmt), null, virtualInvokeExpr);
					
					if(b == null)
						continue;							
				}
				
				if(invokeExpr instanceof StaticInvokeExpr)
				{
					//System.out.println(stmt);
					
					StaticInvokeExpr staticInvokeExpr = (StaticInvokeExpr) invokeExpr;
					
					//Body b = makePatchProbe(pc, body, stmt, (Stmt)pc.getSuccOf(stmt), null, staticInvokeExpr);
					
					//if(b == null)
						//continue;							
				}
			}
		}
		it  = null;
		
		/*
		for(IfStmt ifs : DynamicIfStmtInfo.dynamicIfStmtInfo.keySet())
		{
			System.out.println("IfStmt : " + ifs);
			System.out.println(DynamicIfStmtInfo.dynamicIfStmtInfo.get(ifs)[0]);
		}
		*/
		
		
		/*
		 * 2nd pass for instrumentation
		 */
				
		it = pc.snapshotIterator();
		
		while(it.hasNext())
		{
			Unit unit = it.next();
			Stmt stmt = (Stmt) unit;
			
			if(stmt instanceof IfStmt)
			{
				IfStmt ifs = (IfStmt) stmt;
				
				if(DynamicIfStmtInfo.dynamicIfStmtInfo.containsKey(ifs))
				{
					Unit invokePoint = (Unit) DynamicIfStmtInfo.dynamicIfStmtInfo.get(ifs)[0];
					InvokeStmt toInstrument = (InvokeStmt) DynamicIfStmtInfo.dynamicIfStmtInfo.get(ifs)[1];
					AssignStmt ast = (AssignStmt) DynamicIfStmtInfo.dynamicIfStmtInfo.get(ifs)[2];
					
					pc.insertBefore(toInstrument, invokePoint);
					
					pc.insertAfter(ast, toInstrument);
				}
			}
		}
		
	}
	
}
	

