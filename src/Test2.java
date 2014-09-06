import java.util.ArrayList;
import java.util.Iterator;



public class Test2 
{
	int F_x;
	String F_y;
	
	public static void main(String[] ar)
	{
		int []array ={10,11,12,13};
		int a = 0;
		int b = 2;
		int c = -1;
		int out = new Test2().fun(array, a, b, c);
		System.out.println(out);
	}
	
	public int fun1(int []a, int b)
	{
		return a[b*2];
	}
	
	public static int[] arrRet()
	{
		return null;
	}
	
	public int fun(int[] array, int a, int b,int c)
	{		
	    int []arr = arrRet();   
		int p = arr.length;
		arr[9] = 10;
		int q = arr[5];
		arr[3] = arr[2];
		System.out.println(p);		
		return p;
	}
}
