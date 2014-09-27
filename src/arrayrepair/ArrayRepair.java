
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

package arrayrepair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import profile.UtilInstrum;
import boundedAnalysis.ForwardAnalysis;
import soot.ArrayType;
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
import soot.Transform;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.Expr;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.NewArrayExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.options.Options;
import stringrepair.StringRepair;

public class ArrayRepair extends BodyTransformer 
{
	public static void main(String[] args) 
	{
        
        String []className = {"Test1"};
  
        Pack jtp = PackManager.v().getPack("jtp");

        jtp.add(new Transform("jtp.instrumenter", new ArrayRepair()));
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
        
        soot.Main.main(className);
	}
	
	
	private List<Stmt> newArrayPatchProbe( Body jbody, Value lhs, NewArrayExpr newArrayExpr)
	{
		List<Stmt> probe = new ArrayList<>();
		
		SootClass thrwCls = Scene.v().getSootClass("java.lang.IndexOutOfBoundsException");
		Double d = Math.ceil(Math.random()*100000000);
   	 	Local lException1 = UtilInstrum.getCreateLocal(jbody, 
   	 			"<ex" + d.toString().replace(".", "") + ">", RefType.v(thrwCls));
   	 	
   	 	Stmt sCatch = Jimple.v().newIdentityStmt(lException1, Jimple.v().newCaughtExceptionRef());
   	 	probe.add(sCatch);
		
   	 	
   	 	
		return probe;
	}
	
	private <T extends Expr, AnyNewExpr> Body  makePatchProbe(PatchingChain<Unit> ch ,
			Body jbody, Stmt try_start_stmt, Stmt try_end_stmt, Value lhs, T Expr)
	{
		List<Stmt> probe = new ArrayList<>();
		
		NewArrayExpr newArrayExpr = null;
		
		if(Expr instanceof NewArrayExpr)
		{
			newArrayExpr = (NewArrayExpr) Expr;
			probe.addAll(newArrayPatchProbe(jbody, lhs, newArrayExpr));
		}
		
		
		
		return jbody;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalTransform(Body body, String phase, Map map) 
	{
		SootMethod sMethod = body.getMethod();		
        
		/*
		 *Constructors are also patched here
		 * 
		if(sMethod.getName().startsWith("<"))
			return;
		*/
		
		System.out.println("---- Current Method : " + sMethod.getName() + " ----");
		
		//see forward flow analysis and then look for the variables
        
		PatchingChain<Unit> pc= body.getUnits();
		
		Iterator<Unit> it = pc.snapshotIterator();
		
		while(it.hasNext())
		{
			Unit unit = it.next();
			Stmt stmt = (Stmt) unit;
			
			List<ValueBox> useDefBox = stmt.getUseAndDefBoxes();
			Iterator<ValueBox> it_box = useDefBox.iterator();
			
			boolean containsArrayType =false;
			
			if(stmt instanceof AssignStmt)
			{
				AssignStmt ast = (AssignStmt) stmt;
				Value rhs = ast.getRightOp();
				Value lhs = ast.getLeftOp();
				if(rhs instanceof NewArrayExpr)
				{
					NewArrayExpr nae = (NewArrayExpr) rhs;
					Body b = makePatchProbe(pc, body, stmt, (Stmt) pc.getSuccOf(stmt), lhs, nae);
					
					if(b == null)
						continue;
				}
			}
			
			while(it_box.hasNext())
			{
				Value val = it_box.next().getValue();
				Type valType = val.getType();
				
				if(valType instanceof ArrayType)
					containsArrayType = true;
			}
			
			if(containsArrayType)
			{
				
			}
	    
		}
		
	}
	
	

}
