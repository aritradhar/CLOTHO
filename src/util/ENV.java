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
			+ "C:\\Users\\Aritra\\workspace\\git\\Repair_Spec\\bin";
	
	
	public static int REPAIR_COUNT = 0;
	
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
			
	public static int INFOFLOW_ACCESS_PATH_LENGTH = 4;
}
