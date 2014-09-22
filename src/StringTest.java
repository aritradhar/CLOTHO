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
		System.out.println(st.codePointCount(-1, 20));
		
		String g = "Hello\uD835\uDD6b";
		
		System.out.println(g.length());
		
		int i = 1, j = -9;
		String subs = st.substring(i,j);
		System.out.println(st.codePointBefore(20));
		
		st = st.substring(0, st.indexOf(":"));
		int t = st.length();
		
		int ind = st.indexOf("&");
		int ind1 = ind + 1;
		
		st.substring(ind1, 2);
		
		st.indexOf("^");
		
		System.out.println(st.codePointAt(200));
		st.charAt(20);
		st.substring(20);			
		
		
		System.out.println(subs);
		
		st.subSequence(1, 2);
		char[] c = new char[10];
		String p = String.valueOf(c, 1, 2);
		System.out.println(p);
		
		CharSequence s = st.subSequence(1, -2);
		System.out.println(s);
		//System.out.println(st);
	}
}
