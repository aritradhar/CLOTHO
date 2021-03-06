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









import callGraphTrace.TrapFindType;
import constraintAnalysis.ConstraintStorageDataType;
import constraintAnalysis.ConstraintStorageMap;
import constraintAnalysis.DynamicIfStmtInfo;
import constraintAnalysis.GenerateString;
import constraintAnalysis.LastCheckAnalysis;
import constraintAnalysis.MethodBond;
import constraintAnalysis.OptimizationExlude;
import constraintAnalysis.OptimizationPayloadCheck;
import constraintAnalysis.safeUnit.SafeUnitEvaluator;
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
import soot.SootMethodRef;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.AssignStmt;
import soot.jimple.DivExpr;
import soot.jimple.Expr;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.infoflow.taint.SourceSinkResolver;
import stringrepair.*;
import util.ENV;

public class StringRepairConstraintDynamic extends BodyTransformer
{
	SourceSinkResolver ssr;
	 
	public StringRepairConstraintDynamic()
	{
		 
	}
	public StringRepairConstraintDynamic(SourceSinkResolver ssr)
	{
		this.ssr = ssr;
	}
	
	public static <T extends Expr> List<Stmt> divPatchProbe(PatchingChain<Unit> ch ,
			Body jbody, Stmt try_start_stmt, Stmt try_end_stmt, Value lhs, T Expr)
	{
		List<Stmt> probe = new ArrayList<Stmt>();
		
		Value dom = null, nom = null;
		DivExpr divExpr = null;
		
		if(Expr instanceof DivExpr)
		{
			divExpr = (DivExpr) Expr;
			nom = divExpr.getOp1();
			dom = divExpr.getOp2();
		}
		
		SootClass thrwCls = Scene.v().getSootClass("java.lang.ArithmeticException");
		
		Stmt sGotoLast = Jimple.v().newGotoStmt(try_end_stmt);
   	 	probe.add(sGotoLast);
   	 
   	 	//prepare for catch block
   	 	Double d = Math.ceil(Math.random()*100000000);
   	 	Local lException1 = UtilInstrum.getCreateLocal(jbody, "<ex" + d.toString().replace(".", "") + ">", RefType.v(thrwCls));
   	 	Stmt sCatch = Jimple.v().newIdentityStmt(lException1, Jimple.v().newCaughtExceptionRef());
   	 	probe.add(sCatch);
   	 	
   	 	//catch block
   	 	
   	 	Local newDom = new LocalGenerator(jbody).generateLocal(dom.getType());
	 	
   	 	AssignStmt oneAssign = Jimple.v().newAssignStmt(newDom, IntConstant.v(1));
   	 	probe.add(oneAssign);
   	 	
   	 	Local newNom = new LocalGenerator(jbody).generateLocal(nom.getType());
	 	AssignStmt t1 = Jimple.v().newAssignStmt(newNom, nom);
	 	probe.add(t1);
   	 	
   	 	DivExpr div = Jimple.v().newDivExpr(newNom, newDom);
   	 	
   	 	if(lhs != null)
   	 	{
   	 		AssignStmt lhsAssign = Jimple.v().newAssignStmt(lhs, div);
   	 		probe.add(lhsAssign);
   	 	}
   	 	
		InstrumManager.v().insertRightBeforeNoRedirect(ch, probe, try_end_stmt);
		//instr
		jbody.getTraps().add(Jimple.v().newTrap(thrwCls, try_start_stmt, sGotoLast, sCatch));
    	jbody.validate(); 
    	 
    	 
		return probe;
	}
	
