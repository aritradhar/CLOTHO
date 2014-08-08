package TestPack;

import java.io.IOException;


public class TryCatchExample 
{
	public static void main(String []ar)
	{
		try
		{
			int b=10;
			int c=0;
			int a=b/c;
		}
		catch(Exception e)
		{

			System.out.println(e.toString());
			e.getCause();
			System.err.println("ArithmeticExeption");;
		}
		
	}
}
