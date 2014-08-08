package TestPack;

 import soot.*;
import soot.jimple.*;
import soot.util.*;

import java.util.*;
    
      public class InvokeStaticInstrumenter extends BodyTransformer{
    
        /* some internal fields */
        static SootClass counterClass;
        static SootMethod increaseCounter, reportCounter;
        static SootMethod newMethodAD;
    
        static {
          counterClass    = Scene.v().loadClassAndSupport("MyCounter");
          increaseCounter = counterClass.getMethod("void increase(int)");
          reportCounter   = counterClass.getMethod("void report()");
          newMethodAD     =counterClass.getMethod("void methodAD()");
        }
    
        /* internalTransform goes through a method body and inserts
         * counter instructions before an INVOKESTATIC instruction
         * 
         */
        public static void statementprov()
        {
        	SootClass sc=Scene.v().loadClassAndSupport("MyCounter");
        	SootMethod sm=sc.getMethod("void methodAD()");
        	JimpleBody jb=(JimpleBody) sm.getActiveBody();
        	Chain<Unit> ch=jb.getUnits();
        	System.out.println(ch.toString());
        }
        
        protected void internalTransform(Body body, String phase, Map options) 
        {
        	//System.out.println(body);
          // body's method
        	//System.out.println("-------------calling-----------------");
        	//statementprov();
          SootMethod method = body.getMethod();
    
          // debugging
          System.out.println("instrumenting method : " + method.getSignature());
    
          // get body's unit as a chain
          Chain units = body.getUnits();
    
          // get a snapshot iterator of the unit since we are going to
          // mutate the chain when iterating over it.
          //
          Iterator stmtIt = units.snapshotIterator();
    
          // typical while loop for iterating over each statement
          while (stmtIt.hasNext()) {
    
            // cast back to a statement.
            Stmt stmt = (Stmt)stmtIt.next();
    
            // there are many kinds of statements, here we are only
            // interested in statements containing InvokeStatic
            // NOTE: there are two kinds of statements may contain
            //       invoke expression: InvokeStmt, and AssignStmt
            if (!stmt.containsInvokeExpr()) {
              //System.out.println(stmt.toString());
            	continue;
            }
            

    
            // take out the invoke expression
            InvokeExpr expr = (InvokeExpr)stmt.getInvokeExpr();
    
            // now skip non-static invocations
            if (! (expr instanceof StaticInvokeExpr)) {
              continue;
            }
    
            // now we reach the real instruction
            // call Chain.insertBefore() to insert instructions
            //
            // 1. first, make a new invoke expression
            InvokeExpr incExpr= Jimple.v().newStaticInvokeExpr(increaseCounter.makeRef(),
                                                        IntConstant.v(1));
            // 2. then, make a invoke statement
            Stmt incStmt = Jimple.v().newInvokeStmt(incExpr);
    
            // 3. insert new statement into the chain
            //    (we are mutating the unit chain).
            units.insertBefore(incStmt, stmt);
          }
    
    
          // Do not forget to insert instructions to report the counter
          // this only happens before the exit points of main method.
    
         // 1. check if this is the main method by checking signature
         String signature = method.getSubSignature();
         boolean isMain = signature.equals("void main(java.lang.String[])");
   
         // re-iterate the body to look for return statement
         if (isMain) {
           stmtIt = units.snapshotIterator();
   
           while (stmtIt.hasNext()) {
             Stmt stmt = (Stmt)stmtIt.next();
   
             // check if the instruction is a return with/without value
             if ((stmt instanceof ReturnStmt)
                 ||(stmt instanceof ReturnVoidStmt)) {
            	 //System.out.println("RETURN @@ "+ stmt);
               // 1. make invoke expression of MyCounter.report()
               InvokeExpr reportExpr= Jimple.v().newStaticInvokeExpr(reportCounter.makeRef());
               //add for test
               InvokeExpr methodAD=Jimple.v().newStaticInvokeExpr(newMethodAD.makeRef());
   
               // 2. then, make a invoke statement
               Stmt reportStmt = Jimple.v().newInvokeStmt(reportExpr);
               Stmt ADstmt=Jimple.v().newInvokeStmt(methodAD);
   
               // 3. insert new statement into the chain
               //    (we are mutating the unit chain).
               units.insertBefore(reportStmt, stmt);
               units.insertBefore(ADstmt, stmt);
             }
           }
         }
       }
     }
