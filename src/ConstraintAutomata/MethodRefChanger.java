package ConstraintAutomata;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Pack;
import soot.PackManager;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.SootResolver;
import soot.Transform;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import soot.options.Options;

public class MethodRefChanger extends BodyTransformer
{	
	
	@Override
	protected void internalTransform(Body body, String phaseName, Map options)
	{		
		SootMethod sMethod = body.getMethod();
		System.out.println("<<current method : " + sMethod.getName() + " >>");
		
		PatchingChain<Unit> pc = body.getUnits();
		
		Iterator<Unit> it = pc.snapshotIterator();
		//System.out.println(body);
		
		while(it.hasNext())
		{
			Unit unit = it.next();
			Stmt stmt = (Stmt) unit;
			
			boolean flag = false;
			/*
			only look at the function call which may happen as Invoke statement
			of which may be at the left hand side of an assignment statement
			flag = true if it has method call. I also ignore any special Invoke
			*/
			
			if(stmt instanceof InvokeStmt)
			{
				InvokeExpr invokeExpr = stmt.getInvokeExpr();
				
				if(invokeExpr instanceof SpecialInvokeExpr)	
				{
					continue;
				}
				
				if(invokeExpr instanceof VirtualInvokeExpr)
				{
					System.out.println(stmt);
					flag = true;
					
					VirtualInvokeExpr vEx = (VirtualInvokeExpr) invokeExpr;
					List<Value> args = vEx.getArgs();
					SootMethodRef calledMethodRef = vEx.getMethodRef();
					
					
					//delete old
					
					pc.remove(unit);
				}
			}
			
			if(stmt instanceof AssignStmt)
			{
				AssignStmt astmt = (AssignStmt) stmt;
				Value value = astmt.getRightOp();
				
				if(value instanceof VirtualInvokeExpr)
					System.out.println(stmt);
			}
			
		}
		
		//System.out.println(Constants.patchedMethodMap.entrySet());

	}
	
}

class Main_refChanger
{
	public static void main(String[] args) 
	{	            
        String []className = {"Test_Caller"};
        
        /* add a phase to transformer pack by call Pack.add */
        Pack jtp = PackManager.v().getPack("jtp");

        jtp.add(new Transform("jtp.instrumenter", new MethodRefChanger()));
        Options.v().setPhaseOption("jb", "use-original-names:true");
        
        /*
        Options.v().set_whole_program(true);
        Options.v().set_main_class(className[0]);
        */
        SootResolver.v().resolveClass("TestPack.Test2", SootClass.SIGNATURES);
        SootResolver.v().resolveClass("java.util.Comparator", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.StringBuilder",SootClass.SIGNATURES);
        
        
        // load the patched methods
        
        PatchedMethodLoader pml = new PatchedMethodLoader(Constants.methodListSileName);
        
        soot.Main.main(className);   
	}
}
