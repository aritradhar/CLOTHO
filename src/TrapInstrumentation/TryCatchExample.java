package TrapInstrumentation;

public class TryCatchExample 
{
	public void fun1()
	{
		try
		{
			int b=10;

		}
		catch(Exception e)
		{
			System.err.println("ArithmeticExeption");;
		}
	}
	
	public void handle()
	{
		System.out.println("This is the exception handling code");
	}
}