	private <T extends InvokeExpr> List<Stmt> constraintCheckPatchProbe(Body jbody, Value lhs, T InvokeExpr, PatchingChain<Unit> pc, Stmt st)
	{
		List<Stmt> probe = new ArrayList<Stmt>();
		
		VirtualInvokeExpr virtualInvokeExpr = (InvokeExpr instanceof VirtualInvokeExpr) ? (VirtualInvokeExpr)InvokeExpr : null;
		//StaticInvokeExpr staticInvokeExpr = (InvokeExpr instanceof StaticInvokeExpr) ? (StaticInvokeExpr)InvokeExpr : null;
		
		Type stringType = RefType.v("java.lang.String");
		Type charSequenceType = RefType.v("java.lang.CharSequence");
		
		if(lhs == null)
			return new ArrayList<Stmt>();;
		
		if(InvokeExpr instanceof VirtualInvokeExpr && lhs.getType() == stringType)
		{
			String methodSignature = jbody.getMethod().getSignature();
				
			Value baseCheck = virtualInvokeExpr.getBase();
			
			/*if(ENV.OPTOMIZATION_SUBSEQUENT_PATCH_NON_USE_SKIP)
			{
				System.out.println("#### OPTIMIZATION EXCLUDED" + st);
				if(OptimizationExlude.doOptimize(pc, st, baseCheck))
					return probe;
			}*/
			
			OptimizationExlude.setMap(pc, st, baseCheck);
			
			HashMap<Value, ConstraintStorageDataType> CSDTmap = ConstraintStorageMap.constraintStorageMap.get(methodSignature);
			ConstraintStorageDataType CSDT = ConstraintStorageMap.CSDTget(lhs, CSDTmap);
			
			/*
			 * No constraint information
			 * Fall back to index patching
			 */
			if(CSDT == null)
			{
				probe.addAll(StringRepair.subStringPatchProbe(jbody, lhs, virtualInvokeExpr));
				ENV.STAT_REPAIR_COUNT++;
				return probe;
			}
			
			String generatedString = GenerateString.init(methodSignature, lhs, CSDT);
						
			AssignStmt patchAssign = Jimple.v().newAssignStmt(lhs, StringConstant.v(generatedString));
			
			/*
			 * Set that the string encounters a exception
			 */
			SootClass encounterRepairClass = Scene.v().loadClass("constraintAnalysis.stringRepair.EncounterRepair", SootClass.SIGNATURES);
			SootMethodRef encounterRepairSetRef = encounterRepairClass.getMethodByName("encounterRepairSet").makeRef();
			
			StaticInvokeExpr encounterException = Jimple.v().newStaticInvokeExpr(encounterRepairSetRef,
					Arrays.asList( new Value[]{(Value) StringConstant.v(methodSignature), lhs}));
			
			InvokeStmt encounterExceptionStmt = Jimple.v().newInvokeStmt(encounterException);
					
			probe.add(patchAssign);
			probe.add(encounterExceptionStmt);
			
			ENV.STAT_REPAIR_COUNT++;
		
		}
		
		if(InvokeExpr instanceof VirtualInvokeExpr && lhs.getType() == charSequenceType)
		{
			String methodSignature = jbody.getMethod().getSignature();
			
			HashMap<Value, ConstraintStorageDataType> CSDTmap = ConstraintStorageMap.constraintStorageMap.get(methodSignature);
			ConstraintStorageDataType CSDT = ConstraintStorageMap.CSDTget(lhs, CSDTmap);
			
			/*
			 * No constraint information
			 * Fall back to index patching
			 */
			if(CSDT == null)
			{				
				probe.addAll(StringRepair.subSequencePatchProbe(jbody, lhs, virtualInvokeExpr));
				ENV.STAT_REPAIR_COUNT++;
				return probe;
			}
			
			String generatedString = GenerateString.init(methodSignature, lhs, CSDT);
						
			AssignStmt patchAssign = Jimple.v().newAssignStmt(lhs, StringConstant.v(generatedString));
			
			probe.add(patchAssign);
			
			ENV.STAT_REPAIR_COUNT++;
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
		 VirtualInvokeExpr virtualInvokeExpr = (InvokeExpr instanceof VirtualInvokeExpr) ? (VirtualInvokeExpr)InvokeExpr : null;

		 StaticInvokeExpr staticInvokeExpr = (InvokeExpr instanceof StaticInvokeExpr) ? (StaticInvokeExpr)InvokeExpr : null;
		 
		 SpecialInvokeExpr specialInvokeExpr = (InvokeExpr instanceof SpecialInvokeExpr) ? (SpecialInvokeExpr)InvokeExpr : null;
		
		 int containsAPIcall = 1;
		 //Debug
		 //System.out.println(sMethod);
		 
		 /*
		  * return type String
		  */
		 if(sMethod.getSubSignature().equals("java.lang.String substring(int)")
				 || sMethod.getSubSignature().equals("java.lang.String substring(int,int)"))
		 {
			 thrwCls = Scene.v().getSootClass("java.lang.StringIndexOutOfBoundsException");
			 containsAPIcall = 1;
		 }
		 
		 /*
		  * Return type CharSequence
		  */
		 else if(sMethod.getSubSignature().equals("java.lang.CharSequence subSequence(int,int)")
				 || sMethod.getSubSignature().equals("java.lang.String valueOf(char[],int,int)"))
		 {
			 thrwCls = Scene.v().getSootClass("java.lang.IndexOutOfBoundsException");
			 containsAPIcall = 2;
		 }
		 
		 /*
		  * return type character
		  */
		 else if(sMethod.getSubSignature().equals("char charAt(int)"))
		 {
			 thrwCls = Scene.v().getSootClass("java.lang.IndexOutOfBoundsException");
			 containsAPIcall = 3;
		 }
		 
		 /*
		  * return type integer
		  */
		 else if(sMethod.getSubSignature().equals("int codePointAt(int)") 
				 || sMethod.getSubSignature().equals("int codePointBefore(int)") 
				 || sMethod.getSubSignature().equals("int codePointCount(int,int)")
				 || sMethod.getSubSignature().equals("int offsetByCodePoints(int,int)") )
		 {
			 thrwCls = Scene.v().getSootClass("java.lang.IndexOutOfBoundsException");
			 containsAPIcall = 4;
		 }
		 
		 /*
		  * return type void
		  */
		 else if(sMethod.getSubSignature().equals("void getChars(int,int,char[],int)")
				 || sMethod.getSubSignature().equals("void getBytes(int,int,byte[],int)"))
		 {
			 thrwCls = Scene.v().getSootClass("java.lang.IndexOutOfBoundsException");
			 containsAPIcall = 5;
		 }
		 
		 else if(sMethod.getSignature().equals("<java.lang.String: void <init>(char[],int,int)>"))
		 {
			 thrwCls = Scene.v().getSootClass("java.lang.IndexOutOfBoundsException");
			 containsAPIcall = 6;
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
    	 if(containsAPIcall == 1 || containsAPIcall == 2)
    	 {
    		 List<Stmt> patchProbe = constraintCheckPatchProbe(jbody, lhs, InvokeExpr, ch, try_start_stmt);
    		 
    		 /*
    		  * Optimization may return null here
    		  */
    		 if(patchProbe != null)
    			 probe.addAll(patchProbe);
    	 }
    	 
    	 else if(sMethod.getSubSignature().equals("char charAt(int)"))
    	 {
    		 probe.addAll(stringrepair.StringRepair.charAtPatchProbe(jbody, lhs, virtualInvokeExpr));
    		 ENV.STAT_REPAIR_COUNT++;
    	 }
		 
		 else if(sMethod.getSubSignature().equals("int codePointAt(int)"))
    	 {
    		 probe.addAll(stringrepair.StringRepair.codePointAtPatchProbe(jbody, lhs, virtualInvokeExpr));
    		 ENV.STAT_REPAIR_COUNT++;
    	 }
		 
		 else if(sMethod.getSubSignature().equals("int codePointBefore(int)"))
    	 {
    		 probe.addAll(stringrepair.StringRepair.codePointBeforePatchProbe(jbody, lhs, virtualInvokeExpr));
    		 ENV.STAT_REPAIR_COUNT++;
    	 }
    	 
		 else if(sMethod.getSubSignature().equals("int codePointCount(int,int)"))
    	 {
    		 probe.addAll(stringrepair.StringRepair.codePointCountPatchProbe(jbody, lhs, virtualInvokeExpr));
    		 ENV.STAT_REPAIR_COUNT++;
    	 }
    	 
		 else if(sMethod.getSubSignature().equals("int offsetByCodePoints(int,int)"))
    	 {
    		 probe.addAll(stringrepair.StringRepair.codePointCountPatchProbe(jbody, lhs, virtualInvokeExpr));
    		 ENV.STAT_REPAIR_COUNT++;
    	 }
    	 
		 else if(sMethod.getSubSignature().equals("java.lang.String valueOf(char[],int,int)"))
    	 {
    		 probe.addAll(stringrepair.StringRepair.valueOfPatchProbe(jbody, lhs, staticInvokeExpr));
    		 ENV.STAT_REPAIR_COUNT++;
    	 }
    	 
		 else if(sMethod.getSignature().equals("<java.lang.String: void <init>(char[],int,int)>"))
		 {
			 Value base_specialInvokeExpr = specialInvokeExpr.getBase();
			 System.out.println("&&&& " + specialInvokeExpr);
			 probe.addAll(stringrepair.StringRepair.stringConstructorPatchProbe(jbody, lhs, base_specialInvokeExpr, specialInvokeExpr));
			 ENV.STAT_REPAIR_COUNT++;
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
		/*
		 * Make sure no false popagation
		 */
		OptimizationExlude.optMap.clear();
		
		SootMethod sMethod = body.getMethod();		
        
		if(sMethod.getName().startsWith("<") && ENV.IGNORE_CONSTRUCTOR)
			return;
		
		System.out.println("---- Current Method : " + sMethod.getSubSignature() + " ----");
        
        String methodSignature = sMethod.getSignature();
		
        
		PatchingChain<Unit> pc= body.getUnits();		
		Iterator<Unit> it = pc.snapshotIterator();
		
		SafeUnitEvaluator SUI = null;
		if(ENV.TAINT_ANALYSIS_ENABLE)
		{
			SUI = new SafeUnitEvaluator(ssr, pc, sMethod);
		}
		//System.out.println(this.ssr.toStringMethodToChainMap());
		
		//int counter = 0;
		
		while(it.hasNext())
		{
			Unit unit = it.next();
			Stmt stmt = (Stmt) unit;			
			
			//System.out.println(stmt);
				
			//check for safe
		    if(ENV.TAINT_ANALYSIS_ENABLE)
		    {
		        System.out.println(SUI.isSafe(unit) +"  "+ unit);
		    	if(!SUI.isSafe(unit))
		    	{
		    		ENV.TOTAL_UNSAFE++;
		    		//System.out.println("Unsafe");
		    		continue;
		    	}
		    	else
		    	{
		    		ENV.TOTAL_SAFE++;
		    	}
		    }
		    if(ENV.CALL_CHAIN_LOOK_UP_FOR_EXCEPTION_HANDLER)
		    {
		    	if(TrapFindType.unitTrapMap.containsKey(methodSignature))
		    	{
		    		HashMap<String, Trap> hm = TrapFindType.unitTrapMap.get(methodSignature);
		    		if(hm.containsKey(unit.toString()))
		    		{
		    			/*
		    			 * There is a trap object inside the methdos
		    			 * or in its ancestor
		    			 */
		    			if(hm.get(unit.toString()) != null)
		    			{
		    				continue;
		    			}
		    		}
		    	}
		    }
			/*
			if(!this.ssr.isSafe(unit, sMethod, counter++))
					continue;
			*/
			
			if(stmt instanceof AssignStmt)
			{
				AssignStmt ast = (AssignStmt) stmt;
				
				Value lhs = ast.getLeftOp();
				Value rhs = ast.getRightOp();
				
				if(ENV.OPTOMIZATION_SUBSEQUENT_PATCH_NON_USE_SKIP)
				{
					LastCheckAnalysis.setUsed(lhs);
				}
				
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
							DynamicIfStmtInfo.init(ifStmt, ret, methodSignature, unit, body);
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
				
				if(rhs instanceof SpecialInvokeExpr)
				{
					SpecialInvokeExpr specialInvokeExppr = (SpecialInvokeExpr) rhs;
					//System.out.println("### # " + specialInvokeExppr);
				}
			}
			
			if(stmt instanceof InvokeStmt)
			{
				InvokeExpr invokeExpr = stmt.getInvokeExpr();
				
				String invokedMethodSig = invokeExpr.getMethod().getSignature();
				List<Value> args = invokeExpr.getArgs();
				
				
				if(invokeExpr instanceof VirtualInvokeExpr)
				{
					//System.out.println(stmt);
					
					VirtualInvokeExpr virtualInvokeExpr = (VirtualInvokeExpr) invokeExpr;
					
					Value base = virtualInvokeExpr.getBase();
					if(ENV.OPTOMIZATION_SUBSEQUENT_PATCH_NON_USE_SKIP)
					{
						MethodBond MB = new MethodBond(base, methodSignature, invokedMethodSig);
						//start with false
						OptimizationPayloadCheck OPC = new OptimizationPayloadCheck(args, false);
						LastCheckAnalysis.setInWatchDog(MB, OPC);
						
						if(LastCheckAnalysis.triggerSubsqOptimization(MB))
							continue;
						
					}
					
					
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
				
				/*
				 * For string constructor
				 * public String(char[] value, int offset,int count)
				 * requires a special invoke call analysis
				 */
				
				if(invokeExpr instanceof SpecialInvokeExpr)
				{
					SpecialInvokeExpr specialInvokeExppr = (SpecialInvokeExpr) invokeExpr;
					Body b = makePatchProbe(pc, body, stmt, (Stmt)pc.getSuccOf(stmt), null, specialInvokeExppr);
					if(b == null)
						continue;	
					//System.out.println("###  " + specialInvokeExppr);
				}
			}
		}
		it  = null;
		
		/*
		 * DEBUG
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
			
			if(stmt instanceof AssignStmt)
			{
				AssignStmt ast = (AssignStmt) stmt;
				Value lhs = ast.getLeftOp();
				Value rhs = ast.getRightOp();
				
				/*
				 * Disable division patch
				if(rhs instanceof DivExpr)
				{
					DivExpr divExpr = (DivExpr) rhs;
					divPatchProbe(pc, body, stmt, (Stmt)pc.getSuccOf(stmt), lhs, divExpr);
				}*/
			}
			
			if(stmt instanceof IfStmt)
			{
				IfStmt ifs = (IfStmt) stmt;
				
				if(DynamicIfStmtInfo.dynamicIfStmtInfo.containsKey(ifs))
				{
					Object[] ret = DynamicIfStmtInfo.dynamicIfStmtInfo.get(ifs);
					
					if(ret.length == 3)
					{
						Unit invokePoint = (Unit) ret[0];
						
						if(invokePoint instanceof AssignStmt && ((AssignStmt) invokePoint).getRightOp() instanceof VirtualInvokeExpr)
						{
							VirtualInvokeExpr cie = (VirtualInvokeExpr) ((AssignStmt) invokePoint).getRightOp();
							if(cie.getMethod().getSignature().equals("<java.lang.String: int length()>"))
							{
								InvokeStmt toInstrument = (InvokeStmt) ret[1];
								AssignStmt ast = (AssignStmt) ret[2];
						
								pc.insertBefore(toInstrument, unit);
								pc.insertAfter(ast, toInstrument);
							}
							else
							{
								InvokeStmt toInstrument = (InvokeStmt) ret[1];
								AssignStmt ast = (AssignStmt) ret[2];
						
								pc.insertBefore(toInstrument, invokePoint);
								pc.insertAfter(ast, toInstrument);
							}
						}
						else
						{
							InvokeStmt toInstrument = (InvokeStmt) ret[1];
							AssignStmt ast = (AssignStmt) ret[2];
					
							pc.insertBefore(toInstrument, invokePoint);
							pc.insertAfter(ast, toInstrument);
						}
					}
					
					else if(ret.length == 4)
					{
						Unit invokePoint = (Unit) ret[0];
						InvokeStmt toInstrumentMax = (InvokeStmt) ret[1];
						InvokeStmt toInstrumentMin = (InvokeStmt) ret[2];
						AssignStmt ast = (AssignStmt) ret[3];
						
						//System.out.println("Invokept " + invokePoint);
						pc.insertBefore(toInstrumentMax, invokePoint);
						pc.insertAfter(toInstrumentMin, toInstrumentMax);
						pc.insertAfter(ast, toInstrumentMin);
					}
				}
			}
		}
		
		if(ENV.DEBUG_POST_PATCH_BODY_PRINT)
		{
			Iterator<Unit> i = pc.iterator();
		
			while(i.hasNext())
			{
				System.out.println(i.next());
			}
		}
		//body.validate();
		
		ENV.STAT_UNIT_POST_REPAIR += pc.size();
		
	}
	
}
	

