package acteve.instrumentor;

public class Test_AnalyzeStmt 
{
	public int foo(int a,int b)
	{
		return a/b;
	}
	public int bar(int []a,int b)
	{
		return a[b];
	}
	
	public static void main(String []arg)
	{
		int a=10;
		int b=5;
		int c=100;
		int d=-10;
		int []arr=new int[10];
		for(int i=0;i<arr.length;i++)
		{
			arr[i]=i;
		}
		if(arr[5]==3)
		{
			arr[6]++;
		}
		int r1=new Test_AnalyzeStmt().foo(a, b);
		int r2=new Test_AnalyzeStmt().bar(arr, c);
		int r3=new Test_AnalyzeStmt().bar(arr, d);
	}

}
