package stringrepair;

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
public class FailSafeCharAt 
{
	public static char failSafeCharAt(String base, int index, int length)
	{
		int repairedIndex = IndexRepair.getI(index, length);
		
		if(repairedIndex == 0)
			return ' ';
		
		else
			return base.charAt(IndexRepair.getI(index, length));
	}
}
