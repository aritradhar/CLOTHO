package Patching;

public class PatchGen 
{
	public static void handleException(String ExceptionString)
	{
		
	}
	
	public static void divideByZeroExceptionPatch(int a, int b)
	{
		if(b==0)
			b=1;
	}
	
	public static void divideByZeroExceptionPatch(float a, float b)
	{
		if(b==0)
			b=1;
	}
	
	public static void divideByZeroExceptionPatch(double a, double b)
	{
		if(b==0)
			b=1;
	}
	
	public static void divideByZeroExceptionPatch(long a, long b)
	{
		if(b==0)
			b=1;
	}
	
	public static <E> void arrayIndexOutOfBoundExceptionPatch(E[] array, int index)
	{
		if(index > array.length-1)
			index = array.length-1;
	}
	
	public static <E> void negetiveArrayIndexExceptionPatch(E[] array, int index)
	{
		if(index < 0)
			index=0;
	}
}
