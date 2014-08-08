import java.util.ArrayList;



public class ExampleMonitor 
{
	//static ArrayList s ;
	
	public static void main(String[] a)
	{
		ArrayList<Object> s = new ArrayList();
		//ArrayList<Object> lc = new ArrayList();
		//s = lc;
		s.add("a");
		//System.out.println(s);
		
		
		Integer []arr = new Integer[10];
		//s.add(arr);
		for(Integer i = 0;i<arr.length;i++)
			arr[i] = 1;
		
		//s.add(arr);
		arr[5] = 3;
		//s.add(arr);
		Integer x = 10,y = 5;
		Integer b = x/arr[2];
		
		//System.out.println(s.get(0));
		//int []arr1 = (int[]) s.get(0);
		//System.out.println(arr1[1]);
	}
}
