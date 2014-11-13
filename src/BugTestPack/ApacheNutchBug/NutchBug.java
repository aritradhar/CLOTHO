package BugTestPack.ApacheNutchBug;

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
public class NutchBug 
{
	static int MAX_TITLE_LENGTH = -1;
	public static void bg()
	{
		String title = "acb";
		 if (title.length() > MAX_TITLE_LENGTH) { // truncate title if needed
			 title = title.substring(0, MAX_TITLE_LENGTH);
			 
		 }
		 
		 System.out.println(title);
	}
	
	public static void main(String[] args) {
		NutchBug.bg();
	}
}
