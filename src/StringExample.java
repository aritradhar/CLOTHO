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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;


public class StringExample 
{
	public static void main(String[] args) throws IOException
	{

		String s = new BufferedReader(new InputStreamReader(System.in)).readLine();
		String st1 = "aritra:dhar";
		

		String st = s.substring(1, 61);
		
		String st5 = s.substring(1,4);
		
		System.out.println(st5);
		
		int  k =6;
		if(st.length() > k+1)
		{
			System.out.println("Hit1");
		}
		
		//CharSequence st4 = st.subSequence(1, 2);
		//System.out.println(st4);
		
		System.out.println(st);
		if(st.length() == 1)
		{
			System.out.println("Hit2");
		}
		
		if(st.length() < 39)
		{
			System.out.println("Hit3");
		}
		else
		{
			System.out.println("Hit4");
		}
	
		if(st.startsWith(">:"))
		{
		}
	
		st1 = st1.substring(1, 2);
		System.out.println(st);

	}
}
