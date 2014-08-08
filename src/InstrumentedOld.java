import java.util.ArrayList;
import java.util.Iterator;

import soot.Local;


class Instrumented
{
	
	public static ArrayList<Object> variableList = new ArrayList<>();
	public static ArrayList<Local> LocalVList = new ArrayList<>();
	
	public static synchronized void inst()
	{
		System.out.println("Inside Inst");
	}
	
	public static synchronized void addToList(Object obj)
	{
		Instrumented.LocalVList.add((Local) obj);
		//System.out.println(obj.toString()+" added successfully");
	}
	
	public static synchronized void printList()
	{
		Instrumented.variableList.add("a");
		Iterator<Object> it = variableList.iterator();
		while(it.hasNext())
			System.out.println(it.next());
	}
	
	public static void main (String []a)
	{
		printList();
	}
}
