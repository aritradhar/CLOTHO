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


package ConstraintAutomata;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import profile.InstrumManager;
import profile.UtilInstrum;
import soot.*;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.*;
import soot.util.*;

public class classAnalysis_TryCatch extends BodyTransformer
{	 
	private Local generateNewLocal(Body body, Type type)
	{
		LocalGenerator lg = new LocalGenerator(body);
		return lg.generateLocal(type);
	}	
	
	private NopStmt insertNopStmt(Body body, Unit u)
	{
		NopStmt nop = Jimple.v().newNopStmt();
		body.getUnits().insertAfter(nop, u);
		return nop;
	}	
	
	private Stmt getNextStmt(Body body, int stmt_no)
	{
		Stmt nextStmt = null;
		Iterator<Unit> it = body.getUnits().snapshotIterator();
		int counter = 0;
		while(it.hasNext())
		{
			++counter;
		    Stmt temp = (Stmt) it.next();
			if(counter == stmt_no + 1)
			{
				nextStmt = temp;
				break;
			}
		}
		
		//System.out.println("^^^^" + nextStmt.toString());
		return nextStmt;
	}
	
	public Value getLeftHandFromDivision(Unit unit)
	{
		 List<ValueBox> boxl = unit.getUseAndDefBoxes();
		 Iterator<ValueBox> itbox = boxl.iterator();
		 Value v_patch_candidate = null;
		 
		 while(itbox.hasNext())
		 {
			 ValueBox tempBox = itbox.next();			 
			 if(tempBox.toString().contains("LinkedVariableBox"))
			 {
				 v_patch_candidate = tempBox.getValue();
				 break;
			 }			 
		 }
		 
		 return v_patch_candidate;
	}
	
	public Value getArrayFromArrayExpr(Unit unit)
	{
		 List<ValueBox> boxl = unit.getUseAndDefBoxes();
		 Iterator<ValueBox> itbox = boxl.iterator();
		 Value array_variable = null;
		 
		 while(itbox.hasNext())
		 {
			 ValueBox tempBox = itbox.next();
			 //test
			 //System.out.println(tempBox.toString());
			 if(tempBox.toString().contains("LinkedVariableBox"))
			 {
				 array_variable = tempBox.getValue();
				 //break;
			 }	
		 }
		 
		 return array_variable;
	}
	
	public Value getIndexFromArrayExpr(Unit unit)
	{
		 List<ValueBox> boxl = unit.getUseAndDefBoxes();
		 Iterator<ValueBox> itbox = boxl.iterator();
		 Value v_patch_candidate = null;
		 
		 while(itbox.hasNext())
		 {
			 ValueBox tempBox = itbox.next();
			 //test
			 //System.out.println(tempBox.toString());
			 if(tempBox.toString().contains("LinkedRValueBox"))
			 {
				 v_patch_candidate = tempBox.getValue();
				 //break;
			 }	
		 }
		 ValueBox vb = (ValueBox) v_patch_candidate.getUseBoxes().get(0);
		 Value ret = vb.getValue();
		 //System.out.println("$$   "+vb.getValue().toString());
		 return ret;
		 
	}
	
	public boolean isLocalArryIndex(HashMap<String, Local> string_localmap, Unit unit)
	{
		 List<ValueBox> boxl = unit.getUseAndDefBoxes();
		 Iterator<ValueBox> itbox = boxl.iterator();
		 Value v_patch_candidate = null;
		 
		 while(itbox.hasNext())
		 {
			 ValueBox tempBox = itbox.next();
			 //System.out.println(tempBox.toString());
			 if(tempBox.toString().contains("LinkedRValueBox"))
			 {
				 v_patch_candidate = tempBox.getValue();
				 //break;
			 }	
		 }
		 ValueBox vb = (ValueBox) v_patch_candidate.getUseBoxes().get(0);
		 Value ret = vb.getValue();
		 
		 return string_localmap.containsKey(ret.toString());
	}
	
	
	public Local getNewArray(Type type)
	{
		Local arg = null;
		String stype = type.toString();
		
		
		//handle basic types
		if(stype.equals("int[]"))
   			 arg = Jimple.v().newLocal("<NewArr>", ArrayType.v(IntType.v(), 1));
		
		if(stype.equals("float[]"))
  			 arg = Jimple.v().newLocal("<NewArr>", ArrayType.v(FloatType.v(), 1));
		
		if(stype.equals("double[]"))
  			 arg = Jimple.v().newLocal("<NewArr>", ArrayType.v(DoubleType.v(), 1));
		
		if(stype.equals("char[]"))
  			 arg = Jimple.v().newLocal("<NewArr>", ArrayType.v(CharType.v(), 1));
		
		if(stype.equals("long[]"))
  			 arg = Jimple.v().newLocal("<NewArr>", ArrayType.v(LongType.v(), 1));
		
		if(stype.equals("short[]"))
 			 arg = Jimple.v().newLocal("<NewArr>", ArrayType.v(ShortType.v(), 1));
		
		if(stype.equals("byte[]"))
 			 arg = Jimple.v().newLocal("<NewArr>", ArrayType.v(ByteType.v(), 1));
		
		if(stype.equals("boolean[]"))
			 arg = Jimple.v().newLocal("<NewArr>", ArrayType.v(BooleanType.v(), 1));
		
		//else resolve from class
		else
		{
			stype = stype.substring(0,stype.indexOf('['));
			arg = Jimple.v().newLocal("<NewArr>", ArrayType.v(RefType.v(stype), 1));
		}
		return arg;
	}
	
	
	
