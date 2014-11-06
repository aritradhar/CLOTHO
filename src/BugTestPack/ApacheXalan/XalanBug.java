package BugTestPack.ApacheXalan;

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
public class XalanBug 
{
	public static String toJavaName(String name) 
	{
		final StringBuffer result = new StringBuffer();

		char ch = name.charAt(0);
		result.append(Character.isJavaIdentifierStart(ch) ? ch : '_');

		final int n = name.length();
		for (int i = 1; i < n; i++) {
			ch = name.charAt(i);
			result.append(Character.isJavaIdentifierPart(ch)  ? ch : '_');
		}
		return result.toString();
	}
	
	public static void main(String[] args) 
	{
		String s = XalanBug.toJavaName("");
		System.out.println(s);
	}
}
