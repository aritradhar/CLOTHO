package ConstraintAutomata;
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

import soot.*;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;
     
       public class SootRinner {
         public static void main(String[] args) {
     
          /* check the arguments */
          /*if (args.length == 0) {
            System.err.println("Usage: java MainDriver [options] classname");
            System.exit(0);
          }*/
          
          String []className = {"Test1"};
    
          /* add a phase to transformer pack by call Pack.add */
          Pack jtp = PackManager.v().getPack("jtp");
          //jtp.add(new Transform("jtp.instrumenter", new VariableMonitor()));
    
          jtp.add(new Transform("jtp.instrumenter", new classAnalysis_TryCatch()));
          //jtp.add(new Transform("jtp.instrumenter", new ArrayLookAhead()));
          //jtp.add(new Transform("jtp.instrumenter", new NullPointerArraylen()));
          
          /* Give control to Soot to process all options,
           * InvokeStaticInstrumenter.internalTransform will get called.
           */
          
          //Scene.v().addBasicClass("java.util.ArrayList",SootClass.SIGNATURES);
          //Scene.v().addBasicClass("java.util.NoSuchElementException",SootClass.SIGNATURES);
          
          Options.v().setPhaseOption("jb", "use-original-names:true");
          soot.Main.main(className);
          
          
          
        }
     }