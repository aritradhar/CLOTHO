package TestPack;

import java.util.ArrayList;
import java.util.HashMap;


public class NoSuchElement 
{
	void bar(int a)
	{
		System.out.println(a);
	}
	void foo()
	{
		HashMap<Integer, String> hm = new HashMap<>();
		hm.put(1, new String("one"));
		System.out.println(hm.remove(2));
		ArrayList<Integer> al=new ArrayList<>();
		al.add(1);
		al.add(2);
		al.remove(new Integer(3));
		
		Object x = new Integer(0);
	    System.out.println((String)x);
	}
	
	public static void main(String []a)
	{
		new NoSuchElement().foo();
	}
}
