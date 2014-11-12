package BugTestPack.ApacheOfbizBug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
public class Start 
{
	public void processClientRequest() throws IOException 
	{
		BufferedReader reader = null;
		try 
		{
			reader = new BufferedReader(new InputStreamReader(System.in));
			String request = reader.readLine();
			if (request != null) 
			{
				String key = request.substring(0, request.indexOf(':'));
				
				key = key.concat(key);
			} 
			else 
			{

			}
		}
		finally
		{
			reader.close();
		}
	}
	
	public static void main(String[] args) throws IOException 
	{
		new Start().processClientRequest();
	}
}
