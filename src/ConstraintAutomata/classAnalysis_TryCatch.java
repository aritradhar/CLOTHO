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
import soot.jimple.internal.JNewArrayExpr;
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
	
	
	
	public Local getDenomFromDivision(Unit unit, HashMap<String, Local> string_localmap)
	{
		 Iterator<ValueBox> vbox_it = unit.getUseAndDefBoxes().iterator();
		 
		 Local denom = null;
		 
		 while(vbox_it.hasNext())
		 {

			 ValueBox temp = vbox_it.next();
			 String temp_s = temp.toString();
			 
			 if(temp_s.contains("LinkedRValueBox"))
			 {
				 // blank space after division operator 
				 temp_s = temp_s.substring(temp_s.indexOf("/")+2, temp_s.indexOf(")")); 
				 denom = (string_localmap.containsKey(temp_s)) ? string_localmap.get(temp_s) : null;
			 }
		 }
		 
		 return denom;
	}
	
	
	public Local getNumorFromDivision(Unit unit, HashMap<String, Local> string_localmap)
	{
		Iterator<ValueBox> vbox_it = unit.getUseAndDefBoxes().iterator();
		 Local numar = null;
		 while(vbox_it.hasNext())
		 {
			 ValueBox temp = vbox_it.next();
			 String temp_s = temp.toString();
			 if(temp_s.contains("LinkedRValueBox"))
			 {
				 // blank space after division operator 
				 temp_s = temp_s.substring(temp_s.indexOf("(") + 1, temp_s.indexOf("/") - 1); 
				 numar = (string_localmap.containsKey(temp_s)) ? string_localmap.get(temp_s) : null;
			 }
		 }
		 
		 return numar;
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
	
	public Type getTypeFromString(String typeStr)
	{
		Type T = null;
		
		switch(typeStr)
		{
			case "int" : T = IntType.v();
			break;
			case "float" : T = FloatType.v();
			break;
			case "double" : T = DoubleType.v();
			break;
			case "byte" : T = ByteType.v();
			break;
			case "short" : T = ShortType.v();
			break;
			case "long" : T = LongType.v();
			break;
			case "char" : T = CharType.v();
			break;
		
			default : T = RefType.v(typeStr);
		}
		
		return T;
	}
	
	public Local getNewArray(Type type, int ARRAY_SIZE, Body jbody)
	{
		Local arg = null;
		String stype = type.toString();
		
		//handle basic types
		if(stype.equals("int[]"))
			 arg = generateNewLocal(jbody, ArrayType.v(IntType.v(), ARRAY_SIZE));

		else if(stype.equals("float[]"))
			arg = generateNewLocal(jbody, ArrayType.v(FloatType.v(), ARRAY_SIZE));
		
		else if(stype.equals("double[]"))
			arg = generateNewLocal(jbody, ArrayType.v(DoubleType.v(), ARRAY_SIZE));
		
		else if(stype.equals("char[]"))
			arg = generateNewLocal(jbody, ArrayType.v(CharType.v(), ARRAY_SIZE));
		
		else if(stype.equals("long[]"))
			arg = generateNewLocal(jbody, ArrayType.v(LongType.v(), ARRAY_SIZE));
		
		else if(stype.equals("short[]"))
			arg = generateNewLocal(jbody, ArrayType.v(ShortType.v(), ARRAY_SIZE));
		
		else if(stype.equals("byte[]"))
			arg = generateNewLocal(jbody, ArrayType.v(ByteType.v(), ARRAY_SIZE));
		
		else if(stype.equals("boolean[]"))
			arg = generateNewLocal(jbody, ArrayType.v(BooleanType.v(), ARRAY_SIZE));
		
		//else resolve from class
		else
		{
			String stype_t = stype.substring(0,stype.indexOf('['));
			//System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%"+stype_t);
			arg = generateNewLocal(jbody, ArrayType.v(RefType.v(stype_t), ARRAY_SIZE));
		}
		return arg;
	}
	
	/*
	 * Returns JNewArrayExpr (var = newarray (type) [SIZE])
	 * 
	 * Type is resolved by separating primitive type or custom class
	 * 
	 */
	public JNewArrayExpr getNewArrayExpr(Type type, Value ARRAY_SIZE)
	{
		JNewArrayExpr arg = null;
		String stype = type.toString();
		
		//handle basic types
		if(stype.equals("int[]"))
			 arg = new JNewArrayExpr(IntType.v(), ARRAY_SIZE);

		else if(stype.equals("float[]"))
			arg = new JNewArrayExpr(FloatType.v(), ARRAY_SIZE);
		
		else if(stype.equals("double[]"))
			arg = new JNewArrayExpr(DoubleType.v(), ARRAY_SIZE);
		
		else if(stype.equals("char[]"))
			arg = new JNewArrayExpr(CharType.v(), ARRAY_SIZE);
		
		else if(stype.equals("long[]"))
			arg = new JNewArrayExpr(LongType.v(), ARRAY_SIZE);
		
		else if(stype.equals("short[]"))
			arg = new JNewArrayExpr(ShortType.v(), ARRAY_SIZE);
		
		else if(stype.equals("byte[]"))
			arg = new JNewArrayExpr(ByteType.v(), ARRAY_SIZE);
		
		else if(stype.equals("boolean[]"))
			arg = new JNewArrayExpr(BooleanType.v(), ARRAY_SIZE);
		
		//else resolve from class
		else
		{
			String stype_t = stype.substring(0,stype.indexOf('['));
			//System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%"+stype_t);
			arg = new JNewArrayExpr(RefType.v(stype_t), ARRAY_SIZE);
		}
		return arg;
	}
	
	
	/*
	 * retuns in form | lhslocl | rhs local | cast type Type | isCast|
	 * isCast is a boolean entry to check if it is a cast statement or no
	 * This is to ensure safety.
	 * Output in terms of array of Objects as I need to keep different type
	 * 
	 * Exact type will be handled by type resolution method
	 * getTypeFromString()
	 */
	
	public Object[] processCastStamt(Unit unit, HashMap<String, Local> string_localmap)
	{
		 Object []output = new Object[4];
		
		 Boolean isCast = false;
		 
		 Iterator<ValueBox> itv =unit.getUseAndDefBoxes().iterator();
		 HashMap<String, Value> valbox_mp = new HashMap<>();
		 
		 while(itv.hasNext())
		 {
			 ValueBox vBox = itv.next();
			 String strBox = vBox.toString();
			 //System.out.println(strBox);
			 
			 valbox_mp.put(strBox.substring(0,strBox.indexOf("(")), vBox.getValue());
			 
			 if(strBox.contains("LinkedVariableBox"))
			 {
				 Value lhs= vBox.getValue();
				 Local lhs_local = (string_localmap.containsKey(lhs.toString())) ? string_localmap.get(lhs.toString()) : null;
				 output[0] = lhs_local;
			 }
			 
			 if(strBox.contains("LinkedRValueBox"))
			 {
				 Value castValue= vBox.getValue();
				 String []str_values = vBox.getValue().toString().split(" ");
				 
				 boolean potentialCast = (str_values.length == 2) ? true : false;
				 
				 if(potentialCast && str_values[0].contains("(") && str_values[0].contains(")"))
				 {
					 String castClassStr = str_values[0].replace("(", "").replace(")", "");
					 
					 /* may cause problem as it may be a primitive type
					  * better to handle it on the fly
					  * SootClass castClass = Scene.v().getSootClass(castClassStr);
	    			  * rhs i.e. the casted object
	    			  */
					 
					 String castedLocalString = str_values[1];
					 //safe check
					 Local castedLocal = (string_localmap.containsKey(castedLocalString)) ? string_localmap.get(castedLocalString) : null; 
					 
					 
					 //safely assume that the statement is a cast statement
					 isCast = true;
					 
					 output[1] = castedLocal;
					 Type classCastType = getTypeFromString(castClassStr);
					 output[2] = classCastType;
					 output[3] = isCast;
					 //System.out.println(castClass);
					 
				 }
			 }
		 }
		
		return output;
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
	     
	     /*
	      * flag description
	      * flag = 99 don't process
	      * flag = 1 array index
	      * flag = 2 division
	      * flag = 3 no such element
	      * flag = 4 negative sized array
	      * flag = 5 class cast   
	      */
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
	    	 
	    	 if(stmt instanceof AssignStmt)
	    	 {
				Object []out = processCastStamt(unit, string_localmap);
	    		
				boolean isCast = (boolean)out[3];
	    		
				//TODO
				if(isCast)
	    			flag = 5; 
	    	 }
	    	 
	    	 //check for class casting operation in the statements
	    		 
	    	 
	    	 
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
	    			 if(expMethod.getName().toString().equals("next") || expMethod.getName().toString().equals("nextLine"))// && expMethod.getDeclaringClass().toString().equals("java.util.Iterator"))
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
	    	 
	    	 
	    	 boolean divisionOption = false;
	    	 
	    	 if (flag == 2 && !divisionOption)
	    	 {
	    		 System.out.println("#### Divide by Zero Exception may happen ####");
	    		 
	    		 Value v = getLeftHandFromDivision(unit);
	    		 Local l1 =(Local) v;
	    		 Local lhs = string_localmap.containsKey(l1.toString()) ? string_localmap.get(l1.toString()) : null;
	    		 
	    		 Local denom_toPatch = getDenomFromDivision(unit, string_localmap);
	    		 
	    		 Local numor = getNumorFromDivision(unit, string_localmap);
	    		 
	    		 //make the try-catch probe
	    		 try_start_stmt = stmt;
    			 try_end_stmt = (Stmt) ch.getSuccOf(stmt);
    			 
    			 
    			 List<Stmt> probe = new ArrayList<Stmt>();
    	    	 SootClass thrwCls = Scene.v().getSootClass("java.lang.ArithmeticException");
    	    	 Stmt sGotoLast = Jimple.v().newGotoStmt(try_end_stmt);
    	    	 probe.add(sGotoLast);
    	    	 
    	    	 //prepare for catch block
    	    	 Local lException1 = UtilInstrum.getCreateLocal(jbody, "<ex2>", RefType.v(thrwCls));
    	    	 Stmt sCatch = Jimple.v().newIdentityStmt(lException1, Jimple.v().newCaughtExceptionRef());
    	    	 probe.add(sCatch);
    	    	 
    	    	 AssignStmt oneAssign = Jimple.v().newAssignStmt(denom_toPatch, IntConstant.v(1));
	    		 probe.add(oneAssign);
	    		 DivExpr divExpr = Jimple.v().newDivExpr(numor, denom_toPatch);
	    		 AssignStmt divAssign = Jimple.v().newAssignStmt(lhs, divExpr);
	    		 probe.add(divAssign);
	    		 
	    		 InstrumManager.v().insertRightBeforeNoRedirect(ch, probe, try_end_stmt);
	    		 //instr
	    		 jbody.getTraps().add(Jimple.v().newTrap(thrwCls, try_start_stmt, sGotoLast, sCatch));
    	    	 jbody.validate();
	    		 
	    	 }
	    	 
	    	 if (flag == 2 && divisionOption)
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
	    	 {
	    		 System.out.println("#### No Such Elemet Found Exception may happen####");
	    		 
	    		 try_start_stmt = stmt;
    			 try_end_stmt = (Stmt) ch.getSuccOf(stmt);
    			 
    			 List<Stmt> probe = new ArrayList<Stmt>();
    	    	 SootClass thrwCls = Scene.v().getSootClass("java.lang.ArithmeticException");
    	    	 Stmt sGotoLast = Jimple.v().newGotoStmt(try_end_stmt);
    	    	 probe.add(sGotoLast);
	    		 
	    	 }
	    	 if(flag == 4)
	    	 {
	    		 System.out.println("#### Negative array size exception may happen####");
	    		 
	    		 
	    		//get local///////////////////////////////
	    		 Value v_temp = getIndexFromArrayExpr(unit);
	  
	    		 //local array element to be pased in the catch probe
	    		 Value v_array = getArrayFromArrayExpr(unit);
	    		 Local local_array = (string_localmap.containsKey(v_array.toString())) ? string_localmap.get(v_array.toString()) : null;
	    		 
	    		 //array type to be passed in the catch probe
	    		 Type array_type = local_array.getType();
	    		 //System.out.println("$$$ " + array_type);
	    		 
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
	    		 Value ARRAY_SIZE = IntConstant.v(1); //hard-coded array size
	    		 
	    		 NewArrayExpr arg = getNewArrayExpr(array_type, ARRAY_SIZE); 
	    		 
	    		 AssignStmt array_Assign = Jimple.v().newAssignStmt(local_array, arg);
	    		 probe.add(array_Assign);
	    		 
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


