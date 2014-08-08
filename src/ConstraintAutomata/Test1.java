package ConstraintAutomata;

public class Test1 
{
	public static void main(String[] ar)
	{
		int []array ={1,2,3,4};
		int a = 2;
		int b = 3;
		
		int out = new Test1().fun(array, a, b);
		System.out.println(out);
	}
	
	public int fun(int[] array, int a, int b)
	{
		System.out.println(a+" "+b);
		int index = a + b;
		int x = array[index];
		int y = array[a-b];
		return x + y;
	}
}
