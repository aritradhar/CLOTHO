
import TestPack.*;

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
		int out = new TestPack.Test2().fun1(new int[]{1}, 2);
		System.out.println(out);
		TestPack.Test2.arrRet();
		int[] array = TestPack.Test2.arrRet();
		System.out.println(array[0]);
	}
	
}
