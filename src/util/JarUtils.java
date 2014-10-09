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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarUtils 
{
	public static int BUFFER_SIZE = 10240;
	public static List<String> filesListInDir = new ArrayList<String>();
	
	
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
			System.out.println();
			
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
	

	public static void createJarArchive(String archiveFile, String _dir) 
	{
		try 
		{
			File dir = new File(_dir);
			byte buffer[] = new byte[BUFFER_SIZE];
			// Open archive file
			FileOutputStream stream = new FileOutputStream(archiveFile);
			JarOutputStream out = new JarOutputStream(stream, new Manifest());

			populateFilesList(dir);
			
			int size = filesListInDir.size(), progress = 0;
			int k = 10;
			int  i = k;	
			int v = size/(100/k);
			
			
			for(String filePath : filesListInDir)
			{
				progress++;
				
				if(progress % v == 0)
				{
					
					System.out.print (i + "%  ");
					i += (k);
				}
				System.out.println();
				
				File tmp = new File(filePath);
				
				JarEntry jarAdd = new JarEntry(filePath.substring(dir.getAbsolutePath().length()+1, filePath.length()));
				
				jarAdd.setTime(tmp.lastModified());
				out.putNextEntry(jarAdd);

				FileInputStream in = new FileInputStream(tmp);
				while (true) {
					int nRead = in.read(buffer, 0, buffer.length);
					if (nRead <= 0)
						break;
					out.write(buffer, 0, nRead);
				}
				in.close();
			}

			out.close();
			stream.close();
			System.out.println("Adding completed OK");
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
			System.out.println("Error: " + ex.getMessage());
		}
	}
	
	public static void populateFilesList(File dir) throws IOException 
	{
        File[] files = dir.listFiles();
        
        for(File file : files)
        {
            if(file.isFile()) 
            	filesListInDir.add(file.getAbsolutePath());
            
            else 
            	populateFilesList(file);
        }
    }
	
	
	
	/*
	 * DEBUG
	 * 
	public static void main(String[] args) throws IOException 
	{
		Scanner s = new Scanner(System.in);
		
		String jarFile = s.next();
		String destDir = s.next();
		
		JarExtract.createJarArchive(jarFile, destDir);
		
		s.close();
		System.out.println("Done");
	}
	*/
}
