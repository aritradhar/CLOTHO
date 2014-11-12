package BugTestPack.ApacheHiveBug;

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
public class HiveBug 
{
	public String resultExprString;
	
	private void fixResultExprString()
    {
      String r = resultExprString.trim();
      String prefix = r.substring(0, 6);
      if (!prefix.toLowerCase().equals("select"))
      {
        r = "select " + r;
      }
      resultExprString = r;
    }
	
	public static void main(String[] args) 
	{
		HiveBug h = new HiveBug();
		h.resultExprString = "abc";
		
		h.fixResultExprString();
	}
}
