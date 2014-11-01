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

package apacheMathBug;

import java.text.ParsePosition;


public class Main 
{
	public static void main(String[] args) 
	{
		ParsePosition ps = new ParsePosition(2);
		
		Complex c = new Complex(1.0, 2.22);
		ComplexFormat cf = new ComplexFormat();
		
		cf.parse("10.0 + 1.0", ps);
		
		System.out.println(cf);
	}
}
