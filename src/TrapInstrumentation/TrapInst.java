package TrapInstrumentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootResolver;
import soot.Trap;
import soot.Unit;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.internal.IdentityRefBox;
import soot.jimple.internal.JGotoStmt;
import soot.jimple.internal.JTrap;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.internal.JimpleLocalBox;
import soot.util.Chain;

public class TrapInst extends BodyTransformer{

	Chain stmts;
	Chain traps;


    static Body gbody;
    
	@Override
	protected void internalTransform(Body bodyin, String phase, Map map) 
	{
		if(bodyin.getMethod().getSignature().contains("fun2"))
		{
			//catch block - used
			Local catch_exception_Local = Jimple.v().newLocal("$exception_",RefType.v("java.lang.Exception"));
			//finally block - unused
			Local finally_exception_Local = Jimple.v().newLocal("$finally_",RefType.v("java.lang.Throwable"));
			bodyin.getLocals().add(catch_exception_Local);
			bodyin.getLocals().add(finally_exception_Local);
			Chain<Unit> units = bodyin.getUnits();
		
			SootClass s=Scene.v().loadClassAndSupport("TrapInstrumentation.TryCatchExample");
			Scene.v().loadNecessaryClasses();
			SootMethod smethod=s.getMethodByName("fun1");
			SootMethod handdlemethod=s.getMethodByName("handle");	
			JimpleBody body= (JimpleBody) smethod.retrieveActiveBody();
			gbody=handdlemethod.retrieveActiveBody();
			
			ArrayList<Trap> art=new ArrayList<>();	
			Chain stmts = body.getUnits().getNonPatchingChain();
			//Chain stmts1 = bodyin.getUnits().getNonPatchingChain();
		
			Chain hstmts = gbody.getUnits().getNonPatchingChain();
			Iterator<Unit> hit = hstmts.snapshotIterator();
		
			Unit handle = null;
			JGotoStmt st = null;
	
			Chain<Trap> ctrap=body.getTraps();
	
			Iterator ita=stmts.snapshotIterator();
		
			/*while(ita.hasNext())
			{
			System.out.println(ita.next());
			}
			System.out.println("================================");
			 */
			Iterator<Trap> it = ctrap.snapshotIterator();
			while(it.hasNext())
			{
				Trap trap = it.next();
				art.add(trap);
				System.out.println(trap);
				//System.out.println(trap+"\n--------------------");
				Unit beginUnit = trap.getBeginUnit();
				Unit endUnit = trap.getEndUnit();
				//System.out.println(endUnit);
			}
			System.out.println("--------------------------------------");
		
			Iterator<Unit> i = bodyin.getUnits().snapshotIterator();
		
			/*while(i.hasNext())
			{
				System.out.println(i.next());
			}*/
			int counter = 0;
			int fcounter =0;
			Trap t=art.get(0);
			
			Unit afterin=null;
			Unit startu=null,endu=null;
			//JTrap t = new JTrap(exception, beginStmt, endStmt, handlerStmt)
			
			JGotoStmt gt=null;
			while (i.hasNext()) 
			{
				counter++;
				//System.out.println(counter);
				Unit u = i.next();
				System.out.println(counter+" "+u);
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
						startu = u;
						//System.out.println(t+"\n...........................");
					}
				}
				if(counter == fcounter + 1)
				{
					endu = u;
					t.setEndUnit(u);
					t.setException(Scene.v().loadClassAndSupport("java.lang.RuntimeException"));
					Scene.v().loadNecessaryClasses();
					//t.setHandlerUnit(handle);			
				}
				if(counter == fcounter + 1)
				{
					//System.out.println("### u "+u);
					gt = new JGotoStmt(u);
					//System.out.println(gt);
				}

			}
			System.out.println("@@ gt = "+gt);
			System.out.println("@@ afterin = "+afterin);
		
			units.insertAfter(gt, afterin);
		
			System.out.println("*************************************************");
			System.out.println(t);
			System.out.println("*************************************************");
		
			CaughtExceptionRef jcaught  = Jimple.v().newCaughtExceptionRef();
			//System.out.println(jcaught);

			//JimpleLocal jl = new JimpleLocal("$q1", RefType.v("java.lang.Integer"));
			//Local fieldLocal = Jimple.v().newLocal("$exception_"+bodyin.getMethod().getName(), RefType.v());
			//JimpleLocalBox jlbox = new JimpleLocalBox(fieldLocal);
			//IdentityRefBox rf = new IdentityRefBox(jcaught);
			//System.out.println(jlbox);
			IdentityStmt is = Jimple.v().newIdentityStmt(catch_exception_Local, jcaught);
			//System.out.println("#####"+is);
			t.setHandlerUnit(is);
			
			SootClass exceptioncalsss =Scene.v().loadClassAndSupport("java.lang.Exception");
			Scene.v().loadNecessaryClasses();
			JTrap inst_trap =new JTrap(exceptioncalsss, startu, endu, is);
			
			System.out.println("++++++++++++++++++++++++++++++++");
			System.out.println(inst_trap);
			System.out.println("++++++++++++++++++++++++++++++++");
			
			
			units.insertAfter(is, gt);	
			units.insertBefore(Jimple.v().newNopStmt(), is);
			//----------------------------------------------------------------------------------
			Iterator itl = units.iterator();
			int counter1=0;
			while(itl.hasNext())
			{
				counter++;
				System.out.println(counter + " "+itl.next());
			}
			//----------------------------------------------------------------------------------
			System.out.println(bodyin);
			bodyin.validate();
		}
		//System.out.println(bodyin);
	}

}
