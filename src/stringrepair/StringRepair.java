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


package stringrepair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import profile.InstrumManager;
import profile.UtilInstrum;
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
import soot.Unit;
import soot.Value;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.SubExpr;
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
     
        /* DEBUG
        Scanner s = new Scanner(System.in);
        String st = s.next();
        */
        String st = "c";
        
        if(st.equalsIgnoreCase("j"))
        	Options.v().set_output_format(Options.output_format_jimple);
        
        if(st.equalsIgnoreCase("c"))
        	Options.v().set_output_format(Options.output_format_class);     
        
        Scene.v().addBasicClass("stringrepair.IndexRepair",SootClass.SIGNATURES);
        
        soot.Main.main(className);
	}
	
	
	/*
	 * Get method String API method call from the virtual invoke expression
	 */
	
	public SootMethod getVirtualStringMethodCall(VirtualInvokeExpr virtualInvokeExpr)
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
	 * public CharSequence subSequence(int beginIndex,int endIndex)
	 * Returns a new character sequence that is a subsequence of this sequence.
	 * 
	 * IndexOutOfBoundsException - if beginIndex or endIndex are negative, 
	 * if endIndex is greater than length(), or if beginIndex is greater than startIndex
	 * 
	 * behaves in exactly the same way as the invocation 
	 * str.substring(begin, end)
	 * 
	 * beginIndex - the begin index, inclusive.
	 * endIndex - the end index, exclusive.
	 */
	private List<Stmt> subSequencePatchProbe( Body jbody, Value lhs, VirtualInvokeExpr virtualInvokeExpr)
	{
		List<Stmt> probe = new ArrayList<Stmt>();
		
		List<Value> args = virtualInvokeExpr.getArgs();
		Value base = virtualInvokeExpr.getBase();
		
		SootClass stringClass = Scene.v().getSootClass("java.lang.String");
		SootClass IndexRepairClass = Scene.v().loadClassAndSupport("stringrepair.IndexRepair");
		SootMethod lengthMethod = stringClass.getMethod("int length()");
		
		Value beginIndex = args.get(0);
		Value endIndex = args.get(1);
		
		Local len = new LocalGenerator(jbody).generateLocal(IntType.v());					
		
		VirtualInvokeExpr len_virtual = Jimple.v().newVirtualInvokeExpr((Local)base, lengthMethod.makeRef());			
		
		AssignStmt len_assign = Jimple.v().newAssignStmt(len, len_virtual);
		probe.add(len_assign);
		
		StaticInvokeExpr staticInvI = Jimple.v().newStaticInvokeExpr(IndexRepairClass.getMethod("int getI(int,int,int)").makeRef(), 
				Arrays.asList(new Value[]{beginIndex, endIndex, len}));
		
		StaticInvokeExpr staticInvJ = Jimple.v().newStaticInvokeExpr(IndexRepairClass.getMethod("int getJ(int,int,int)").makeRef(), 
				Arrays.asList(new Value[]{beginIndex, endIndex, len}));
		
		Local f_index = new LocalGenerator(jbody).generateLocal(IntType.v());
		Local l_index = new LocalGenerator(jbody).generateLocal(IntType.v());
		
		AssignStmt assignI = Jimple.v().newAssignStmt(f_index, staticInvI);
		AssignStmt assignJ = Jimple.v().newAssignStmt(l_index, staticInvJ);
		
		probe.add(assignI);
		probe.add(assignJ);
		
		
		VirtualInvokeExpr subsequence_virtual = Jimple.v().newVirtualInvokeExpr((Local)base, 
				stringClass.getMethod("java.lang.CharSequence subSequence(int,int)").makeRef(), Arrays.asList(new Local[]{f_index, l_index}));
		
		if(lhs == null)
		{
			InvokeStmt st = Jimple.v().newInvokeStmt(subsequence_virtual);
			probe.add(st);
		}
		else
		{
			AssignStmt base_assign = Jimple.v().newAssignStmt(lhs, subsequence_virtual);
			probe.add(base_assign);
		}
		
		
		
		return probe;
	}
	
	private List<Stmt> subStringPatchProbe( Body jbody, Value lhs, VirtualInvokeExpr virtualInvokeExpr)
	{
		List<Stmt> probe = new ArrayList<Stmt>();
		SootMethod sMethod = virtualInvokeExpr.getMethod();
		
		List<Value> args = virtualInvokeExpr.getArgs();
		Value base = virtualInvokeExpr.getBase();
		
		SootClass stringClass = Scene.v().getSootClass("java.lang.String");
		SootClass IndexRepairClass = Scene.v().loadClassAndSupport("stringrepair.IndexRepair");
		
		SootMethod lengthMethod = stringClass.getMethod("int length()");
		
		if(sMethod.getSubSignature().equals("java.lang.String substring(int)"))
		{
			Value index = args.get(0);
			Local len = new LocalGenerator(jbody).generateLocal(IntType.v());					
			
			VirtualInvokeExpr len_virtual = Jimple.v().newVirtualInvokeExpr((Local)base, lengthMethod.makeRef());			
			
			AssignStmt len_assign = Jimple.v().newAssignStmt(len, len_virtual);
			probe.add(len_assign);
			
			/* 
			 * No longer required
			 * 
			SubExpr len_sub = Jimple.v().newSubExpr(len, IntConstant.v(1));
			AssignStmt sub_len_assign = Jimple.v().newAssignStmt(len, len_sub);
			probe.add(sub_len_assign);
			*/
			
			Local li = new LocalGenerator(jbody).generateLocal(IntType.v());
			
			StaticInvokeExpr repairIndex_static = Jimple.v().newStaticInvokeExpr(
					IndexRepairClass.getMethod("int getI(int,int)").makeRef(), Arrays.asList(new Value[]{index, len}));
			
					
			AssignStmt repairIndex_assign = Jimple.v().newAssignStmt(li, repairIndex_static);
			probe.add(repairIndex_assign);
			
			VirtualInvokeExpr substring_virtual = Jimple.v().newVirtualInvokeExpr((Local)base, 
					stringClass.getMethod("java.lang.String substring(int)").makeRef(), Arrays.asList(new Local[]{li}));
			
			if(lhs == null)
			{
				InvokeStmt st = Jimple.v().newInvokeStmt(substring_virtual);
				probe.add(st);
			}
			else
			{
				AssignStmt base_assign = Jimple.v().newAssignStmt(lhs, substring_virtual);
				probe.add(base_assign);
			}
			
			
		}
		
		if(sMethod.getSubSignature().equals("java.lang.String substring(int,int)"))
		{
			Value first_index = args.get(0);
			Value second_index = args.get(1);														
			
			Local f_index = new LocalGenerator(jbody).generateLocal(IntType.v());
									
			/*
			 * IfStmt is no longer required here
			 */			
			
			Local l_index = new LocalGenerator(jbody).generateLocal(IntType.v());
			
			Local len = new LocalGenerator(jbody).generateLocal(IntType.v());
			VirtualInvokeExpr len_virtual1 = Jimple.v().newVirtualInvokeExpr((Local)base, lengthMethod.makeRef());						
			AssignStmt len_assign1 = Jimple.v().newAssignStmt(len, len_virtual1);
			probe.add(len_assign1);
			
			
			StaticInvokeExpr staticInvI = Jimple.v().newStaticInvokeExpr(IndexRepairClass.getMethod("int getI(int,int,int)").makeRef(), 
					Arrays.asList(new Value[]{first_index, second_index, len}));
			
			StaticInvokeExpr staticInvJ = Jimple.v().newStaticInvokeExpr(IndexRepairClass.getMethod("int getJ(int,int,int)").makeRef(), 
					Arrays.asList(new Value[]{first_index, second_index, len}));
			
			AssignStmt assignI = Jimple.v().newAssignStmt(f_index, staticInvI);
			AssignStmt assignJ = Jimple.v().newAssignStmt(l_index, staticInvJ);
			
			//System.out.println(assignI);
			probe.add(assignI);
			probe.add(assignJ);
			
			
			/*
			 * Not Needed
			 /*
			VirtualInvokeExpr len_virtual = Jimple.v().newVirtualInvokeExpr((Local)base, lengthMethod.makeRef());			
			
			AssignStmt len_assign = Jimple.v().newAssignStmt(l_index, len_virtual);
			probe.add(len_assign);
						
					
			SubExpr len_sub = Jimple.v().newSubExpr(l_index, IntConstant.v(1));
			AssignStmt sub_len_assign = Jimple.v().newAssignStmt(l_index, len_sub);
			probe.add(sub_len_assign);
			*/
			
			VirtualInvokeExpr substring_virtual = Jimple.v().newVirtualInvokeExpr((Local)base, 
					stringClass.getMethod("java.lang.String substring(int,int)").makeRef(), Arrays.asList(new Local[]{f_index, l_index}));
			
			if(lhs == null)
			{
				InvokeStmt st = Jimple.v().newInvokeStmt(substring_virtual);
				probe.add(st);
			}
			else
			{
				AssignStmt base_assign = Jimple.v().newAssignStmt(lhs, substring_virtual);
				probe.add(base_assign);
			}
			
			
		}
		
		return probe;
	}

	private Body makePatchProbe(PatchingChain<Unit> ch , Body jbody, Stmt try_start_stmt, Stmt try_end_stmt, Value lhs, VirtualInvokeExpr virtualInvokeExpr)
	{
		 List<Stmt> probe = new ArrayList<Stmt>();
		 //System.out.println(sMethod.getSubSignature());
		 SootMethod sMethod = virtualInvokeExpr.getMethod();
		 
		 if(sMethod == null)
			 return null;
		 
		 SootClass thrwCls = null;
		 
		 if(sMethod.getSubSignature().equals("char chatAt(int)") || sMethod.getSubSignature().equals("inr codePointAt(int)") 
				 || sMethod.getSubSignature().equals("int codePointBefore(int)") 
				 || sMethod.getSubSignature().equals("int codePointCount(int,int)")
				 || sMethod.getSubSignature().equals("int offsetByCodePoints(int,int)") 
				 || sMethod.getSubSignature().equals("void getChars(int,int,char[],int)")
				 || sMethod.getSubSignature().equals("void getBytes(int,int,byte[],int)")
				 || sMethod.getSubSignature().equals("java.lang.String substring(int)")
				 || sMethod.getSubSignature().equals("java.lang.String substring(int,int)")
				 || sMethod.getSubSignature().equals("java.lang.CharSequence subSequence(int,int)")
				 )
		 {
			 thrwCls = Scene.v().getSootClass("java.lang.IndexOutOfBoundsException");
		 
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
    	 
    	 //call patching function based on methods
    	 
    	 if( sMethod.getSubSignature().equals("java.lang.String substring(int)")
				 || sMethod.getSubSignature().equals("java.lang.String substring(int,int)"))
    	 {
    		 probe.addAll(subStringPatchProbe(jbody, lhs, virtualInvokeExpr));
    	 }
    	 
    	 if(sMethod.getSubSignature().equals("java.lang.CharSequence subSequence(int,int)"))
    	 {
    		 probe.addAll(subSequencePatchProbe(jbody, lhs, virtualInvokeExpr));
    	 }
    	 
    	 //add assignment statemnets
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
						
			//System.out.println(stmt);
			
			if(stmt instanceof AssignStmt)
			{
				AssignStmt ast = (AssignStmt) stmt;
				
				Value lhs = ast.getLeftOp();
				Value rhs = ast.getRightOp();
				
				//For string API all the calls would be virtual invoke expression
				if(rhs instanceof VirtualInvokeExpr)
				{
					
					System.out.println(stmt);
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
					System.out.println(stmt);
					
					VirtualInvokeExpr virtualInvokeExpr = (VirtualInvokeExpr) invokeExpr;
					
					Body b = makePatchProbe(pc, body, stmt, (Stmt)pc.getSuccOf(stmt), null, virtualInvokeExpr);
					
					if(b == null)
						continue;
					
					
				}
			}
		}
	}
	
	
	
}
