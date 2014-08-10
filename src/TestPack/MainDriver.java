package TestPack;

import soot.*;
     
       public class MainDriver {
         public static void main(String[] args) {
     
          /* check the arguments */
          if (args.length == 0) {
            System.err.println("Usage: java MainDriver [options] classname");
            System.exit(0);
          }
    
          /* add a phase to transformer pack by call Pack.add */
          Pack jtp = PackManager.v().getPack("jtp");
          //jtp.add(new Transform("jtp.instrumenter",new InvokeStaticInstrumenter()));
    
          //Scene.v().addBasicClass("java.lang.Object",SootClass.SIGNATURES);
          //Scene.v().addBasicClass("java.lang.Integer$IntegerCache", SootClass.SIGNATURES);
          //Scene.v().addBasicClass("java.lang.System", SootClass.SIGNATURES);
          
          jtp.add(new Transform("jtp.instrumenter",new FieldInstr()));
          
          
          /* Give control to Soot to process all options,
           * InvokeStaticInstrumenter.internalTransform will get called.
           */
          soot.Main.main(args);
        }
     }