import java.util.Arrays;

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


public class StringTest 
{
	void foo()
	{
		
	}
	
	void bar()
	{
		
	}
	public static void main(String[] args) 
	{
		String st = "aritra:dhar";

		st = st.substring(0, st.indexOf(":"));
		int a = st.length();
		
		System.out.println(st.codePointCount(0, 2));
		st.charAt(3);
		st.substring(1);
		
		int i = 1, j = 3;
		
		String subs = st.substring(i,j);
		System.out.println(subs);
		
		st.subSequence(1, 2);
		char[] c = new char[10];
		st.getChars(0, 1, c, 2);
		
		
		CharSequence s = st.subSequence(1, 2);
		System.out.println(s);
		//System.out.println(st);
	}
}
