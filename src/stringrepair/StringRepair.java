package stringrepair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import profile.InstrumManager;
import profile.UtilInstrum;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Pack;
import soot.PackManager;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootResolver;
import soot.Transform;
import soot.Unit;
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


import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import soot.options.Options;
import ConstraintAutomata.MethodRefChanger;


public class StringRepair extends BodyTransformer
{
	public static void main(String[] args) 
	{
        
        String []className = {"StringTest"};
  
        Pack jtp = PackManager.v().getPack("jtp");

        jtp.add(new Transform("jtp.instrumenter", new StringRepair()));
        Options.v().setPhaseOption("jb", "use-original-names:true");
        
        
        soot.Main.main(className);
	}
	
	
	/*
	 * Get method String API method call from the virtual invoke expression
	 */
	private SootMethod getVirtualStringMethodCall(VirtualInvokeExpr virtualInvokeExpr)
	{
		Value base = virtualInvokeExpr.getBase();
		
		SootClass baseClass = MethodRefChanger.getProperClass(base);
		
		//skip for primitive types
		if(baseClass == null)
			return null;
		
		//now trigger patching strategy based on method call
		if(baseClass.getName().equals("java.lang.String"))
		{
			return virtualInvokeExpr.getMethod();
		}
		
		return null;
	}
	
	/*
	private List<Stmt> subStringPatchProbe(Value lhs, Stmt stmt)
	{
		
	}*/
	
	private Body makePatchProbe(PatchingChain<Unit> ch , Body jbody, Stmt try_start_stmt, Stmt try_end_stmt, Value lhs, VirtualInvokeExpr virtualInvokeExpr)
	{
		 List<Stmt> probe = new ArrayList<Stmt>();
		 //System.out.println(sMethod.getSubSignature());
		 SootMethod sMethod = getVirtualStringMethodCall(virtualInvokeExpr);
		 
		 if(sMethod == null)
			 return null;
		 
		 SootClass thrwCls = null;
		 
		 SootClass s = Scene.v().getSootClass("java.lang.String");

		 if(sMethod.getSubSignature().equals("char chatAt(int)") || sMethod.getSubSignature().equals("inr codePointAt(int)") 
				 || sMethod.getSubSignature().equals("int codePointBefore(int)") 
				 || sMethod.getSubSignature().equals("int codePointCount(int,int)")
				 || sMethod.getSubSignature().equals("int offsetByCodePoints(int,int)") 
				 || sMethod.getSubSignature().equals("void getChars(int,int,char[],int)")
				 || sMethod.getSubSignature().equals("void getBytes(int,int,byte[],int)")
				 || sMethod.getSubSignature().equals("java.lang.String substring(int)")
				 || sMethod.getSubSignature().equals("java.lang.String substring(int,int)")
				 || sMethod.getSubSignature().equals("java.lang.CharSequence subSequence(int,int)")
				 || sMethod.getSubSignature().equals("java.lang.String valueOf(char[],int,int)")
				 || sMethod.getSubSignature().equals("java.lang.String valueOf(char[],int,int)"))
		 {
			 thrwCls = Scene.v().getSootClass("java.lang.IndexOutOfBoundsException");
		 
		 }
		 else
		 {
			 return null;
		 }
		 
		 if(sMethod.getSubSignature().equals("char chatAt(int)"))
		 {
			 
		 }
		 
		 Stmt sGotoLast = Jimple.v().newGotoStmt(try_end_stmt);
    	 probe.add(sGotoLast);
    	 
    	 //prepare for catch block
    	 Double d = Math.random();
    	 Local lException1 = UtilInstrum.getCreateLocal(jbody, "<ex>" + d.toString() , RefType.v(thrwCls));
    	 Stmt sCatch = Jimple.v().newIdentityStmt(lException1, Jimple.v().newCaughtExceptionRef());
    	 probe.add(sCatch);
    	 
    	 //call patching function based on methods
    	 
    	 if(lhs != null)
    	 {
    		 
    	 }
    	 
    	 InstrumManager.v().insertRightBeforeNoRedirect(ch, probe, try_end_stmt);
		 //instr
		 jbody.getTraps().add(Jimple.v().newTrap(thrwCls, try_start_stmt, sGotoLast, sCatch));
    	 jbody.validate(); 
		 
		 return jbody;
	}

	@Override
	protected void internalTransform(Body body, String phaseName, Map options) 
	{
		SootMethod sMethod = body.getMethod();
			
		if(sMethod.getName().startsWith("<"))
			return;
		System.out.println("---- Current Method : " + sMethod.getName() + " ----");
		
		PatchingChain<Unit> pc= body.getUnits();
		
		Iterator<Unit> it = pc.snapshotIterator();
		
		while(it.hasNext())
		{
			Unit unit = it.next();
			Stmt stmt = (Stmt) unit;
			
			if(stmt instanceof AssignStmt)
			{
				AssignStmt ast = (AssignStmt) stmt;
				
				Value lhs = ast.getLeftOp();
				Value rhs = ast.getRightOp();
				
				//For string API all the calls would be virtual invoke expression
				if(rhs instanceof VirtualInvokeExpr)
				{
					VirtualInvokeExpr virtualInvokeExpr = (VirtualInvokeExpr) rhs;
					
					/*
					 * 
					SootMethod calledMethod = getVirtualStringMethodCall(virtualInvokeExpr);
					
					if(calledMethod == null)
						continue;
					*/
					
					Body b = makePatchProbe(pc, body, stmt, (Stmt)pc.getSuccOf(stmt), lhs, virtualInvokeExpr);
					
					if(b == null)
						continue;
					
					
				}
			}
			
			if(stmt instanceof InvokeStmt)
			{
				InvokeExpr invokeExpr = stmt.getInvokeExpr();
				
				if(invokeExpr instanceof VirtualInvokeExpr)
				{
					VirtualInvokeExpr virtualInvokeExpr = (VirtualInvokeExpr) invokeExpr;
					
					Body b = makePatchProbe(pc, body, stmt, (Stmt)pc.getSuccOf(stmt), null, virtualInvokeExpr);
					
					if(b == null)
						continue;
					
					
				}
			}
		}
	}
	
	
	
}
