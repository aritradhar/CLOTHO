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

import java.util.Scanner;

public class BugSynthetic 
{
	public static void main(String[] args) 
	{
		String st = "abc";		
		st = st.substring(1,4);
		System.out.println("After 1st failure : " + st);
		
		if(st.startsWith("abcd"))
		{
			System.out.println("Static : " + st);
		}
		
		Scanner s = new Scanner(System.in);
		String in = s.next();
		
		if(st.startsWith(in))
		{
			System.out.println("Dynamic : " + st);
		}
		
		System.out.println("After Dynamic : " + st);
		
		String st1 = "pqrs";
		st1 = st1.substring(1,50);
		System.out.println("After 2nd failure : " + st1);
		s.close();
	}
}
