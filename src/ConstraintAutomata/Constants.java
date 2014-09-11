package ConstraintAutomata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import soot.SootClass;
import soot.SootMethod;

public class Constants 
{
	
	public static String methodListSileName = "MethodList.txt";	
	
	public static String patcheClause = "__patched";
	
	public static HashMap<SootClass, HashSet<SootMethod>> patchedMethodMap = new HashMap<>();
}
