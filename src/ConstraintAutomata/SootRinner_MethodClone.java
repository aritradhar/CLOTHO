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
import soot.options.Options;
     
       public class SootRinner_MethodClone 
       {
         public static void main(String[] args) {
     
          
          String []className = {"Test2"};
          SootClass currentClass = Scene.v().loadClassAndSupport(className[0]);
          
          /* add a phase to transformer pack by call Pack.add */
          Pack jtp = PackManager.v().getPack("jtp");

          jtp.add(new Transform("jtp.instrumenter", new MethodClone(currentClass)));
          Options.v().setPhaseOption("jb", "use-original-names:true");
          soot.Main.main(className);   
          
        }
     }