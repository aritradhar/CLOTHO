//*************************************************************************************
//*********************************************************************************** *
//author Aritra Dhar																* *
//MT12004																			* *
//M.TECH CSE																		* * 
//INFORMATION SECURITY																* *
//IIIT-Delhi																		* *	 
//---------------------------------------------------------------------------------	* *																				* *
/////////////////////////////////////////////////									* *
//The program will do the following::::         //									* *
/////////////////////////////////////////////////									* *
//version 1.0																		* *
//*********************************************************************************** * 
//*************************************************************************************

package trace;
/*
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
*/
import java.util.ArrayList;


public class TraceFlash 
{
	public static ArrayList<Object> traceList = new ArrayList<Object>();
	
	public static int flashTraceMemory(String MethodName, Object... valueType)
	{
		traceList.add(MethodName);
		for(Object vt : valueType)
			traceList.add(vt);
		
		return 1;
	}
	
	/*
	public static void flashTrace(String MethodName, ValueTraceType... vTraceType)
	{
		 try
	      {
	         FileOutputStream fileOut = new FileOutputStream(MethodName + ".trace", true);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         
	         for(ValueTraceType vt : vTraceType)
	         {
	        	 out.writeObject(vt);
	        	 out.writeUTF("\n");
	         }
	         out.writeUTF("\n");
	         out.close();
	         fileOut.close();
	         
	      }
		 
		  catch(IOException i)
	      {
	          System.err.println("problem occured in serialization");
	          i.printStackTrace();
	      }
	}
	*/
}
