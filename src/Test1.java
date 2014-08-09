

public class Test1 
{
	public static void main(String[] ar)
	{
		int []array ={1,2,3,4};
		int a = 2;
		int b = 0;
		int c = -1;
		int out = new Test1().fun(array, a, b, c);
		System.out.println(out);
	}
	
	public int fun(int[] array, int a, int b,int c)
	{
		Object obj = new Integer(0);
	    try{
		System.out.println((String)obj);
		
	    }
	    catch(ClassCastException e)
	    {
	    	System.err.println("cast");
	    	System.out.println(obj);
	    }
	    
	    /*
	    Integer a1 =10;
		Object a2 = (Object) a1;
		Double a3 = (Double) a2;
	    */
	    
	    
	    int []arr = new int[c];
		
		//int p = arr.length;
		System.out.println(a+" "+b);
		int index = a + b;
		int index1 = a / b;
		int x = array[index];
		int y = array[a-b];
		return index;
	}
}
