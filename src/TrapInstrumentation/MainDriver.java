package TrapInstrumentation;
import soot.*;
import soot.jimple.Jimple;
     
       public class MainDriver {
         public static void main(String[] args) {
     
          /* check the arguments */
          if (args.length == 0) {
            System.err.println("Usage: java MainDriver [options] classname");
            System.exit(0);
          }
    
          /* add a phase to transformer pack by call Pack.add */
          Pack jtp = PackManager.v().getPack("jtp");
          jtp.add(new Transform("jtp.instrumenter",
                                new TrapInst1()));
    

          SootResolver.v().resolveClass("TrapInstrumentation.TryCatchExample", SootClass.SIGNATURES);
          SootResolver.v().resolveClass("java.util.Comparator", SootClass.SIGNATURES);
          SootResolver.v().resolveClass("java.lang.StringBuilder", SootClass.SIGNATURES);
          soot.Main.main(args);
          
 
        }
     }