	protected void internalTransform(Body jbody, String phaseName, Map options) 
	{
		 // this is to analyze potential ArrayIndexOutofBoundException and other RuntimeException

	     SootMethod smethod = jbody.getMethod();
	     System.out.println("<<<<<<<< Current Method : "+smethod.getName()+" >>>>>>>>");
	     
	     Chain<Local> l = jbody.getLocals();
	     Iterator<Local> it_local = l.iterator();
	     
	     HashMap<String, String> localmap = new HashMap<>();
	     HashMap<String, Local> string_localmap = new HashMap<>();
	     
	     System.out.println("Variables\n--------------------------------------------------------");
	     while(it_local.hasNext())
	     {
	    	 Local loc = it_local.next();
	    	 localmap.put(loc.toString(), loc.getType().toString());
	    	 string_localmap.put(loc.toString(), loc);
	    	 
	    	 System.out.println(loc+" : " + loc.getType());
	     }
	     
	     System.out.println("\nStatement wise symbolic analysis\n----------------------------------------------------------");
	     
	     
	     PatchingChain<Unit> ch=jbody.getUnits();
	     
	     
	     Iterator<Unit> it = ch.snapshotIterator();
	     String str = "";
	     
	     
	     /////////////////////////////////////////////////////////
	     
	     Stmt try_start_stmt = null;
	     Stmt try_end_stmt = null;
	     Boolean fg = false;
	     int stmt_counter = 0;
	     
	 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
	     while(it.hasNext())
	     {
	    	 ++stmt_counter;
	    	 
	    	 Unit unit = it.next();
	    	 Stmt stmt = (Stmt)unit;
	    	 
	    	 System.out.println(unit);
	    	 
	    	 List usedefbox = unit.getUseAndDefBoxes();
	    	 //System.out.println(usedefbox);
	    	 
	    	 
	    	 //System.out.println(s);
	    	 Iterator it_box  = usedefbox.iterator();
	    	 int flag=0;
	    	 
	    	 if(unit.toString().contains("@parameter")) 
	    		 continue; //handle new array separately
	    	 
	    	 //handle GotoStmt separately
	    	 if(stmt instanceof GotoStmt)
	    		 continue;
	    	 if(unit.toString().contains("newarray"))
	    	 {
	    		 if(isLocalArryIndex(string_localmap, unit))
	    			 flag = 4;
	    		 else
	    			 flag = 99;
	    	 }
	    		 
	    	 //handle non-Invoke expressions
	    	 if (!stmt.containsInvokeExpr() && flag!=4 && flag!=99)
	    	 {
	    		 while(it_box.hasNext())
	    		 { 
	    			 str = it_box.next().toString();
	    			 //System.out.println(":::"+str);	 
	    			 int pos=0;
	    			 for(int i=0;i<str.length();i++)
	    			 {
	    				 if(str.charAt(i) == '(')
	    				 {
	    					 pos = i;
	    					 break;
	    				 }
	    			 }
	    			 
	    			 str = str.substring(pos+1,str.length()-1);
	    			 
	    			 
	    			 if(localmap.containsKey(str) && localmap.get(str).contains("["))
	    			 {
	    				 //System.out.println("%%%%"+str);
	    				 int start = 0;
	    				 int end = str.length()-1;
	    				 for(int m=0; m<str.length(); m++)
	    				 {
	    					 if(str.charAt(m) == '[')
	    						 start = m;
	    				 }
	    				 
	    				 String indexVar = str.substring(start+1,end);
	    				 //System.out.println("%%%%"+str);
	    				 //System.out.println(" Array Index Out of Bound/ -ve index May occur");
	    				 flag = 1;
	    				 break;
	    			 }
	    			 
	    			 
	    			 
	    	    
	    			 if(str.contains("/"))
	    			 {
	    				 flag = 2;
	    				 continue;
	    			 }
	    	    
	    			 //System.out.print(str + ":::");
	    		 }
	    	 }
	    	 
	    	 //handle invoke expressions
	    	 else if(flag != 4 && flag != 99)
	    	 {
	    		//check for NoSuchElementException
	    		 InvokeExpr expr = (InvokeExpr)stmt.getInvokeExpr();
	    		 if(expr instanceof InterfaceInvokeExpr)
	    		 {
	    			 SootMethod expMethod = expr.getMethod();
	    			 if(expMethod.getName().toString().equals("next") && expMethod.getDeclaringClass().toString().equals("java.util.Iterator"))
	    				 flag = 3;
	    		 }
	    	 }
	    	 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    	 
	    	 if (flag == 1)
	    	 {
	    		 System.out.println("#### Array Index Out of Bound/ -ve index May occur ####");
	    		 Boolean t1 = false, t2 = false;
	    		 
	    		 //instrument invariant
	    		 List usedefbox_in = unit.getUseAndDefBoxes();
	    		 Iterator it_use_def = usedefbox_in.iterator();
	    		 
	    		 Local index_local = null;
	    		 Local base_array_local = null;
	    		 
	    		 while(it_use_def.hasNext())
	    		 {
	    			 String tmp = it_use_def.next().toString();
	    			 String index_variable_name = tmp;
	    			 String base_array_variable_name = tmp;
	    			 
	    			 //System.out.println(temp);
	    			 
	    			 
	    			 //////////////////////////////////////////////////////////////////////////////////////
	    			 //Look for index in immediate box
	    			 if(index_variable_name.contains("ImmediateBox"))
	    			 {
	    				 index_variable_name = index_variable_name.substring(index_variable_name.indexOf("(") + 1,index_variable_name.indexOf(")"));
	    				 
	    				 //System.out.println(":::"+index_variable_name);
	    				 
	    				 if(string_localmap.containsKey(index_variable_name))
	    					 t1 = true;
	    			 }
	    			 
	    			 ///////////////////////////////////////////////////////////////////////////////////////
	    			 //look for base array in the Jimple local box
	    			 if(base_array_variable_name.contains("JimpleLocalBox"))
	    			 {
	    				 base_array_variable_name = base_array_variable_name.substring(base_array_variable_name.indexOf("(") + 1,base_array_variable_name.indexOf(")"));
	    				 
	    				 //System.out.println(":::"+base_array_variable_name);
	    				 
	    				 if(string_localmap.containsKey(base_array_variable_name))
	    					 t2 = true;
	    				 	    				 
	    			 }
	    			 
	    			 
	    			 ////////////////////////////////////////////////////////////////////////////////////////
	    			 if(string_localmap.containsKey(index_variable_name) && t1 && t2)
	    				 index_local = string_localmap.get(index_variable_name);
	    			 /////////////////////////////////////////////////////////////////////////////////////////
	    			 if(string_localmap.containsKey(base_array_variable_name))	    						 
	    				 base_array_local = string_localmap.get(base_array_variable_name);
	    				 //System.out.println(base_array_local.toString());
	    			 
	    			 ////////////////////////////////////////////////////////////////////////////////////////
	    		 }
	    		 
	    		 
	    		 
	    		 if(t1 && t2) 
	    		 {
	    			 //make the throwable probing clause	    			 
	    			 //setting the start of the try probe at the statement
	    			 try_start_stmt = stmt;
	    			 //Maybe the right idea
	    			 //try_end_stmt = (Stmt) ch.getLast();
	    			 try_end_stmt = (Stmt) ch.getSuccOf(stmt);
	    			 //try_end_stmt = getNextStmt(jbody, stmt_counter);
	    			 ////////////////////////////////////////////////////////////////////////////////////////////
	    			 
	    			 List<Stmt> probe = new ArrayList<Stmt>();
	    	    	 SootClass thrwCls = Scene.v().getSootClass("java.lang.IndexOutOfBoundsException");
	    	    	 Stmt sGotoLast = Jimple.v().newGotoStmt(try_end_stmt);
	    	    	 probe.add(sGotoLast);
	    		 
	    	    	 Local lException1 = UtilInstrum.getCreateLocal(jbody, "<ex1>", RefType.v(thrwCls));
	    	    	 Stmt sCatch = Jimple.v().newIdentityStmt(lException1, Jimple.v().newCaughtExceptionRef());
	    	    	 probe.add(sCatch);
	    		 
	    	    	 //stmt for catch block	    
	    	    	 
	    	    	 /*
	    	    	 SootMethod string_length_method = Scene.v().getMethod("<java.lang.String: int length()>");
	    	    	 
	    			 //System.out.println(string_length.toString());
	    			 VirtualInvokeExpr s_len_check_expr = Jimple.v().newVirtualInvokeExpr(base_array_local, string_length_method.makeRef());
	    			 //System.out.println("::"+s_len_check_expr.toString());	    			 
	    			 
	    			 //Local array_length_left_local = generateNewLocal(jbody, IntType.v());
	    			  * 
	    			  */
	    			
	    			 Local array_length_left_local = UtilInstrum.getCreateLocal(jbody, "<len_temp>", IntType.v());
	    			 
	    			 /*
	    			 AssignStmt patch_assign = Jimple.v().newAssignStmt(array_length_left_local, s_len_check_expr);
	    			 System.out.println(patch_assign);
	    	    	 
	    			 //probe.add(patch_assign);
	    			 
	    			 AddExpr normalize_expr = Jimple.v().newAddExpr(array_length_left_local, IntConstant.v(-1));
	    			 AssignStmt normalize_assign = Jimple.v().newAssignStmt(array_length_left_local, normalize_expr);
	    			 */
	    			 //probe.add(normalize_assign);
	    			 
	    			 AssignStmt zeroAssign = Jimple.v().newAssignStmt(array_length_left_local, IntConstant.v(0));
	    			 probe.add(zeroAssign);
	    	    	 /////////////////////////////////////////////////////
	    	    	 InstrumManager.v().insertRightBeforeNoRedirect(ch, probe, try_end_stmt);
	    			
	    	    	 // insert trap (catch)
	    	    	 jbody.getTraps().add(Jimple.v().newTrap(thrwCls, try_start_stmt, sGotoLast, sCatch));
	    	    	 jbody.validate();
	    			 
	    			 ///////////////////////////////////////////////////////////////////////////////////////////
	    			 
	    		     fg = true;
	    		 }
	    	 }
	    	 
	    	 
	    	 
	    	 if (flag == 2)
	    	 {
	    		 //Handles by making the lhs to 1
	    		 System.out.println("#### Divide by Zero Exception may happen ####");
	    		 //System.out.println("unit  "+unit.toString());
	    		 //get local///////////////////////////////
	    		 Value v = getLeftHandFromDivision(unit);
	    		 Local l1 =(Local) v;
	    		 Local lhs_to_patched = null;
	    		 if(string_localmap.containsKey(l1.toString()))
	    			 lhs_to_patched = string_localmap.get(l1.toString());
	    		 	    		 
	    		 
	    		 //instrument try catch
	    		 try_start_stmt = stmt;
	    		 
	    		 //Maybe the right idea
    			 //try_end_stmt = (Stmt) ch.getLast();
    			 try_end_stmt = (Stmt) ch.getSuccOf(stmt);
    			 
    			 
    			 List<Stmt> probe = new ArrayList<Stmt>();
    	    	 SootClass thrwCls = Scene.v().getSootClass("java.lang.ArithmeticException");
    	    	 Stmt sGotoLast = Jimple.v().newGotoStmt(try_end_stmt);
    	    	 probe.add(sGotoLast);
    	    	 
    	    	 //prepare for catch block
    	    	 Local lException1 = UtilInstrum.getCreateLocal(jbody, "<ex2>", RefType.v(thrwCls));
    	    	 Stmt sCatch = Jimple.v().newIdentityStmt(lException1, Jimple.v().newCaughtExceptionRef());
    	    	 probe.add(sCatch);
    	    	 
    	    	 
    	    	 //stmt for catch block
    	    	 AssignStmt oneAssign = Jimple.v().newAssignStmt(lhs_to_patched, IntConstant.v(1));
	    		 probe.add(oneAssign);
	    		 
	    		 InstrumManager.v().insertRightBeforeNoRedirect(ch, probe, try_end_stmt);
	    		 //instr
	    		 jbody.getTraps().add(Jimple.v().newTrap(thrwCls, try_start_stmt, sGotoLast, sCatch));
    	    	 jbody.validate();
	    		 
	    		 //System.out.println(oneAssign);
	    		 	    		 
	    	 }
	    	 
	    	 if(flag == 3)
	    		 System.out.println("#### No Such Elemet Found Exception may happen####");

	    	 if(flag == 4)
	    	 {
	    		 System.out.println("#### Negative array size exception may happen####");
	    		 
	    		//get local///////////////////////////////
	    		 Value v_temp = getIndexFromArrayExpr(unit);
	  
	    		 Value v_array = getArrayFromArrayExpr(unit);
	    		 Local local_array = (string_localmap.containsKey(v_array.toString())) ? string_localmap.get(v_array.toString()) : null;
	    		 
	    		 Type array_type = local_array.getType();
	    		 System.out.println("$$$ " + array_type);
	    		 
	    		 Local index_to_patched = string_localmap.containsKey(v_temp.toString()) ? string_localmap.get(v_temp.toString()) : null;
	    		 
	    		 //if(string_localmap.containsKey(v_temp.toString()))
	    		 //	index_to_patched = string_localmap.get(v_temp.toString());	    		 	    		 
	    		 
	    		//instrument try catch
	    		 try_start_stmt = stmt;
	    		 
	    		 //take the next one -----> Question??
    			 try_end_stmt = (Stmt) ch.getSuccOf(stmt);
    			 
    			 List<Stmt> probe = new ArrayList<Stmt>();
    	    	 SootClass thrwCls = Scene.v().getSootClass("java.lang.NegativeArraySizeException");
    	    	 Stmt sGotoLast = Jimple.v().newGotoStmt(try_end_stmt);
    	    	 probe.add(sGotoLast);
    	    	 
    	    	 //prepare for catch block
    	    	 Local lException1 = UtilInstrum.getCreateLocal(jbody, "<ex3>", RefType.v(thrwCls));
    	    	 Stmt sCatch = Jimple.v().newIdentityStmt(lException1, Jimple.v().newCaughtExceptionRef());
    	    	 probe.add(sCatch);
    	    	 
    	    	 
    	    	 //stmt for catch block
    	    	 //assign the index val
    	    	 AssignStmt oneAssign_1 = Jimple.v().newAssignStmt(index_to_patched, IntConstant.v(1));
	    		 probe.add(oneAssign_1);
	    		 //array reassign
	    		 Local arg = null; 
	    		 if(array_type.toString().contains("int"))
	    			 arg = Jimple.v().newLocal("<NewArr>", ArrayType.v(IntType.v(), 1));
	    		 
	    		 //System.out.println(arg);

	    		 
	    		 InstrumManager.v().insertRightBeforeNoRedirect(ch, probe, try_end_stmt);
	    		 //instr
	    		 jbody.getTraps().add(Jimple.v().newTrap(thrwCls, try_start_stmt, sGotoLast, sCatch));
    	    	 jbody.validate();
	    		 
	    		 
	    		 
	    		 
	    		 //System.out.println("!!! "+index_to_patched.toString());	    		 	    		 
	    	 }
	    	 //System.out.println(unit+" : "+s);
	     }
	     
	     /*
	     if( (try_end_stmt != null && try_start_stmt != null) || fg )
	     {
	    	 List<Stmt> probe = new ArrayList<Stmt>();
	    	 SootClass thrwCls = Scene.v().getSootClass("java.lang.IndexOutOfBoundsException");
	    	 Stmt sGotoLast = Jimple.v().newGotoStmt(try_end_stmt);
	    	 probe.add(sGotoLast);
		 
	    	 Local lException1 = UtilInstrum.getCreateLocal(jbody, "<ex1>", RefType.v(thrwCls));
	    	 Stmt sCatch = Jimple.v().newIdentityStmt(lException1, Jimple.v().newCaughtExceptionRef());
	    	 probe.add(sCatch);
		 
	    	 //stmt for catch block
	    	 
	    	 
	    	 
	    	 /////////////////////////////////////////////////////
	    	 InstrumManager.v().insertRightBeforeNoRedirect(ch, probe, try_end_stmt);
			
	    	 // insert trap (catch)
	    	 jbody.getTraps().add(Jimple.v().newTrap(thrwCls, try_start_stmt, sGotoLast, sCatch));
	    	 jbody.validate();
	     }
	     */
	     
	}
	     //System.out.println(ch.toString());

}


