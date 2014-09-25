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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import boundedAnalysis.FlowInformation;
import boundedAnalysis.ForwardAnalysis;
import profile.InstrumManager;
import profile.UtilInstrum;
import soot.Body;
import soot.BodyTransformer;
import soot.BooleanType;
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
import soot.jimple.DoubleConstant;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.LengthExpr;
import soot.jimple.NopStmt;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.SubExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
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
		
		System.out.println("subSequencePatchProbe Executed ");
		
		return probe;
	}
	
	@SuppressWarnings("unchecked")
	private List<Stmt> subStringPatchProbe( Body jbody, Value lhs, VirtualInvokeExpr virtualInvokeExpr)
	{
		List<Stmt> probe = new ArrayList<Stmt>();
		SootMethod sMethod = virtualInvokeExpr.getMethod();
		
		//List<Value> args = virtualInvokeExpr.getArgs();
		//Value base = virtualInvokeExpr.getBase();
		
		SootClass stringClass = Scene.v().getSootClass("java.lang.String");
		//SootClass IndexRepairClass = Scene.v().loadClassAndSupport("stringrepair.IndexRepair");
		
		//SootMethod lengthMethod = stringClass.getMethod("int length()");
		
		if(sMethod.getSubSignature().equals("java.lang.String substring(int)"))
		{
					
			Object[] res = singleIndexPatcher(jbody, lhs, virtualInvokeExpr, false);
			
			List<Stmt> sl = (List<Stmt>) res[0];
			probe.addAll(sl);
			
			/*Now all included i single method call
			 * 
			 * Value index = args.get(0);
			Local len = new LocalGenerator(jbody).generateLocal(IntType.v());					
			
			VirtualInvokeExpr len_virtual = Jimple.v().newVirtualInvokeExpr((Local)base, lengthMethod.makeRef());			
			
			AssignStmt len_assign = Jimple.v().newAssignStmt(len, len_virtual);
			probe.add(len_assign);
			
			
			Local li = new LocalGenerator(jbody).generateLocal(IntType.v());
			
			StaticInvokeExpr repairIndex_static = Jimple.v().newStaticInvokeExpr(
					IndexRepairClass.getMethod("int getI(int,int)").makeRef(), Arrays.asList(new Value[]{index, len}));
			
					
			AssignStmt repairIndex_assign = Jimple.v().newAssignStmt(li, repairIndex_static);
			probe.add(repairIndex_assign);
			*/
			
			VirtualInvokeExpr substring_virtual = Jimple.v().newVirtualInvokeExpr((Local)res[1], 
					stringClass.getMethod("java.lang.String substring(int)").makeRef(), Arrays.asList(new Local[]{(Local) res[2]}));
			
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
			/*
			 * Now all included i single method call
			 * 
			Value first_index = args.get(0);
			Value second_index = args.get(1);														
			
			Local f_index = new LocalGenerator(jbody).generateLocal(IntType.v());
									
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
			*/
			
			Object[] res = doubleIndexPatcher(jbody, lhs, virtualInvokeExpr);
			
			List<Stmt> sl = (List<Stmt>) res[0];
			probe.addAll(sl);
			
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
			
			VirtualInvokeExpr substring_virtual = Jimple.v().newVirtualInvokeExpr((Local)res[1], 
					stringClass.getMethod("java.lang.String substring(int,int)").makeRef(), 
					Arrays.asList(new Local[]{(Local) res[2], (Local) res[3]}));
			
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
		System.out.println("subStringPatchProbe Executed ");
		return probe;
	}
	
	
	/*
	 * returns -> Object[0] = probe
	 * 			  Object[1] = base
	 * 			  Object[2] = li //index local to be  passed
	 * 
	 * flag = true for 1 <= index <= length 
	 */
	private Object[] singleIndexPatcher( Body jbody, Value lhs, VirtualInvokeExpr virtualInvokeExpr, Boolean flag)
	{
		List<Stmt> probe = new ArrayList<Stmt>();
		List<Value> args = virtualInvokeExpr.getArgs();
		Value base = virtualInvokeExpr.getBase();
		SootClass stringClass = Scene.v().getSootClass("java.lang.String");

		SootClass IndexRepairClass = Scene.v().loadClassAndSupport("stringrepair.IndexRepair");
		
		SootMethod lengthMethod = stringClass.getMethod("int length()");
		
		Value index = args.get(0);
		Local len = new LocalGenerator(jbody).generateLocal(IntType.v());					
		
		VirtualInvokeExpr len_virtual = Jimple.v().newVirtualInvokeExpr((Local)base, lengthMethod.makeRef());			
		
		AssignStmt len_assign = Jimple.v().newAssignStmt(len, len_virtual);
		probe.add(len_assign);
		
		
		Local li = new LocalGenerator(jbody).generateLocal(IntType.v());
		
		StaticInvokeExpr repairIndex_static = null;
		
		if (flag)
		{
			
			repairIndex_static = Jimple.v().newStaticInvokeExpr(
					IndexRepairClass.getMethod("int getI(int,int,double)").makeRef(), Arrays.asList(new Value[]{index, len, DoubleConstant.v(1.0)}));
		}
		
		else
		{
			repairIndex_static = Jimple.v().newStaticInvokeExpr(
				IndexRepairClass.getMethod("int getI(int,int)").makeRef(), Arrays.asList(new Value[]{index, len}));
		}
		
				
		AssignStmt repairIndex_assign = Jimple.v().newAssignStmt(li, repairIndex_static);
		probe.add(repairIndex_assign);

		Object[] ret = new Object[]{probe, base, li};
		
		System.out.println("##single Index patch Executed ");
		
		return ret;
	}
	
	/*
	 * returns -> Object[0] = probe
	 * 			  Object[1] = base
	 * 			  Object[2] = li //lower index local to be  passed
	 * 			  Object[3] = hi //higher index local to be  passed
	 */
	private Object[] doubleIndexPatcher( Body jbody, Value lhs, VirtualInvokeExpr virtualInvokeExpr)
	{
		List<Stmt> probe = new ArrayList<Stmt>();
		List<Value> args = virtualInvokeExpr.getArgs();
		Value base = virtualInvokeExpr.getBase();
		SootClass stringClass = Scene.v().getSootClass("java.lang.String");

		SootClass IndexRepairClass = Scene.v().loadClassAndSupport("stringrepair.IndexRepair");
		SootMethod lengthMethod = stringClass.getMethod("int length()");
		
		Value first_index = args.get(0);
		Value second_index = args.get(1);														
		
		Local f_index = new LocalGenerator(jbody).generateLocal(IntType.v());
								
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
		
		Object[] ret = new Object[]{probe, base, f_index, l_index};
		
		System.out.println("##double Index patch Executed ");
		
		return ret;
	}
	
	/*
	 * For static invoke expression
	 * return ->  Object[0] = probe
	 * 			  Object[1] = char array
	 * 			  Object[2] = offset
	 * 			  Object[3] = count
	 */
	private Object[] doubleIndexPatcher( Body jbody, Value lhs, StaticInvokeExpr virtualInvokeExpr)
	{
		List<Stmt> probe = new ArrayList<Stmt>();
		List<Value> args = virtualInvokeExpr.getArgs();

		SootClass IndexRepairClass = Scene.v().loadClassAndSupport("stringrepair.IndexRepair");
		
		Value charArray = args.get(0);
		Value offset = args.get(1);
		Value count = args.get(2);														
		
		Local offset_l = new LocalGenerator(jbody).generateLocal(IntType.v());
								
		Local count_l = new LocalGenerator(jbody).generateLocal(IntType.v());
		
		//character array length calculation
		Local len = new LocalGenerator(jbody).generateLocal(IntType.v());
		LengthExpr lenExpr = Jimple.v().newLengthExpr(charArray);						
		AssignStmt len_assign1 = Jimple.v().newAssignStmt(len, lenExpr);
		probe.add(len_assign1);
		
		
		StaticInvokeExpr staticInvI = Jimple.v().newStaticInvokeExpr(IndexRepairClass.getMethod("int getI(int,int,int,double)").makeRef(), 
				Arrays.asList(new Value[]{offset, count, len, DoubleConstant.v(1.0)}));
		
		StaticInvokeExpr staticInvJ = Jimple.v().newStaticInvokeExpr(IndexRepairClass.getMethod("int getJ(int,int,int,double)").makeRef(), 
				Arrays.asList(new Value[]{offset, count, len, DoubleConstant.v(1.0)}));
		
		AssignStmt assignI = Jimple.v().newAssignStmt(offset_l, staticInvI);
		AssignStmt assignJ = Jimple.v().newAssignStmt(count_l, staticInvJ);
		
		//System.out.println(assignI);
		probe.add(assignI);
		probe.add(assignJ);
		
		Object[] ret = new Object[]{probe, charArray, offset_l, count_l};
		
		System.out.println("##double Index patch Executed <static>");
		
		return ret;
	}
	
	
	
	/*
	 * handle same as subString(int)
	 */
	@SuppressWarnings("unchecked")
	private List<Stmt> charAtPatchProbe( Body jbody, Value lhs, VirtualInvokeExpr virtualInvokeExpr)
	{
		List<Stmt> probe = new ArrayList<Stmt>();
		SootClass stringClass = Scene.v().getSootClass("java.lang.String");
				
		Object[] res = singleIndexPatcher(jbody, lhs, virtualInvokeExpr, false);
		
		List<Stmt> sl = (List<Stmt>) res[0];
		probe.addAll(sl);
		VirtualInvokeExpr charAt_virtual = Jimple.v().newVirtualInvokeExpr((Local) res[1], 
				stringClass.getMethod("char charAt(int)").makeRef(), Arrays.asList(new Local[]{(Local) res[2]}));
		
		if(lhs == null)
		{
			InvokeStmt st = Jimple.v().newInvokeStmt(charAt_virtual);
			probe.add(st);
		}
		else
		{
			AssignStmt base_assign = Jimple.v().newAssignStmt(lhs, charAt_virtual);
			probe.add(base_assign);
		}
		
		System.out.println("charAtPatchProbe Executed ");
		
		return probe;
	}
	
	@SuppressWarnings("unchecked")
	private List<Stmt> codePointAtPatchProbe( Body jbody, Value lhs, VirtualInvokeExpr virtualInvokeExpr)
	{
		List<Stmt> probe = new ArrayList<Stmt>();
		SootClass stringClass = Scene.v().getSootClass("java.lang.String");
				
		Object[] res = singleIndexPatcher(jbody, lhs, virtualInvokeExpr, false);
		
		List<Stmt> sl = (List<Stmt>) res[0];
		probe.addAll(sl);
		VirtualInvokeExpr codePointAt_virtual = Jimple.v().newVirtualInvokeExpr((Local) res[1], 
				stringClass.getMethod("int codePointAt(int)").makeRef(), Arrays.asList(new Local[]{(Local) res[2]}));
		
		if(lhs == null)
		{
			InvokeStmt st = Jimple.v().newInvokeStmt(codePointAt_virtual);
			probe.add(st);
		}
		else
		{
			AssignStmt base_assign = Jimple.v().newAssignStmt(lhs, codePointAt_virtual);
			probe.add(base_assign);
		}
		
		System.out.println("codePointAtPatchProbe Executed ");
		
		return probe;
	}
	
	@SuppressWarnings("unchecked")
	private List<Stmt> codePointBeforePatchProbe( Body jbody, Value lhs, VirtualInvokeExpr virtualInvokeExpr)
	{
		List<Stmt> probe = new ArrayList<Stmt>();
		SootClass stringClass = Scene.v().getSootClass("java.lang.String");
				
		Object[] res = singleIndexPatcher(jbody, lhs, virtualInvokeExpr, true);
		
		List<Stmt> sl = (List<Stmt>) res[0];
		probe.addAll(sl);
		VirtualInvokeExpr codePointBefore_virtual = Jimple.v().newVirtualInvokeExpr((Local) res[1], 
				stringClass.getMethod("int codePointBefore(int)").makeRef(), Arrays.asList(new Local[]{(Local) res[2]}));
		
		if(lhs == null)
		{
			InvokeStmt st = Jimple.v().newInvokeStmt(codePointBefore_virtual);
			probe.add(st);
		}
		else
		{
			AssignStmt base_assign = Jimple.v().newAssignStmt(lhs, codePointBefore_virtual);
			probe.add(base_assign);
		}
		
		System.out.println("codePointAtPatchProbe Executed ");
		
		return probe;
	}
	
	@SuppressWarnings("unchecked")
	private List<Stmt> codePointCountPatchProbe( Body jbody, Value lhs, VirtualInvokeExpr virtualInvokeExpr)
	{
		List<Stmt> probe = new ArrayList<Stmt>();
		SootClass stringClass = Scene.v().getSootClass("java.lang.String");
				
		Object[] res = doubleIndexPatcher(jbody, lhs, virtualInvokeExpr);
		
		List<Stmt> sl = (List<Stmt>) res[0];
		probe.addAll(sl);
		VirtualInvokeExpr codePointCount_virtual = Jimple.v().newVirtualInvokeExpr((Local) res[1], 
				stringClass.getMethod("int codePointCount(int,int)").makeRef(), 
				Arrays.asList(new Local[]{(Local) res[2], (Local) res[3]}));
		
		if(lhs == null)
		{
			InvokeStmt st = Jimple.v().newInvokeStmt(codePointCount_virtual);
			probe.add(st);
		}
		else
		{
			AssignStmt base_assign = Jimple.v().newAssignStmt(lhs, codePointCount_virtual);
			probe.add(base_assign);
		}
		
		System.out.println("codePointAtPatchProbe Executed ");
		
		return probe;
	}
	
	@SuppressWarnings("unchecked")
	private List<Stmt> valueOfPatchProbe( Body jbody, Value lhs, StaticInvokeExpr staticlInvokeExpr)
	{
		List<Stmt> probe = new ArrayList<Stmt>();
		SootClass stringClass = Scene.v().getSootClass("java.lang.String");
				
		Object[] res = doubleIndexPatcher(jbody, lhs, staticlInvokeExpr);
		
		List<Stmt> sl = (List<Stmt>) res[0];
		probe.addAll(sl);
		
		StaticInvokeExpr valueOf_static = Jimple.v().newStaticInvokeExpr(
				stringClass.getMethod("java.lang.String valueOf(char[],int,int)").makeRef(), 
				Arrays.asList(new Local[]{(Local) res[1], (Local) res[2], (Local) res[3]}));
		
		
		if(lhs == null)
		{
			InvokeStmt st = Jimple.v().newInvokeStmt(valueOf_static);
			probe.add(st);
		}
		else
		{
			AssignStmt base_assign = Jimple.v().newAssignStmt(lhs, valueOf_static);
			probe.add(base_assign);
		}
		
		System.out.println("valueofPatchProbe Executed ");
		
		return probe;
	}
	
	
	
	private <T extends InvokeExpr> Body  makePatchProbe(PatchingChain<Unit> ch ,
			Body jbody, Stmt try_start_stmt, Stmt try_end_stmt, Value lhs, T InvokeExpr)
	{
		 List<Stmt> probe = new ArrayList<Stmt>();
		 
		 SootMethod sMethod = InvokeExpr.getMethod();
		 System.out.println(sMethod.getSubSignature());
		 
		 if(sMethod == null)
			 return null;
		 
		 SootClass thrwCls = null;
		 
		 
		 VirtualInvokeExpr virtualInvokeExpr = (InvokeExpr instanceof VirtualInvokeExpr) ? (VirtualInvokeExpr)InvokeExpr : null;

		 StaticInvokeExpr staticInvokeExpr = (InvokeExpr instanceof StaticInvokeExpr) ? (StaticInvokeExpr)InvokeExpr : null;
		 
		 //Debug
		 //System.out.println(sMethod);
		 
		 if(sMethod.getSubSignature().equals("char charAt(int)") || sMethod.getSubSignature().equals("int codePointAt(int)") 
				 || sMethod.getSubSignature().equals("int codePointBefore(int)") 
				 || sMethod.getSubSignature().equals("int codePointCount(int,int)")
				 || sMethod.getSubSignature().equals("int offsetByCodePoints(int,int)") 
				 || sMethod.getSubSignature().equals("void getChars(int,int,char[],int)")
				 || sMethod.getSubSignature().equals("void getBytes(int,int,byte[],int)")
				 || sMethod.getSubSignature().equals("java.lang.String substring(int)")
				 || sMethod.getSubSignature().equals("java.lang.String substring(int,int)")
				 || sMethod.getSubSignature().equals("java.lang.CharSequence subSequence(int,int)")
				 || sMethod.getSubSignature().equals("java.lang.String valueOf(char[],int,int)")
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
    	 
    	 if(sMethod.getSubSignature().equals("char charAt(int)"))
    	 {
    		 probe.addAll(charAtPatchProbe(jbody, lhs, virtualInvokeExpr));
    	 }
    	 
    	 if(sMethod.getSubSignature().equals("int codePointAt(int)"))
    	 {
    		 probe.addAll(codePointAtPatchProbe(jbody, lhs, virtualInvokeExpr));
    	 }
    	 
    	 if(sMethod.getSubSignature().equals("int codePointBefore(int)"))
    	 {
    		 probe.addAll(codePointBeforePatchProbe(jbody, lhs, virtualInvokeExpr));
    	 }
    	 
    	 if(sMethod.getSubSignature().equals("int codePointCount(int,int)"))
    	 {
    		 probe.addAll(codePointCountPatchProbe(jbody, lhs, virtualInvokeExpr));
    	 }
    	 
    	 if(sMethod.getSubSignature().equals("int offsetByCodePoints(int,int)"))
    	 {
    		 probe.addAll(codePointCountPatchProbe(jbody, lhs, virtualInvokeExpr));
    	 }
    	 
    	 if(sMethod.getSubSignature().equals("java.lang.String valueOf(char[],int,int)"))
    	 {
    		 probe.addAll(valueOfPatchProbe(jbody, lhs, staticInvokeExpr));
    	 }
    	 
    	 
    	 //add assignment statements
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
		
		//see forward flow analysis and then look for the variables
		ForwardAnalysis fwA = new ForwardAnalysis(new BriefUnitGraph(body));
        
        String methodSig = sMethod.getSignature();
		
        
		PatchingChain<Unit> pc= body.getUnits();
		
		Iterator<Unit> it = pc.snapshotIterator();
		
		while(it.hasNext())
		{
			Unit unit = it.next();
			Stmt stmt = (Stmt) unit;			
	        
			if(FlowInformation.getOutValueFromUnit(methodSig, unit) != null)
				System.out.println("%%%%%  " + FlowInformation.getOutValueFromUnit(methodSig, unit));
			//System.out.println(stmt);
			
			if(stmt instanceof AssignStmt)
			{
				AssignStmt ast = (AssignStmt) stmt;
				
				Value lhs = ast.getLeftOp();
				Value rhs = ast.getRightOp();
				
				//For string API all the calls would be virtual invoke expression
				if(rhs instanceof VirtualInvokeExpr)
				{
					
					//System.out.println(stmt);
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
				
				if(rhs instanceof StaticInvokeExpr)
				{
					StaticInvokeExpr staticInvokeExpr = (StaticInvokeExpr) rhs;
					
					Body b = makePatchProbe(pc, body, stmt, (Stmt)pc.getSuccOf(stmt), lhs, staticInvokeExpr);
					
					if(b == null)
						continue;
				}
			}
			
			if(stmt instanceof InvokeStmt)
			{
				InvokeExpr invokeExpr = stmt.getInvokeExpr();
				
				if(invokeExpr instanceof VirtualInvokeExpr)
				{
					//System.out.println(stmt);
					
					VirtualInvokeExpr virtualInvokeExpr = (VirtualInvokeExpr) invokeExpr;
					
					Body b = makePatchProbe(pc, body, stmt, (Stmt)pc.getSuccOf(stmt), null, virtualInvokeExpr);
					
					if(b == null)
						continue;							
				}
				
				if(invokeExpr instanceof StaticInvokeExpr)
				{
					//System.out.println(stmt);
					
					StaticInvokeExpr staticInvokeExpr = (StaticInvokeExpr) invokeExpr;
					
					Body b = makePatchProbe(pc, body, stmt, (Stmt)pc.getSuccOf(stmt), null, staticInvokeExpr);
					
					if(b == null)
						continue;							
				}
			}
		}
	}	
}
