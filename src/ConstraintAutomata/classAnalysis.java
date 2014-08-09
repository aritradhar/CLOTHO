package ConstraintAutomata;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.*;
import soot.grimp.internal.GEqExpr;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.*;
import soot.jimple.internal.JAndExpr;
import soot.util.*;

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


public class classAnalysis extends BodyTransformer
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
	     Chain<Unit> ch=jbody.getUnits();
	     
	     Iterator<Unit> it = ch.snapshotIterator();
	     String str = "";
	     
	 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
	     while(it.hasNext())
	     {
	    	 
	    	 Unit unit = it.next();
	    	 Stmt stmt = (Stmt)unit;
	    	 
	    	 System.out.println(unit);
	    	 
	    	 List usedefbox = unit.getUseAndDefBoxes();
	    	 //System.out.println(usedefbox);
	    	 
	    	 
	    	 //System.out.println(s);
	    	 Iterator it_box  = usedefbox.iterator();
	    	 int flag=0;
	    	 
	    	 if(unit.toString().contains("@parameter") || unit.toString().contains("newarray"))
	    		 continue;
	    	 //handle non-Invoke expressions
	    	 if (!stmt.containsInvokeExpr())
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
	    				 flag = 2;
	    	    
	    			 //System.out.print(str + ":::");
	    		 }
	    	 }
	    	 //handle invoke expressions
	    	 else
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
	    			 String index_variable_name = it_use_def.next().toString();
	    			 String base_array_variable_name = it_use_def.next().toString();
	    			 
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
	    			 if(string_localmap.containsKey(index_variable_name))
	    				 index_local = string_localmap.get(index_variable_name);
	    			 /////////////////////////////////////////////////////////////////////////////////////////
	    			 if(string_localmap.containsKey(base_array_variable_name))
	    				 base_array_local = string_localmap.get(base_array_variable_name);
	    			 ////////////////////////////////////////////////////////////////////////////////////////
	    		 }
	    		 
	    		 
	    		 
	    		 if(t1 && t2) 
	    		 {
	    			 
	    			 //craft the part where the invariant should satisfy the upper bound
	    			 SootMethod string_length_method = Scene.v().getMethod("<java.lang.String: int length()>");
	    			 //System.out.println(string_length.toString());
	    			 VirtualInvokeExpr s_len_check_expr = Jimple.v().newVirtualInvokeExpr(base_array_local, string_length_method.makeRef());
	    			 
	    			 /*
	    			 Local array_length_left_local = Jimple.v().newLocal("_array_len_auto_gen", IntType.v());
	    			 Chain locals_t = jbody.getLocals();
	    			 locals_t.add(array_length_left_local);
	    			 */
	    			 Local array_length_left_local = generateNewLocal(jbody, IntType.v());
	    			 
	    			 LeExpr leExpr_ = Jimple.v().newLeExpr(index_local, array_length_left_local);
	    			 
	    			 
	    			 AssignStmt string_length_stmt = Jimple.v().newAssignStmt(array_length_left_local, s_len_check_expr);
	    			 jbody.getUnits().insertBefore(string_length_stmt, unit);
	    			 
	    			 //LeExpr leExpr = Jimple.v().newLeExpr(index_local, negOne);
	    			 
	    			 //jbody.getUnits().insertBefore(s_len_check_expr,unit);
	    			 
	    			 //test/////////////////////////////////////////////////////////////////////////
	    			 //System.out.println(":::"+ index_local);
	    			 //System.out.println(":::"+ base_array_local);
	    			 ///////////////////////////////////////////////////////////////////////////////
	    			 
	    			 List<Unit> generated = new ArrayList<Unit>();
	    			 //Type booleanType = BooleanType.v();
	    		 
	    			 //Local localBoolean = generateNewLocal(jbody, booleanType);
	    		 
	    			//generate condition for lower bound
	    			 IntConstant negOne = IntConstant.v(-1);
	    			 
	    			 //LeExpr equalExpr = Jimple.v().newLeExpr(localBoolean, negOne);
	    			 //push the index val to the if condition
	    			 LeExpr leExpr = Jimple.v().newLeExpr(index_local, negOne);
	    			 
	    			 NopStmt nop = insertNopStmt(jbody, unit);
	    			 
	    			 //AndExpr jand = Jimple.v().newAndExpr(leExpr, leExpr_);
	    			 		 
	    			 IfStmt ifStmt = Jimple.v().newIfStmt(leExpr, nop);
	    			 //IfStmt ifStmt = Jimple.v().newIfStmt(jand, nop);
	    			 
	    			 generated.add(ifStmt);
	    		 
	    			 jbody.getUnits().insertBefore(generated, unit);
	    		 }
	    	 }
	    	 
	    	 if (flag == 2)
	    	 {
	    		 System.out.println("@@@@"+stmt.toString());
	    		 System.out.println("####Divide by Zero Exception may happen ####");
	    	 }
	    	 
	    	 if(flag == 3)
	    		 System.out.println("#### No Such Elemet Found Exception may happen####");

	    	 //System.out.println(unit+" : "+s);
	     }
	}
	     //System.out.println(ch.toString());
}

