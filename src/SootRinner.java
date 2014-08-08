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
     
       public class SootRinner {
         public static void main(String[] args) {
     
          /* check the arguments */
          if (args.length == 0) {
            System.err.println("Usage: java MainDriver [options] classname");
            System.exit(0);
          }
    
          /* add a phase to transformer pack by call Pack.add */
          Pack jtp = PackManager.v().getPack("jtp");
          //jtp.add(new Transform("jtp.instrumenter", new VariableMonitor()));
    
          jtp.add(new Transform("jtp.instrumenter", new dataDependency()));
          /* Give control to Soot to process all options,
           * InvokeStaticInstrumenter.internalTransform will get called.
           */
          
          Scene.v().addBasicClass("java.util.ArrayList",SootClass.SIGNATURES);
          soot.Main.main(args);
          
        }
     }