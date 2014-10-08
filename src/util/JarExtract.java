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

package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarExtract 
{
	@SuppressWarnings("resource")
	public static void extract(String jarFile, String destDir) throws IOException
	{
		JarFile jar = new JarFile(jarFile);
		Enumeration<JarEntry> enumEntries = jar.entries();
		
		int size = jar.size(), progress = 0;
		int k = 10;
		int  i = k;	
		int v = size/(100/k);
		
		while (enumEntries.hasMoreElements()) 
		{	
			progress++;
			if(progress % v == 0)
			{
				
				System.out.print (i + "%  ");
				i += (k);
			}
			
		    JarEntry file = (JarEntry) enumEntries.nextElement();
		    File f = new File(destDir + File.separator + file.getName());
		    
		    if (file.isDirectory()) 
		    { 
		        f.mkdir();
		        continue;
		    }
		    
		    InputStream is = jar.getInputStream(file); // get the input stream
		    FileOutputStream fos = new java.io.FileOutputStream(f);
		    
		    while (is.available() > 0) 
		    {  
		        fos.write(is.read());
		    }
		    
		    fos.close();
		    is.close();
		}
	}
	
	
	public static void main(String[] args) throws IOException 
	{
		Scanner s = new Scanner(System.in);
		
		String jarFile = s.next();
		String destDir = s.next();
		
		JarExtract.extract(jarFile, destDir);
		
		s.close();
		System.out.println("Done");
	}
}
