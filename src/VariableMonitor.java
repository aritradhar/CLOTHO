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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import polyglot.ast.Instanceof;
import soot.Body;
import soot.BodyTransformer;
import soot.BooleanType;
import soot.Local;
import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.JastAddJ.AssignExpr;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.AssignStmt;
import soot.jimple.GotoStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NewExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.internal.JimpleLocalBox;
import soot.util.Chain;


public class VariableMonitor extends BodyTransformer
{
	public Local generateNewLocal(Body body , Type type)
	{
		LocalGenerator lg = new LocalGenerator(body);
		return lg.generateLocal(type);
    }
	
	public static Local findKey(String st)
	{
		Iterator<Local> it_localkey = localMap.keySet().iterator();
		while(it_localkey.hasNext())
		{
			Local nextLocal = it_localkey.next();
			if(nextLocal.toString().equals(st))
				return nextLocal;
		}
		return null;
	}
	
	public static Type findValue(String st)
	{
		Iterator<Local> it_localkey = localMap.keySet().iterator();
		while(it_localkey.hasNext())
		{
			Local nextLocal = it_localkey.next();
			if(nextLocal.toString().equals(st))
				return localMap.get(nextLocal);
		}
		return null;
	}
	
	static SootClass instclass;
	static SootMethod instmethod, instmethod1,instmethod_print;
	static HashMap<Local, Type> localMap = new HashMap<>();
	static HashMap<String, Type> localMap_string = new HashMap<>();
	
	 static 
	 {
		 instclass = Scene.v().loadClassAndSupport("Instrumented");
		 instmethod = instclass.getMethod("void inst()");
		 instmethod1 = instclass.getMethodByName("addToList");
		 instmethod_print = instclass.getMethodByName("printList");
	 }
	 
