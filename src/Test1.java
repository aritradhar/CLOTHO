import java.util.ArrayList;
import java.util.Iterator;



public class Test1 
{
	public static void main(String[] ar)
	{
		int []array ={10,11,12,13};
		int a = 0;
		int b = 2;
		int c = -1;
		int out = new Test1().fun(array, a, b, c);
		System.out.println(out);
	}
	
	public int fun1(int []a, int b)
	{
		return a[b*2];
	}
	
	public int fun(int[] array, int a, int b,int c)
	{
	    /*ArrayList<Integer> ar = new ArrayList<Integer>();
	    ar.add(1);
	    ar.add(3);
	    ar.add(4);
	    ar.add(5);
	    Iterator<Integer> it = ar.iterator();
	    
	    System.out.println(it.next());
	    System.out.println(it.next());
	    System.out.println(it.next());
	    System.out.println(it.next());
	    System.out.println(it.next());
	    */
	    /*
	    Integer []a1 ={10};
	    Integer a2 =10;
		Object a3 = (Object) a1;
		
		Double a4 = 1.30;
		Double a5 = 1.90;
		
		
		@SuppressWarnings("unused")
		double a16 = (double) a3 + a2 ;
		Double a6 = (Double) a3 + a4 ;
	    
		System.out.println(a6);
	    */
		
	    int []arr = new int[c];
	    Integer []arri = new Integer[c];
	    
	    
		//int p = arr.length;
		System.out.println(a+" "+b);
		int index = a + b;
		int index1 = a / b;
		int x = array[index];
		int y = array[a-b];
		
		
		int q = new Test1().fun1(array,y);
		if(y>0)
			System.out.println(arri[x].hashCode());
		
		System.out.println(arr[index + 3]);
		//arr[0] = 9999;
		//arri[0] = 1;
		return y;
	}
}
