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


package ConstraintAutomata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import soot.SootClass;
import soot.SootMethod;

public class Constants 
{
	public static String UniversalDelim = ":";
	
	public static String methodListSileName = "MethodList.txt";	
	
	public static String patcheClause = "__patched";
	
	public static ConcurrentHashMap<SootClass, HashSet<SootMethod>> patchedMethodMap = new ConcurrentHashMap<>(16, 0.9f, 5);
}
