package TestPack;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import soot.*;
import soot.jimple.*;
import soot.util.*;

public class classAnalysis 
{	 
	 public static void main(String []a)
	 {
		 // this is to analyze potential ArrayIndexOutofBoundException
		 SootClass sc=Scene.v().loadClassAndSupport("RuntimeExceptions");
		 Scene.v().loadNecessaryClasses();		 
	     SootMethod smethod=sc.getMethodByName("main");
	     JimpleBody jbody= (JimpleBody) smethod.retrieveActiveBody();
	     Chain<Local> l = jbody.getLocals();
	     Iterator<Local> it_local = l.iterator();
	     
	     HashMap<String, String> localmap = new HashMap<>();
	     while(it_local.hasNext())
	     {
	    	 Local loc = it_local.next();
	    	 localmap.put(loc.toString(), loc.getType().toString());
	    	 System.out.println(loc+" : " + loc.getType());
	     }
	     
	     System.out.println("----------------------------------------------------------");
	     Chain<Unit> ch=jbody.getUnits();
	     
	     Iterator<Unit> it = ch.iterator();
	     String str = "";
	     while(it.hasNext())
	     {
	    	 Unit unit = it.next();
	    	 Stmt stmt = (Stmt)unit;
	    	 System.out.println(unit);
	    	 List usedefbox = unit.getUseAndDefBoxes();
	    	// System.out.println(usedefbox);
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
	    	 if (flag == 1)
	    		 System.out.println("#### Array Index Out of Bound/ -ve index May occur ####");
	    	 if (flag == 2)
	    		 System.out.println("#### Divide by Zero Exception may happen ####");
	    	 if(flag == 3)
	    		 System.out.println("#### No Such Elemet Found Exception may happen####");

	    	 //System.out.println(unit+" : "+s);
	     }
	     //System.out.println(ch.toString());
	 }
}
