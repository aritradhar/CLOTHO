package acteve.instrumentor;
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
			int x=10;
			int y =5;
			int z =x+y;
			System.err.println("ArithmeticExeption");;
		}
		finally
		{
			System.out.println("Finally block");
		}
	}
}
