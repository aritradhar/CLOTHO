
public class Test_Caller 
{
	public void testFun()
	{
		int out = new Test2().fun1(new int[]{1}, 2);
		System.out.println(out);
		Test2.arrRet();
		int[] array = Test2.arrRet();
	}
	
}
