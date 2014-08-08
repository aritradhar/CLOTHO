package TestPack;

import java.util.Iterator;
import java.util.List;

import polyglot.ast.Stmt;
import soot.IdentityUnit;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.baf.internal.AbstractBranchInst;
import soot.jimple.AssignStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.internal.JGotoStmt;
import soot.util.Chain;


public class TryCatchAnalysis 
{
	public static void main(String []a) 
	{
		
		SootClass sclass=Scene.v().loadClassAndSupport("TryCatchExample");
		//SootClass sclass=Scene.v().loadClassAndSupport("Test_AnalyzeStmt");
		Scene.v().loadNecessaryClasses();
		//Scene.v().loadDynamicClasses();
		List<SootMethod> sm=sclass.getMethods();
		SootMethod smethod=sclass.getMethodByName("main");
		
		List<SootClass> exp_list=smethod.getExceptions();
		System.out.println("Exception : "+exp_list);
		//Body body=smethod.retrieveActiveBody();
		JimpleBody jbody= (JimpleBody) smethod.retrieveActiveBody();
		Chain<Trap> ctrap=jbody.getTraps();
		
		
		System.out.println(ctrap.size());
		System.out.println("@@@Trap"+ctrap);
		
		System.out.println(jbody);
		
		Chain<Unit> ch=jbody.getUnits();
		Iterator<Unit> it=ch.snapshotIterator();
		CaughtExceptionRef stmt= Jimple.v().newCaughtExceptionRef();
		//System.out.println(stmt);
		Chain<Local> ch1=jbody.getLocals();
		
		
		System.out.println("---------------------------------------------");
	    //System.out.println(ch1);
		
		while(it.hasNext())
		{
			Unit unit=it.next();
			System.out.println(unit);
			List vb = unit.getUseAndDefBoxes();
			Iterator itvb = vb.iterator();
			/*
			while(itvb.hasNext())
			{
				System.out.println(itvb.next());
			}
			if((unit instanceof AssignStmt))
			{
				//if(unit.branches())
				//System.out.println("####  "+unit);
			}
			if(unit instanceof ReturnVoidStmt) 
			{
				
				//System.out.println(unit.toString());
			}*/
			List<UnitBox> unitbox=unit.getUnitBoxes();
		}
		
	}
}
