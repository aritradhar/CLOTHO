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
	
	private void fixResultExprStringP()
    {
      String r = resultExprString.trim();
      String prefix = "";
      if(r.length() >=6)
    	  prefix = r.substring(0, 6);
      else
    	  prefix = r;
      if (!prefix.toLowerCase().equals("select"))
      {
        r = "select " + r;
      }
      resultExprString = r;
    }
	
	public static void main(String[] args) 
	{
		long s = System.currentTimeMillis();
		for(int i = 0; i<50000;i++)
		{
			HiveBug h = new HiveBug();
			h.resultExprString = "sas";
		
			h.fixResultExprString();
		}
		long e = System.currentTimeMillis();
		
		System.out.println((e - s) + "ms");
	}
}
