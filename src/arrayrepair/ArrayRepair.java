
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import profile.InstrumManager;
import profile.UtilInstrum;
import boundedAnalysis.ForwardAnalysis;
import soot.ArrayType;
import soot.Body;
import soot.BodyTransformer;
import soot.IntType;
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
import soot.javaToJimple.LocalGenerator;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.Expr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.LengthExpr;
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
        
        Scene.v().addBasicClass("arrayrepair.IndexRepair",SootClass.SIGNATURES);      
        
        soot.Main.main(className);
	}
	
	/*
	 * Returns an object array
	 * Object[0] -> probe
	 * Object[1] -> thrwCls
	 * Object[2] -> sCatch
	 */
	
	private Object[] newArrayPatchProbe( Body jbody, Value lhs, NewArrayExpr newArrayExpr)
	{
		List<Stmt> probe = new ArrayList<>();
		
		SootClass thrwCls = Scene.v().getSootClass("java.lang.NegativeArraySizeException");
		Double d = Math.ceil(Math.random()*100000000);
   	 	Local lException1 = UtilInstrum.getCreateLocal(jbody, 
   	 			"<ex" + d.toString().replace(".", "") + ">", RefType.v(thrwCls));
   	 	
   	 	Stmt sCatch = Jimple.v().newIdentityStmt(lException1, Jimple.v().newCaughtExceptionRef());
   	 	probe.add(sCatch);
		
   	 	//catch block instrumentation
   	 	//re-initialize the array with size 1
   	 	
   	 	NewArrayExpr nae = Jimple.v().newNewArrayExpr(newArrayExpr.getType(), IntConstant.v(1));
   	 	AssignStmt ast = Jimple.v().newAssignStmt(lhs, nae);
   	 	
   	    probe.add(ast);
   	 	
   	    System.out.println("#newArrayPatchProbe executed");
   	    
		return new Object[]{probe, thrwCls, sCatch};
	}
	
	/*
	 * Returns an object array
	 * Object[0] -> probe
	 * Object[1] -> thrwCls
	 * Object[2] -> sCatch
	 */
	
	private Object[] arrayRefPatchProbe(Body jbody, ValuePair valuePair, ArrayRef arrayRef)
	{
		List<Stmt> probe = new ArrayList<>();
	
		SootClass thrwCls = Scene.v().getSootClass("java.lang.IndexOutOfBoundsException");
		Double d = Math.ceil(Math.random()*100000000);
   	 	Local lException1 = UtilInstrum.getCreateLocal(jbody, 
   	 			"<ex" + d.toString().replace(".", "") + ">", RefType.v(thrwCls));
   	 	
   	 	Stmt sCatch = Jimple.v().newIdentityStmt(lException1, Jimple.v().newCaughtExceptionRef());
   	 	probe.add(sCatch);
		
   	 	//catch block instrumentation
   	 	
   	 	Value index = arrayRef.getIndex();
   	 	Value base = arrayRef.getBase();
   	 	
   	 	SootClass IndexRepairClass = Scene.v().loadClassAndSupport("arrayrepair.IndexRepair");
   	 	
   	 	Local baseLength = new LocalGenerator(jbody).generateLocal(IntType.v());
   	 	LengthExpr lenExpr = Jimple.v().newLengthExpr(base);
   	 	AssignStmt lenAssign = Jimple.v().newAssignStmt(baseLength, lenExpr);
   	 	probe.add(lenAssign);
   	 	
   	 	
   	 	Local indexLoc = new LocalGenerator(jbody).generateLocal(IntType.v());
   	 	AssignStmt indAssign = Jimple.v().newAssignStmt(indexLoc, 
   	 			Jimple.v().newStaticInvokeExpr(IndexRepairClass.getMethodByName("getI").makeRef(), Arrays.asList(new Value[]{index, baseLength})));
   	 	
   	 	probe.add(indAssign);
   	 	
   	 	ArrayRef newArrayRef = Jimple.v().newArrayRef(base, indexLoc);
   	 	
   	 	Local l = new LocalGenerator(jbody).generateLocal(valuePair.value.getType());
   	 	AssignStmt temp = Jimple.v().newAssignStmt(l, valuePair.value);
   	 	probe.add(temp);
   	 	
   	 	AssignStmt arrayRefAssign = null;
   	 	
   	 	if(valuePair.isLeft)
   	 		arrayRefAssign = Jimple.v().newAssignStmt(l, newArrayRef);
   	 	else if(valuePair.isRight)
   	 		arrayRefAssign = Jimple.v().newAssignStmt(newArrayRef, l);
   	 	
   	 	probe.add(arrayRefAssign);
   	 	
		return new Object[]{probe, thrwCls, sCatch};
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Value> Body  makePatchProbe(PatchingChain<Unit> ch,
			Body jbody, Stmt try_start_stmt, Stmt try_end_stmt, ValuePair valuePair, T Expr)
	{
		List<Stmt> probe = new ArrayList<>();
		
		Stmt sGotoLast = Jimple.v().newGotoStmt(try_end_stmt);
    	probe.add(sGotoLast);   	    	
    	
    	
		NewArrayExpr newArrayExpr = null;
		ArrayRef arrayRef = null;
		
		Object[] ret = null;
		if(Expr instanceof NewArrayExpr)
		{
			newArrayExpr = (NewArrayExpr) Expr;
			ret = newArrayPatchProbe(jbody, valuePair.value, newArrayExpr);
			
			probe.addAll((List<Stmt>)ret[0]);
		}
		
		else if(Expr instanceof ArrayRef)
		{
			arrayRef = (ArrayRef) Expr;
			ret = arrayRefPatchProbe(jbody, valuePair, arrayRef);
			
			probe.addAll((List<Stmt>)ret[0]);
		}
		
		else
		{
			return null;
		}
		
		InstrumManager.v().insertRightBeforeNoRedirect(ch, probe, try_end_stmt);
		 //instr
		jbody.getTraps().add(Jimple.v().newTrap((SootClass) ret[1], try_start_stmt, sGotoLast, (Stmt) ret[2]));
		jbody.validate(); 
		
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
					//send the other side
					Body b = makePatchProbe(pc, body, stmt, (Stmt) pc.getSuccOf(stmt), new ValuePair(lhs, false, false), nae);
					
					if(b == null)
						continue;
				}
				
				/*
				 * In case any thing assigned to a array
				 */
				if(lhs instanceof ArrayRef)
				{
					ArrayRef arf = (ArrayRef) lhs;
					Body b = makePatchProbe(pc, body, stmt, (Stmt) pc.getSuccOf(stmt), new ValuePair(rhs, true, false), arf);
					
					if(b == null)
						continue;
				}
				
				/*
				 * In case the array is assigned to something else 
				 */
				if(rhs instanceof ArrayRef)
				{
					ArrayRef arf = (ArrayRef) rhs;
					Body b = makePatchProbe(pc, body, stmt, (Stmt) pc.getSuccOf(stmt), new ValuePair(lhs, false, true), arf);
					
					if(b == null)
						continue;
				}
			}
			
			while(it_box.hasNext())
			{
				Value val = it_box.next().getValue();
				Type valType = val.getType();
				
				if(valType instanceof ArrayType)
				{
					containsArrayType = true;
					break;
				}
			}
			
			if(containsArrayType)
			{
				//System.out.println(unit);
			}
	    
		}
		
	}
	
	

}
