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
package arrayrepair;

import soot.Value;


public class ValuePair 
{
	Value value;
	boolean isLeft;
	boolean isRight;
	
	public ValuePair(Value value, boolean isLeft, boolean isRight)
	{
		this.value = value;
		this.isLeft = isLeft;
		this.isRight = isRight;
	}
}
