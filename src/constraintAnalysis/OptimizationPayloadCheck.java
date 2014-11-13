package constraintAnalysis;

import java.util.ArrayList;
import java.util.List;

import soot.Value;

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
public class OptimizationPayloadCheck 
{
	/*
	 * Modify allowed
	 */
	public List<Value> args;
	public boolean isChanged;
	
	public OptimizationPayloadCheck(List<Value> args, boolean isChanged)
	{
		this.args = args;
		this.isChanged = isChanged;
	}
	
}
