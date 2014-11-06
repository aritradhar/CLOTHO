package BugTestPack.ApacheCommonFileFtpBug;

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
public class Ftpbug {

	public static String __parsePathname(String reply)
	{
		int begin, end;

		begin = reply.indexOf('"') + 1;
		end = reply.indexOf('"', begin);

		return reply.substring(begin, end);
	}

	public static void main(String[] args)
	{
		String s = Ftpbug.__parsePathname("|257");
		System.out.println(s);
	}

}
