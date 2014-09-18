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

package trace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.IntType;
import soot.Local;
import soot.Pack;
import soot.PackManager;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.util.Chain;


public class ProgramTracer extends BodyTransformer
{
	
	public static void main(String[] args) 
	{
        
        String []className = {"StringTest"};
  
        Pack jtp = PackManager.v().getPack("jtp");

        jtp.add(new Transform("jtp.instrumenter", new ProgramTracer()));
        Options.v().setPhaseOption("jb", "use-original-names:true");
     
        /* DEBUG
        Scanner s = new Scanner(System.in);
        String st = s.next();
        */
        String st = "c";
        
        if(st.equalsIgnoreCase("j"))
        	Options.v().set_output_format(Options.output_format_jimple);
        
        if(st.equalsIgnoreCase("c"))
        	Options.v().set_output_format(Options.output_format_class);     
        
        Scene.v().addBasicClass("trace.TraceFlash",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.util.ListIterator",SootClass.SIGNATURES);
        
        
        soot.Main.main(className);
	}
	
	@Override
	protected void internalTransform(Body body, String phaseName, Map options) 
	{
		SootMethod sMethod = body.getMethod();
		
		if(sMethod.getName().startsWith("<"))
			return;
		
		System.out.println("<<<< " + sMethod + " >>>>");
		
		Chain<Local> local_list = body.getLocals();
		System.out.println(local_list);
		
		PatchingChain<Unit> pc = body.getUnits();
		Iterator<Unit> it = pc.snapshotIterator();
		
		while(it.hasNext())
		{
			Unit unit = it.next();
			Stmt stmt = (Stmt) unit;
			
			if(stmt instanceof IdentityStmt)
				continue;
			
			Iterator<ValueBox> usedDefList =  stmt.getDefBoxes().iterator();
			
			List<Value> val_list = new ArrayList<>();
			while(usedDefList.hasNext())
			{
				Value value = usedDefList.next().getValue();
				val_list.add(value);
			}
			
			SootClass traceFlashClass = Scene.v().loadClassAndSupport("trace.TraceFlash");
			
			StaticInvokeExpr stEvr = Jimple.v().newStaticInvokeExpr
					(traceFlashClass.getMethodByName("flashTraceMemory").makeRef(), val_list);
			
			AssignStmt ast = Jimple.v().newAssignStmt(new LocalGenerator(body).generateLocal(IntType.v()), stEvr);
			
			pc.insertAfter(ast, unit);
			
			System.out.println(unit);
			
		}
	}

}
