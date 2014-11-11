package BugTestPack.ApacheCliBug;

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
public class CliBug2_x 
{
protected String rtrim(String s)
{
	         if (s == null)
	         {
	             return s;
	         }
	 
	         int pos = s.length();
	 
	         while ((pos >= 0) && Character.isWhitespace(s.charAt(pos - 1)))
	         {
	             --pos;
	         }
	 
	         return s.substring(0, pos);
	     }
	
	public static void main(String[] args) 
	{
		new CliBug2_x().rtrim("");
	}
}
