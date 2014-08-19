import java.util.ArrayList;
import java.util.Iterator;



public class Test2 
{
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
	
	public int fun(int[] array, int a, int b,int c)
	{
		
	    int []arr = null;    	    
		int p = arr.length;
		System.out.println(p);
		return p;
	}
}
