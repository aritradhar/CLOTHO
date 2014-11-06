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

package util;

import soot.Scene;
import soot.SootClass;
import soot.options.Options;

/*
 * Repair tool global environment controller
 */
public class ENV 
{
	
	public static String SOOT_CLASS_PATH = "C:\\Users\\Aritra\\workspace\\git\\Repair_Spec\\bin;"
			+ "C:\\Users\\Aritra\\Desktop\\jperf-2.0.2\\lib\\jfreechart-1.0.6.jar;"
			+ "C:\\Users\\Aritra\\Desktop\\jperf-2.0.2\\lib\\jcommon-1.0.10.jar;"
			+ "C:\\Users\\Aritra\\Desktop\\jperf-2.0.2\\lib\\forms-1.1.0.jar;"
			+ "C:\\Users\\Aritra\\Desktop\\jperf-2.0.2\\lib\\swingx-0.9.6.jar;"
			+ "C:\\lib\\javax-crypto.jar;"
			+ "C:\\lib\\swt.jar;"
			+ "C:\\Users\\Aritra\\workspace\\git\\Repair_Spec\\Azureus2.jar;"
			+ "C:\\lib\\apache-logging-log4j.jar;"
			+ "C:\\lib\\commons-cli-1.2.jar;"
			+ "C:\\lib\\junit-4.8.2.jar;"
			+ "C:\\Users\\Aritra\\workspace\\git\\Repair_Spec\\bin;"
			+ "C:\\test\\lib\\servlet-api-2.5-20081211.jar;"
			+ "C:\\lib\\spring-mock-2.0.6.jar;"
			+ "C:\\lib\\spring-2.0-core.jar;"
			+ "C:\\lib\\org-apache-commons-logging.jar;"
			+ "C:\\lib\\apache-commons.jar;"
			+ "C:\\lib\\com.springsource.edu.emory.mathcs.backport_3.1.0.jar;"
			+ "C:\\aspectj1.6\\lib\\aspectjrt.jar;"
			+ "C:\\aspectj1.6\\lib\\aspectjtools.jar;"
			+ "C:\\aspectj1.6\\lib\\aspectjweaver.jar;"
			+ "C:\\aspectj1.6\\lib\\org.aspectj.matcher.jar";
	
	
	/*
	 * All stats here
	 */
	public static int STAT_REPAIR_COUNT = 0;
	public static int STAT_UNIT_HANDLED = 0;
	public static int STAT_UNIT_POST_REPAIR = 0;
	public static int STAT_CALL_GRAPH_SIZE = 0;
	
	/*
	 * 1 -> Jimple
	 * 2 -> Class
	 */
	public static int CONSTRAINT_ANALYSIS_PHASE_WRITE_OPTION = 1;
	public static int INSTRUMRNTATION_PASE_WRITE_OPTION = 2;
	public static int CALLGRAPH_ANALYSIS_PHASE_WRITE_OPTION = 1;
	/*
	 * range - [1, infinity]
	 * snippet from the javadoc of infoflow
	 * Sets the maximum depth of the access paths. All paths will be truncated
	 * if they exceed the given size.
	 * @param accessPathLength the maximum value of an access path. If it gets longer than
	 * this value, it is truncated and all following fields are assumed as tainted
	 * (which is imprecise but gains performance)
	 * Default value is 5.
	 */
			
	public final static int INFOFLOW_ACCESS_PATH_LENGTH = 4;
	
	public final static boolean TAINT_ANALYSIS_ENABLE = true;
	
	public final static boolean CALL_CHAIN_LOOK_UP_FOR_EXCEPTION_HANDLER = true;
	public final static boolean CALL_CHAIN_LOOK_UP_FOR_EXCEPTION_HANDLER_INSTRUMRNTATION = true;
	
	public final static boolean PROFILE_ANALYSIS_TIME = true;
	public final static boolean PROFILE_ANALYSIS_MEMORY = true;
	
	public final static boolean KEEP_SOURCE_LINE_NUMBER = true;
	
