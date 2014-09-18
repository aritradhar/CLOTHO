package TestPack;
import java.util.ArrayList;
import java.util.Iterator;



public class Test2 
{
	int F_x;
	String F_y;
	
	public Test2()
	{
		
	}
	
	public Test2(int z)
	{
		
	}
	
	public Test2(String z)
	{
		
	}
	
	public static void main(String[] ar)
	{

	}
	
	public int fun1(int []a, int b)
	{
		return a[b*2];
	}
	
	public int fun1(int []a, float b)
	{
		int in = (int) (b/2);
		return a[in];
	}
	
	public static int[] arrRet()
	{
		return null;
	}
	
	public int fun(int[] array, int a, int b,int c)
	{		
	    int []arr = arrRet();   
		int p = arr.length;
		return p;
	}
}
