package TrapInstrumentation;

public class Instrumented 
{
	public void fun2(int a)
	{
		int p=10;
		int q = 20;
		int res = p/q;
		System.out.println(res);
		
	}
}