	public static void classReseolver()
	{
		Scene.v().addBasicClass("constraintAnalysis.ConstraintStorageMapDynamic",SootClass.SIGNATURES);
        Scene.v().addBasicClass("constraintAnalysis.GenerateStringDynamic",SootClass.SIGNATURES);
        Scene.v().addBasicClass("stringrepair.IndexRepair", SootClass.SIGNATURES);   
        Scene.v().addBasicClass("stringrepair.FailSafeCharAt", SootClass.SIGNATURES);
        Scene.v().addBasicClass("constraintAnalysis.stringRepair.EncounterRepair", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.util.Formatter", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.io.BufferedWriter", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.io.OutputStreamWriter", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.util.Formatter", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.util.Formatter", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.StringIndexOutOfBoundsException",SootClass.SIGNATURES);
        Scene.v().addBasicClass("StringTest",SootClass.SIGNATURES);
	}
	
	public static void classReseolverBody()
	{
		Scene.v().addBasicClass("java.lang.Object",SootClass.BODIES);
		Scene.v().addBasicClass("java.lang.System", SootClass.BODIES);
        Scene.v().addBasicClass("java.util.Formatter", SootClass.BODIES);
        Scene.v().addBasicClass("java.io.BufferedWriter", SootClass.BODIES);
        Scene.v().addBasicClass("java.io.OutputStreamWriter", SootClass.BODIES);
        Scene.v().addBasicClass("java.util.Formatter", SootClass.BODIES);
        Scene.v().addBasicClass("java.util.Formatter", SootClass.BODIES);
        Scene.v().addBasicClass("java.util.HashMap", SootClass.BODIES);
        Scene.v().addBasicClass("java.util.URLDecoder", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.String", SootClass.BODIES);
        Scene.v().addBasicClass("java.io.BufferedReader", SootClass.BODIES);
        Scene.v().addBasicClass("java.io.PrintStream", SootClass.BODIES);
        Scene.v().addBasicClass("java.io.InputStreamReader", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.Throwable", SootClass.BODIES);
        Scene.v().addBasicClass("java.io.Reader", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.IllegalArgumentException", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.Exception", SootClass.BODIES);
        Scene.v().addBasicClass("java.io.FilterOutputStream", SootClass.BODIES);
        Scene.v().addBasicClass("java.io.OutputStream", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.RuntimeException", SootClass.BODIES);
        Scene.v().addBasicClass("java.io.IOException", SootClass.BODIES);
        Scene.v().addBasicClass("java.net.URLDecoder", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.StringBuilder", SootClass.BODIES);
        Scene.v().addBasicClass("java.util.StringTokenizer", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.AbstractStringBuilder", SootClass.BODIES);
        Scene.v().addBasicClass("java.util.AbstractMap", SootClass.BODIES);
        Scene.v().addBasicClass("java.io.InputStream", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.Character", SootClass.BODIES);
        Scene.v().addBasicClass("java.math.BigDecimal", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.Double", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.Float", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.Integer", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.Short", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.Long", SootClass.BODIES);
        Scene.v().addBasicClass("java.math.BigInteger", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.NumberFormatException", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.Number", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.Character", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.StringBuffer", SootClass.BODIES);
        Scene.v().addBasicClass("java.io.File", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.Math", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.Class", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.AssertionError", SootClass.BODIES);
        Scene.v().addBasicClass("java.lang.Error", SootClass.BODIES);
        Scene.v().addBasicClass("java.util.MissingResourceException", SootClass.BODIES);
        Scene.v().addBasicClass("java.util.MissingResourceException", SootClass.BODIES);
        Scene.v().addBasicClass("java.util.MissingResourceException", SootClass.BODIES);
	}
	
	public static void WriteOption(int option) 
	{
		if(option == 1)
			Options.v().set_output_format(Options.output_format_jimple);
		

		if(option == 2)
			Options.v().set_output_format(Options.output_format_class);
	}
}
