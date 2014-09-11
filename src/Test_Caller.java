
public class Test_Caller 
{
	/*
	public static void main(String[] ar)
	{
		new Test_Caller().testFun();
	}
	*/
	
	public void testFun()
	{
		int out = new Test2().fun1(new int[]{1}, 2);
		System.out.println(out);
		Test2.arrRet();
		int[] array = Test2.arrRet();
	}
	
}
