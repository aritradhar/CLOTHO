import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class RuntimeExceptions 
{
	public static void main (String []a) throws FileNotFoundException, IOException
	{
		Object []x = new String[3];
		x[0] = new Integer(10);
		
		ArrayList<Integer> ar = new ArrayList<>();
		ar.add(1);
		ar.add(2);
		
		Iterator it = ar.iterator();
		it.next();
		it.next();
		//it.next();
		
		File f = new File("a.txt");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String s = "";
		while((s=br.readLine())!=null)
		{
			
		}
		
		Integer[] a1 =new Integer[2];
		int i = 3;
		//if(i < 1)
		
		int b = a1[i+i+4];
		int k=9;
		int k1 = k/b;
		
		
	}
}
