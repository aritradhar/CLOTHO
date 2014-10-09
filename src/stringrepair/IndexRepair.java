
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
	
	public static int getI(int i, int len, double flag)
	{
		indexRepair(i, len,flag);
		return ind1;
	}
	
	public static int getI(int i, int j, int len)
	{
		indexRepair(i, j, len);
		return ind1;
	}
	
	
	/*
	 * For repairing
	 * public static String valueOf(char[] data, int offset, int count)
	 */
	public static int getI(int i, int j, int len, double flag)
	{
		indexRepair(i, j, len, flag);
		return ind1;
	}
	/*
	 * For repairing
	 * public static String valueOf(char[] data, int offset, int count)
	 */
	public static int getJ(int i, int j, int len, double flag)
	{
		indexRepair(i, j, len, flag);
		return ind2;
	}
	/*
	 * 
	 */
	
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
	 * i -> offset
	 * j -> count
	 */
	public static void indexRepair(int i, int j,int len, double flag)
	{		
		set(i,j);
		
		if(i<0)
		{
			ind1 = 0;
		}
		
		if(j<0)
		{
			ind2 = 0;
		}
		
		if(len == 0)
		{
			ind1 = 0;
			ind2 = 0;
		}
		
		if((i + j) > len)
		{
			ind1 = 0;
			ind2 = len;
		}
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
	//TODO have bugs
	/*
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
		
		if(i>j)
		{
			if(j<=len-1 && len >0)
			{
				ind1 = ind2-1;
			}
		}
	}
	*/
	
	/*
	 * Works both for subsequence(int,int) and substring(int,int)
	 */
	public static void indexRepair(int i, int j, int len)
	{
		if(len == 0 || len == 1)
		{
			ind1 = ind2 = 0;
			return;
		}
		
		if(i>j)
		{
			if(i<0)
			{
				ind1 = 0;
				ind2 = ind1 + 1;
			}
			
			if(i>=0 && i<len - 1 && j<0)
			{
				ind1 = i;
				ind2 = ind1 + 1;
			}
			
			else if(i>=len-1 && j>len-1)
			{
				ind2 = len - 1;
				ind1 = ind2 - 1;
			}
			
			else if(i>=len - 1 && j>=1 && j<=len-1)
			{
				ind2 = j;
				ind1 = ind2 - 1;
			}
			
			else if(i>=len - 1 && j<0)
			{
				ind1 = len - 2;
				ind2 = ind1 + 1;
			}
			else if(i<len - 1 && j>=1 && j<=len-1)
			{
				ind2 = j;
				ind1 = ind2 - 1;
			}
			
			return;
			
		}
		
		if(j>i)
		{
			/*
			 * Both i and j are bad
			 */
			if(j<=0)
			{
				ind2 = 1;
				ind1 = ind2 - 1;
			}
			
			else if(j>len - 1 && i>=len - 1)
			{
				ind2 = len - 1;
				ind1 = ind2 - 1;
			}
			else if(i<0 && j>len - 1)
			{
				ind1 = 0;
				ind2 = len - 1;
			}
			
			/* 
			 * i is bad
			 * j is good
			 */		
			else if(j<=len - 1 && j>0 && i<0)
			{
				ind1 = 0;
				ind2 = j;
			}
			
			/* 
			 * j is bad
			 * i is good
			 */			
			else if(i<len - 1 && i>=0 && j>len-1 )
			{
				ind1 = i;
				ind2 = len - 1;
			}
			
			/*
			 * Bounds are satisfied
			 */
			else
			{
				set(i,j);
			}
				
			return;
		}
		/*
		 * Some crazy corner cases
		 */
		if(i==j)
		{
			if(i<0)
			{
				ind1 = 0;
				ind2 = ind1 + 1;
			}
			
			else if(i>=len-1)
			{
				ind2 = len - 1;
				ind1 = ind2 - 1;
			}
		}
	}
	

	public static void main(String[] args) 
	{
		indexRepair(1, -2,11);
		
		System.out.println(ind1 + " " + ind2);
	}

	
}
