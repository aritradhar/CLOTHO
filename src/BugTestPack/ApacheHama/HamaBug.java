package BugTestPack.ApacheHama;

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
public class HamaBug 
{
	public static int getRowIndex(byte[] bytes) {
		String rKey = new String(bytes);
		// return zero If all zero
		if(rKey.equals("000000000000000")) {
			return 0;
		}
		if(rKey.substring(0, 8).equals("00000000")){
			int i = 8;
			while (rKey.charAt(i) == '0') {
				i++;
			}
			return Integer.parseInt(rKey.substring(i, rKey.length()));
		} else {
			int i = 0;
			while (rKey.charAt(i) == '0') {
				i++;
			}
			return Integer.parseInt(rKey.substring(i, rKey.length()));
		}
	}
	
	public static void main(String[] args) 
	{
		HamaBug.getRowIndex(new byte[]{});
	}
}
