
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

package trace;

import java.io.Serializable;

import soot.Type;
import soot.Value;

public class ValueTraceType implements Serializable
{
	Value value;
	Type type;
	
	public ValueTraceType(Value value, Type type)
	{
		this.value = value;
		this.type = type;
	}
	
}


