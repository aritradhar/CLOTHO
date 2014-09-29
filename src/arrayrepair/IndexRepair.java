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

package arrayrepair;


public class IndexRepair 
{
	public static int ind;
	
	public static int getI(int index, int len)
	{
		setIndex(index, len);
		return ind;
	}
	
	public static void setIndex(int index, int len)
	{
		if(index<0)
			ind = 0;
		
		if(index > len-1)
		{
			ind = len - 1;
		}
	}
}
