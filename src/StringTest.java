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


public class StringTest 
{
	void foo()
	{			
		try
		{
			new StringTest().bar();
		}
		catch(IllegalArgumentException ex)
		{
			ex.printStackTrace();
		}

	}
	
	void bar()
	{

		new StringTest().bar1();
	}
	void bar1()
	{
		
	}
	public static void main(String[] args) throws IOException
	{
		//new StringTest().bar();
		//new StringTest().foo();
		
		String s = "aritra:dhar";
		String st1 = "aritra:dhar";
		/*
		System.out.println(st.codePointCount(-1, 20));
		
		String g = "Hello\uD835\uDD6b";
		
		System.out.println(g.length());
		
		int i = 1, j = -9;
		String subs = st.substring(i,j);
		System.out.println(st.codePointBefore(20));
		
		st = st.substring(0, st.indexOf(":"));
		int t = st.length();
		
		int ind = st.indexOf("&");
		int ind4 = st.indexOf("^");
		int ind1 = ind + 1;
		
		ind  = 6;
		System.out.println(st.length() + "  " + st);
		
		st.substring(ind1, 2);
		*/
		String st = s.substring(1, 2);
		int  k =6;
		if(st.length() > k+99)
		{
			int a =0;
		}
		
		if(st.length() == 1)
		{
			int a = 1;
		}
		
		if(st.length() > 2)
		{
			int a = 1;
		}
		
		if(st.length() < 39)
		{
			int a = 1;
		}
		else
		{
			int j = 29;
		}
		
		if(st.startsWith(">"))
		{
			int a1 =0;
		}
		if(st.startsWith(">:"))
		{
			int a1 =0;
		}
		if(st.contains("aav"))
		{
			int a1 =0;
		}
		if(st1.contains("aavst1"))
		{
			int a1 =0;
		}
//		if(st == "aa")
//		{
//			int a1 =0;
//		}
		
		String st3 = new String("AAWWW");
		if(st.equals(st3))
		{
			int a1 =0;
		}
		
		
		System.out.println(st);
		st = s.substring(1, 200);
		
		System.out.println(st);
		/*
		if(ind != 25)
		{
			int a2 =0;
		}
		*/
		/*
		System.out.println(st.codePointAt(200));
		st.charAt(20);
		st.substring(20);			
		
		
		//System.out.println(subs);
		
		
		st.subSequence(1, 2);
		char[] c = new char[10];
		String p = String.valueOf(c, 1, 2);
		System.out.println(p);
		
		//st.substring(ind, 1);
		CharSequence cs = st.subSequence(1, -2);
		System.out.println(cs);*/
		//System.out.println(st);
	}
}
