package profile;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.util.Chain;

/** Takes a set of methods and wraps then with a try/catch, with empty catch block, to 
 *  avoid exceptions to escape to callers. */
public class CatchWrapInstrumenter 
{
	public static void main(String []ag)
	{
		SootClass sc=Scene.v().loadClassAndSupport("SampleTest");
		List<SootMethod> sm=sc.getMethods();
		instrument(sm);
	}
	
	
	public static Stmt getFirstNonIdStmt(PatchingChain pchain) {
		Stmt sFirstNonId = null;
		for (Iterator it = pchain.iterator(); it.hasNext(); ) {
			sFirstNonId = (Stmt) it.next();
			if (!(sFirstNonId instanceof IdentityStmt))
				break;
		}
		return sFirstNonId;
	}
	public static Local getLocal(Body b, String localName) {
		// look for existing bs local, and return it if found
		Chain locals = b.getLocals();
		for (Iterator itLoc = locals.iterator(); itLoc.hasNext(); ) {
			Local l = (Local)itLoc.next();
			if (l.getName().equals(localName))
				return l;
		}
		
		return null;
	}
	
	public static Local getCreateLocal(Body b, String localName, Type t) {
		// try getting it
		Local l = getLocal(b, localName);
		if (l != null) {
			assert l.getType().equals(t); // ensure type is correct
			return l;
		}
		// no luck; create it
		Chain locals = b.getLocals();
		l = Jimple.v().newLocal(localName, t);
		locals.add(l);
		return l;
	}
	
	
	public static void instrument(List<SootMethod> mtds) {
		// label0:
		//   ... (method body -- before final return)
		// label1:
		//   goto label3
		// label2:
		//   local1 := @caughtexception;
		//   local1.printStackTrace(System.out);
		// label3:
		//   return  (end of old method body)
		//
		// catch java.lang.Throwable from label0 to label1 with label2;
		
		// get access to Throwable class and toString method
		SootClass thrwCls = Scene.v().getSootClass("java.lang.Throwable");
		SootMethod mPrintStackTrace = thrwCls.getMethod("void printStackTrace(java.io.PrintStream)");
		
		// get access to System.out field
		SootClass clsSystem = Scene.v().getSootClass("java.lang.System");
		SootClass clsPrintStream = Scene.v().getSootClass("java.io.PrintStream");
		Type printStreamType = clsPrintStream.getType();
		SootField fldSysOut = clsSystem.getField("out", printStreamType);
//		SootMethod mPrintln = clsPrintStream.getMethod("void println(java.lang.String)");
		
		for (SootMethod m : mtds) {
			// create probe from label1 to label3 (excluding return)
			List<Stmt> probe = new ArrayList<Stmt>();
			Body b = m.retrieveActiveBody();
			PatchingChain<Unit> pchain = b.getUnits();
			
			Stmt sFirstNonId = getFirstNonIdStmt(pchain);
			Stmt sLast = (Stmt) pchain.getLast();
			
			// Don't instrument empty methods
			if (sFirstNonId == sLast)
				continue;
			
			// FOR NOW, no other returns allowed apart from last stmt
			for (Unit u : pchain)
				assert (!(u instanceof ReturnStmt) && !(u instanceof RetStmt)) || u == sLast;
			
			
			// label1:
			//   goto label3
			Stmt sGotoLast = Jimple.v().newGotoStmt(sLast);
			probe.add(sGotoLast);
			// label2:
			//   local1 := @caughtexception;
			Local lException1 = getCreateLocal(b, "<ex1>", RefType.v(thrwCls));
			Stmt sCatch = Jimple.v().newIdentityStmt(lException1, Jimple.v().newCaughtExceptionRef());
			probe.add(sCatch);
//			//   local2 := local1.toString();
//			Local lThrwMessage = UtilInstrum.getCreateLocal(b, "<ex2>", RefType.v("java.lang.String"));
//			Stmt sThrwToString = Jimple.v().newAssignStmt(lThrwMessage, 
//					Jimple.v().newVirtualInvokeExpr(lException1, mPrintStackTrace.makeRef()));
//			probe.add(sThrwToString);
			   //System.out.println(local2);
			
			  // local1.printStackTrace(System.out);
			Local lSysOut = getCreateLocal(b, "<sysout>", printStreamType);
			Stmt sGetSysOutToLocal = Jimple.v().newAssignStmt(lSysOut, Jimple.v().newStaticFieldRef(fldSysOut.makeRef()));
			probe.add(sGetSysOutToLocal);
			Stmt sCallPrintStackTrace = Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(lException1, mPrintStackTrace.makeRef(),
					lSysOut));
			probe.add(sCallPrintStackTrace);
			
			
		//	InstrumManager.v().insertRightBeforeNoRedirect(pchain, probe, sLast);
			
			// insert trap (catch)
			b.getTraps().add(Jimple.v().newTrap(thrwCls, sFirstNonId, sGotoLast, sCatch));
			
//			// DEBUG
			System.out.println(m.getName() + ":");
			System.out.println("##"+sCallPrintStackTrace);
			System.out.println(pchain);
		}
	}
	

	
}
