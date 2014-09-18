
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

package stringrepair;

public class IndexRepair
{
	public static int ind1;
	public static int ind2;
	
	/*
	 * Allowable 0 -> len - 1
	 */
	public static int getI(int i, int len)
	{
		indexRepair(i, len);
		return ind1;
	}
	
	public static int getI(int i, int len,double flag)
	{
		indexRepair(i, len,flag);
		return ind1;
	}
	
	public static int getI(int i, int j, int len)
	{
		indexRepair(i, j, len);
		return ind1;
	}
	
	public static int getJ(int i, int j, int len)
	{
		indexRepair(i, j, len);
		return ind2;
	}
	
	public static void set(int i, int j)
	{
		ind1 = i;
		ind2 = j;
	}
	public static void set(int i)
	{
		ind1 = i;
	}
	
	/*
	 * Works both for substing(int), charAt(int), codePointAt(int)
	 */
	public static void indexRepair(int i, int len)
	{
		set(i);
		
		if(i < 0)
		{
			ind1 = 0;
		}
		if(i > len - 1)
		{
			ind1 = len - 1;
		}
	}
	
	/*
	 * For code point before
	 */
	
	public static void indexRepair(int i, int len, double flag)
	{
		set(i);
		
		if(i < 1)
		{
			ind1 = 1;
		}
		if(i > len)
		{
			ind1 = len;
		}
	}
	
	/*
	 * Works both for subsequence(int,int) and substring(int,int)
	 */
	
	public static void indexRepair(int i, int j, int len)
	{
		set(i, j);
		
		if(i<0 && j>=len)
		{
			ind1 = 0;
			ind2 = len - 1;
		}
		if(i<0)
		{
			ind1 = 0;
		}
		
		if(j >= len)
		{
			ind2 = len - 1;
		}
		
		if(j<0)
		{
			if(i >= len-1)
			{
				ind2 = len - 1;
				ind1 = ind2 - 1;
			}
			else
			{
				ind2 = ind1 + 1;
			}
		}
	}
	
	
	/*
	 *
	public static void main(String[] args) 
	{
		indexRepair(0, -9, 5);
		
		System.out.println(ind1 + " " + ind2);
	}
	*/
	
}
