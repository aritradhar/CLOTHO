package ConstraintAutomata;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
		
		System.out.println("^^^^" + nextStmt.toString());
		return nextStmt;
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
	    			 try_end_stmt = (Stmt) ch.getLast();
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
	    	    	 SootMethod string_length_method = Scene.v().getMethod("<java.lang.String: int length()>");
	    			 //System.out.println(string_length.toString());
	    			 VirtualInvokeExpr s_len_check_expr = Jimple.v().newVirtualInvokeExpr(base_array_local, string_length_method.makeRef());
	    			 //System.out.println("::"+s_len_check_expr.toString());	    			 
	    			 
	    			 //Local array_length_left_local = generateNewLocal(jbody, IntType.v());
	    			
	    			 Local array_length_left_local = UtilInstrum.getCreateLocal(jbody, "<len_temp>", IntType.v());
	    			 
	    			 AssignStmt patch_assign = Jimple.v().newAssignStmt(array_length_left_local, s_len_check_expr);
	    			 System.out.println(patch_assign);
	    	    	 
	    			 //probe.add(patch_assign);
	    			 
	    			 AddExpr normalize_expr = Jimple.v().newAddExpr(array_length_left_local, IntConstant.v(-1));
	    			 AssignStmt normalize_assign = Jimple.v().newAssignStmt(array_length_left_local, normalize_expr);
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
	    		 System.out.println(stmt.toString());
	    		 System.out.println("#### Divide by Zero Exception may happen ####");
	    	 }
	    	 
	    	 if(flag == 3)
	    		 System.out.println("#### No Such Elemet Found Exception may happen####");

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

