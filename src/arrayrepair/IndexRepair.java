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
	static int ind;
	
	static int getI()
	{
		return ind;
	}
	
	static void setIndex(int index, int len)
	{
		if(index<0)
			ind = 0;
		
		if(index > len-1)
		{
			ind = len - 1;
		}
	}
}
