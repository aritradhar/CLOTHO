package constraintAnalysis;

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
public class MethodBond 
{
	Value base;
	String currentMethodContextSignature;
	String methodInvoked;
	
	public MethodBond(Value base, String currentMethodContextSignature, String methodInvoked)
	{
		this.base = base;
		this.currentMethodContextSignature = currentMethodContextSignature;
		this.methodInvoked = methodInvoked;
	}
}
