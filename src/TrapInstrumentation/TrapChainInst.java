package TrapInstrumentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import polyglot.ast.Instanceof;
import soot.ArrayType;
import soot.Body;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.JastAddJ.ReturnStmt;
import soot.jbco.jimpleTransformations.GotoInstrumenter;
import soot.jimple.AssignStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NopStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.IdentityRefBox;
import soot.jimple.internal.JGotoStmt;
import soot.jimple.internal.JimpleLocalBox;
import soot.util.Chain;
import soot.util.Switch;

class TrapChainInst
{
	Chain stmts;
	Chain traps;

	Map beginUnitToTraps = new HashMap();
	Map endUnitToTraps = new HashMap();
    static Body gbody;
	
	public static void main(String []a)
	{
		SootClass s=Scene.v().loadClassAndSupport("TrapInstrumentation.TryCatchExample");
		SootClass s1=Scene.v().loadClassAndSupport("TrapInstrumentation.Instrumented");
		Scene.v().loadNecessaryClasses();
		SootMethod smethod=s.getMethodByName("fun1");
		SootMethod handdlemethod=s.getMethodByName("handle");
		
		JimpleBody jbody= (JimpleBody) smethod.retrieveActiveBody();
		gbody=handdlemethod.retrieveActiveBody();
		
		Scene.v().loadNecessaryClasses();
		SootMethod smethod2=s1.getMethodByName("fun2");
		JimpleBody jbody1= (JimpleBody) smethod2.retrieveActiveBody();
		
		new TrapChainInst().wrapTraps(jbody, jbody1);
		//System.out.println(jbody);
		
	}
	public NopStmt insertNopStmt(Body body, Unit u)
	{
		NopStmt nop = Jimple.v().newNopStmt();
		body.getUnits().insertAfter(nop, u);
		return nop;
	}
	
	void wrapTraps(Body body, Body bodyin)
	{
		Chain<Unit> units = bodyin.getUnits();
		
		ArrayList<Trap> art=new ArrayList<>();
		
		Chain stmts = body.getUnits().getNonPatchingChain();
		//Chain stmts1 = bodyin.getUnits().getNonPatchingChain();
		
		
		Chain hstmts = gbody.getUnits().getNonPatchingChain();
		Iterator<Unit> hit = hstmts.snapshotIterator();
		
		Unit handle = null;
		JGotoStmt st = null;

		
		Chain<Trap> ctrap=body.getTraps();
	
		Iterator ita=stmts.snapshotIterator();
		while(ita.hasNext())
		{
			System.out.println(ita.next());
		}
		System.out.println("================================");
		Iterator<Trap> it = ctrap.snapshotIterator();
		while(it.hasNext())
		{
			Trap trap = it.next();
			art.add(trap);
			System.out.println(trap);
			//System.out.println(trap+"\n--------------------");
			Unit beginUnit = trap.getBeginUnit();
			Unit endUnit = trap.getEndUnit();
			//trap.
			//System.out.println(endUnit);
		}
		System.out.println("--------------------------------------");
		
		Iterator<Unit> i = bodyin.getUnits().snapshotIterator();
		int counter = 0;
		int fcounter =0;
		Trap t=art.get(0);
		Unit afterin=null;
		
		JGotoStmt gt=null;
		while (i.hasNext()) 
		{
		
			counter++;
			Unit u = i.next();
			System.out.println(u);
			//System.out.println(u);

			List vb = u.getUseAndDefBoxes();
			//System.out.println(u.getUseAndDefBoxes());
			Iterator boxit = vb.iterator();
			while(boxit.hasNext())
			{
				String st1 = boxit.next().toString();
				if(st1.contains("/"))
				{
					afterin = u;
					fcounter=counter;					
					t.setBeginUnit(u);
					//System.out.println(t+"\n...........................");
				}
			}
			if(counter == fcounter + 1)
			{
				t.setEndUnit(u);
				t.setException(Scene.v().loadClassAndSupport("java.lang.RuntimeException"));
				//t.setHandlerUnit(handle);
				
			}
			if(counter == fcounter + 1)
			{
				gt = new JGotoStmt(u);
				//System.out.println(gt);
			}
			//if(counter == )
			
			
			//if(counter == 2)
		}
		//System.out.println("@@ gt = "+gt);
		//System.out.println("@@ afterin = "+afterin);
		units.insertAfter(gt, afterin);
				
		
		//System.out.println(stmts);
		//System.out.println(ctrap);
		//JCaughtExceptionRef jcaught = new JCaughtExceptionRef();
		//System.out.println(jcaught);
		CaughtExceptionRef jcaught  = Jimple.v().newCaughtExceptionRef();
		//System.out.println(jcaught);

		//JimpleLocal jl = new JimpleLocal("$q1", int);
		Local fieldLocal = Jimple.v().newLocal("$exception_"+body.getMethod().getName(), RefType.v());
		JimpleLocalBox jlbox = new JimpleLocalBox(fieldLocal);
		IdentityRefBox rf = new IdentityRefBox(jcaught);
		//System.out.println(jlbox);
		IdentityStmt is = Jimple.v().newIdentityStmt(fieldLocal, jcaught);
		t.setHandlerUnit(is);
		units.insertAfter(is, gt);
		
		System.out.println("*************************************************");
		System.out.println(t);
		System.out.println("*************************************************");
		
		//----------------------------------------------------------------------------------
		Iterator itl = units.snapshotIterator();
		while(itl.hasNext())
			System.out.println(itl.next());
		//----------------------------------------------------------------------------------
		//System.out.println("$$$$  "+is);
		/*
		Iterator<Trap> it = ctrap.iterator();
		while(it.hasNext())
		{
			Trap trap = it.next();
			System.out.println(trap+"\n--------------------");
			Unit beginUnit = trap.getBeginUnit();
			Unit endUnit = trap.getEndUnit();
			
			//System.out.println(endUnit);
		}*/
		

	}
}