	protected void internalTransform(Body body, String phase, Map options) 
	{
		SootMethod method = body.getMethod();
        System.out.println("instrumenting method : " + method.getSignature());
        Chain units = body.getUnits();	
        
        String signature = method.getSubSignature();
        boolean isMain = signature.equals("void main(java.lang.String[])");
        
        
        if(isMain)
        {
        	Chain<Local> localchain = body.getLocals();
        	Iterator<Local> it_local = localchain.iterator();
        
        
        	while(it_local.hasNext())
        	{
        		Local temp_local = it_local.next();
        		Type local_type = temp_local.getType();
        		localMap.put(temp_local, local_type);
        		localMap_string.put(temp_local.toString(), local_type);
        	}
        
        	Iterator<Local> it_localMap = localMap.keySet().iterator();
        	while(it_localMap.hasNext())
        	{
        		Local loc = it_localMap.next();
        		System.out.println(loc + "::"+localMap.get(loc)+" : "+loc.getNumber());
        	}
        }
        
        //System.out.println("$$$$"+body.getMethod());
        if(isMain)
        {
        	//add static field
        	//System.out.println("$$$$"+body.getMethod());
			//SootField sf = new SootField("variablList_Field",RefType.v("java.util.ArrayList"), Modifier.STATIC);
			//Scene.v().getMainClass().addField(sf);			
			
			NewExpr newExpr = Jimple.v().newNewExpr(RefType.v("java.util.ArrayList"));			
			Local l = Jimple.v().newLocal("variablList",RefType.v("java.util.ArrayList"));
			body.getLocals().add(l);
			SootMethodRef ref = Scene.v().getSootClass("java.util.ArrayList").getMethod("<init>",Collections.emptyList()).makeRef();
			InvokeExpr expr = Jimple.v().newSpecialInvokeExpr(l,ref);
			Stmt invoke = Jimple.v().newInvokeStmt(expr);						
			AssignStmt assign = Jimple.v().newAssignStmt(l,newExpr);
			//body.getUnits().add(assign);
			//System.out.println("$$$$$"+assign.toString());
			
			//Here making a local for the add call to the ArrayList
			Local fieldLocal = Jimple.v().newLocal("variableMap"+body.getMethod().getName(), RefType.v());
			Local l1 = generateNewLocal(body , RefType.v("java.util.List"));
			 //System.out.println("############"+l1+" "+l1.getType());			
			
			SootMethod ArrayListAdd = Scene.v().getSootClass("java.util.ArrayList").getMethod("boolean add(java.lang.Object)");			
		
			//body.getUnits().add(invoke);			
			//System.out.println("$$$$$"+expr.toString());
			//SootMethodRef add_ref = Scene.v().getSootClass("java.util.ArrayList").getMethod("boolean add",Collections.emptyList()).makeRef();			
			
			//System.out.println("#########"+sf.getType());
			//Scene.v().loadClassAndSupport("java.util.ArrayList");
			SootClass arratList_soot = Scene.v().getSootClass("java.util.ArrayList");        	
			
        	Iterator stmtIt = units.snapshotIterator();
        	
        	
        	while (stmtIt.hasNext()) 
        	{
        		Stmt stmt = (Stmt)stmtIt.next();
        		if(stmt instanceof GotoStmt)
        			continue;
        		
        		if(stmt.toString().contains("@parameter0"))
        		{
        			//System.out.println("@@@@@@@@@@@@@@@@@@@@@");
        			units.insertAfter(assign, stmt);
        			units.insertAfter(invoke, assign);
        		}
        	   
        		//debug
        		System.out.println("----------------------------------\n"+stmt+"\n----------------------------------");
        		List list_UseDefBox = stmt.getUseAndDefBoxes();
        		//System.out.println(list_UseDefBox);
        		Iterator it_UseDefBox = list_UseDefBox.iterator();
        		
        		ArrayList<Local> localArray = new ArrayList<>();
        		while(it_UseDefBox.hasNext() && !stmt.toString().contains("@parameter0"))
        		{
        			Object box =it_UseDefBox.next();
        			//System.out.println("####"+box.toString());
        			if(box.toString().contains("LinkedRValueBox"))
        				continue;
        			if(box.toString().contains("IdentityRefBox"))
        				continue;
        			if(box.toString().contains("ConditionExprBox"))
        				continue;
        			
        			String var_st = box.toString().substring(box.toString().indexOf("(") + 1, box.toString().indexOf(")"));
        			
        			if(localMap_string.containsKey(var_st))
        			{
        				localArray.add(findKey(var_st));
        				      				
        				//invoke from the instrumented method
        				System.out.println(" "+findKey(var_st).toString());
        				InvokeExpr reportExpr1= Jimple.v().newStaticInvokeExpr(instmethod1.makeRef(),findKey(var_st));
           			 	Stmt staticInvokeStmt = Jimple.v().newInvokeStmt(reportExpr1);
           			 	
           			 	Local addL = findKey(var_st);
           			 	//body.getLocals().add(addL);
           			 	
           			 	InvokeExpr virtualInvokeexpr = Jimple.v().newVirtualInvokeExpr(l, ArrayListAdd.makeRef(), addL);
           			 	Stmt virtualInvokeStmt = Jimple.v().newInvokeStmt(virtualInvokeexpr);
           			 	
           			 	//This insert is only for testing purpose
           			 	InvokeExpr reportExpr2= Jimple.v().newStaticInvokeExpr(instmethod_print.makeRef());
        			 	Stmt reportStmt_printTest = Jimple.v().newInvokeStmt(reportExpr2);
           			 	//This call is not handled when running this class file as the object is not there in the stack
           			 	//------------------
        			 	//units.insertAfter(reportStmt_printTest, stmt);
           			 	//units.insertAfter(staticInvokeStmt, stmt);
           			 	units.insertAfter(virtualInvokeStmt, stmt);
        				//------------------
           			 	//debug
        				//System.out.println(findKey(var_st).toString()+"  :  "+findValue(var_st));
        			}
        			else
        			{
        				//System.out.println(var_st+"  : INVALID");
        			}        			
        			//if(box instanceof JimpleLocalBox)
        				//System.out.println(((JimpleLocalBox) box).getValue());					
        		}
        		//debug
        		//System.out.println("________________________________________________");
        	        		
        		if ((stmt instanceof ReturnStmt)||(stmt instanceof ReturnVoidStmt))
        		{
        			       			       			
        			 InvokeExpr reportExpr= Jimple.v().newStaticInvokeExpr(instmethod.makeRef());
        			 Stmt reportStmt = Jimple.v().newInvokeStmt(reportExpr);
        			 units.insertBefore(reportStmt, stmt);
        			 
        			 /*//Giving -ve stack problem

        			 InvokeExpr reportExpr1= Jimple.v().newStaticInvokeExpr(instmethod1.makeRef());
        			 Stmt reportStmt1 = Jimple.v().newInvokeStmt(reportExpr1); 
        			 units.insertBefore(reportStmt1, stmt);
        			 
        			 */
        		}
        	}        	
        	stmtIt = units.snapshotIterator();
        	        	
        	System.out.println("_______________________________________________________");
        	System.out.println("Instrumented body");
        	System.out.println("_______________________________________________________");
        	while (stmtIt.hasNext()) 
        	{   		
        		System.out.println(stmtIt.next());
        	}
        }
	}
}
