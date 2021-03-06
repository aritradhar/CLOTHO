package ConstraintAutomata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Iterator;

import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SourceLocator;
import soot.jimple.JasminClass;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.options.Options;
import soot.util.JasminOutputStream;

public class ClassClone 
{
	public static SootClass currentClass;
	public static SootClass sClass;

	
	ClassClone(SootClass currentClass)
	{
		ClassClone.currentClass = currentClass;
	}
	
	//not used
	public static void addMethodPatched(SootClass currentClass)
	{
		Iterator<SootMethod> it_mtd = currentClass.getMethods().iterator();
		
		while(it_mtd.hasNext())
		{
			SootMethod nxtMethod = it_mtd.next();
			
			//if(nxtMethod.getName().contains("<init>"))
				//continue;
			
			try
			{
				if(!nxtMethod.getName().startsWith("<"))
				{
				 SootMethod newMethod = new SootMethod(nxtMethod.getName() + Constants.patcheClause,
						 nxtMethod.getParameterTypes(), nxtMethod.getReturnType(),
						 nxtMethod.getModifiers(), nxtMethod.getExceptions());
				 
				 currentClass.addMethod(newMethod);

		          JimpleBody body = Jimple.v().newBody(newMethod);
		          body.importBodyContentsFrom(nxtMethod.retrieveActiveBody());
		          newMethod.setActiveBody(body);
		          
		          System.out.println("Method " + newMethod.getName() + " clonned");
				}
			}
			catch(Exception ex)
			{
				System.err.println(nxtMethod.getName()+ " skipped");
			}
		}
		
		//System.out.println(currentClass.getMethods().get(2).getActiveBody());
		flashClass(currentClass);

	}
	
	private static void cloneField(SootClass currentClass, SootClass sClass)
	{
		Iterator<SootField> it_fld = currentClass.getFields().iterator();
		
		while(it_fld.hasNext())
		{
			SootField nxtField = it_fld.next();
			try
			{
				SootField newField = new SootField(nxtField.getName(), nxtField.getType(), nxtField.getModifiers());
				sClass.addField(newField);
				
				System.out.println("Field " + nxtField.getName() + " cloned");
			}
			catch(Exception ex)
			{
				System.err.println(nxtField.getName()+ " skipped");
			}
		}
	}
	
	private static void cloneMethod(SootClass currentClass, SootClass sClass)
	{
		Iterator<SootMethod> it_mtd = currentClass.getMethods().iterator();
		
		while(it_mtd.hasNext())
		{
			SootMethod nxtMethod = it_mtd.next();
			try
			{
				 SootMethod newMethod = new SootMethod(nxtMethod.getName(),
						 nxtMethod.getParameterTypes(), nxtMethod.getReturnType(),
						 nxtMethod.getModifiers(), nxtMethod.getExceptions());
				 
				 sClass.addMethod(newMethod);

		          JimpleBody body = Jimple.v().newBody(newMethod);
		          body.importBodyContentsFrom(nxtMethod.retrieveActiveBody());
		          newMethod.setActiveBody(body);
		          
		          FileWriter fwrite = new FileWriter(Constants.methodListSileName,true);		          
		          
		          System.out.println("Method " + nxtMethod.getName() + " clonned");
		          fwrite.append(sClass.toString() + Constants.UniversalDelim + nxtMethod.getBytecodeSignature() + "\n");
		          
		          fwrite.close();
		          
			}
			catch(Exception ex)
			{
				System.err.println(nxtMethod.getName()+ " skipped");
			}
		}
	}
	
	private static void cloneMethod(SootClass currentClass, SootClass sClass, boolean taint)
	{
		if(!taint)
			throw new IllegalArgumentException("taint flag seet to false");
		
		Iterator<SootMethod> it_mtd = currentClass.getMethods().iterator();
		
		while(it_mtd.hasNext())
		{
			SootMethod nxtMethod = it_mtd.next();
			//skip constructor
				
			try
			{		 
				SootMethod newMethod = new SootMethod(nxtMethod.getName(),
						 nxtMethod.getParameterTypes(), nxtMethod.getReturnType(),
						 nxtMethod.getModifiers(), nxtMethod.getExceptions());
								 
				 
				 sClass.addMethod(newMethod);
				 

		         JimpleBody body = Jimple.v().newBody(newMethod);
		         
		         
		         body.importBodyContentsFrom(nxtMethod.retrieveActiveBody());
		         newMethod.setActiveBody(body);
		          
		         System.out.println("Method " + newMethod.getName() + " clonned");
		         
		         if(!nxtMethod.getName().startsWith("<"))
		         {
		        	 SootMethod newMethod_patched = new SootMethod(nxtMethod.getName() + Constants.patcheClause,
						 nxtMethod.getParameterTypes(), nxtMethod.getReturnType(),
						 nxtMethod.getModifiers(), nxtMethod.getExceptions());
		         
		        	 sClass.addMethod(newMethod_patched);
		         
		        	 JimpleBody body_patched = Jimple.v().newBody(newMethod_patched);
		         
		        	 body_patched.importBodyContentsFrom(nxtMethod.retrieveActiveBody());
		        	 newMethod_patched.setActiveBody(body_patched);
		         
		        	 FileWriter fwrite = new FileWriter(Constants.methodListSileName,true);	
		        	 System.out.println("Method" + newMethod_patched.getName() + " patched added");
		        	 
		        	 fwrite.append(sClass.toString() + Constants.UniversalDelim + newMethod_patched.getSubSignature() + "\n");
			          
			          fwrite.close();
		         
		         }
			}
			
			catch(Exception ex)
			{
				System.err.println(nxtMethod.getName()+ " skipped");
			}
		}
	}
	
	private static void flashClass(SootClass sClass)
	{
		String fileName = SourceLocator.v().getFileNameFor(sClass, Options.output_format_class);
		
		StringBuffer Fname = new StringBuffer();
		
		try
		{
			//fixed
			String []filenameFragments = fileName.split("\\\\");
									
			//exclude the class name
			for(int i = 0; i < filenameFragments.length - 1; i++)
			{
				Fname.append(filenameFragments[i] + "\\");
			}
			
			Fname = new StringBuffer(Fname.subSequence(0, Fname.length()));
			System.out.println(Fname);
				
			File file = new File(Fname.toString()); 
			file.mkdirs();
		}
		catch(Exception ex)
		{
			System.err.println("Problem in creating new path for the clss file");
			ex.printStackTrace();
		}
	    try
	    {
	    	//all nested paths are created -- fixed
	    	OutputStream streamOut = new JasminOutputStream(new FileOutputStream(fileName));
	    	PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
	    	
	    	JasminClass jasminClass = new soot.jimple.JasminClass(sClass);
	        
	    	jasminClass.print(writerOut);
	        
	    	writerOut.flush();
	        streamOut.close();
	        
	        System.out.println("Class clone flashed in disk");
	    }
	    
	    catch(Exception ex)
	    {
	    	System.err.println("Exception happen in clone class flashing");
	    	ex.printStackTrace();
	    }
	}
	
	public static void getClone()
	{
		Scene.v().loadClassAndSupport("java.lang.Object");
		
		sClass = new SootClass(currentClass.getName(), currentClass.getModifiers());
		sClass.setSuperclass(currentClass.getSuperclass());
		
		cloneField(currentClass, sClass);
		cloneMethod(currentClass, sClass);
		
	}

	//call if want to flash to disk
	public static void getClone(boolean flash)
	{
		if(!flash)
		{
			throw new IllegalArgumentException("Wrong flag type");
		}
		
		Scene.v().loadClassAndSupport("java.lang.Object");
		
		sClass = new SootClass(currentClass.getName(), currentClass.getModifiers());
		sClass.setSuperclass(currentClass.getSuperclass());
		
		sClass.getInterfaces().addAll(currentClass.getInterfaces());
		
		cloneField(currentClass, sClass);
		cloneMethod(currentClass, sClass);
		
		flashClass(sClass);
	}
	
	//call if want to flash to disk + method clone for patched version
	public static void getClone(boolean flash, boolean taint)
	{
		if(! (flash && taint))
		{
			throw new IllegalArgumentException("Wrong flag type");
		}
		
		
		Scene.v().loadClassAndSupport("java.lang.Object");
		
		sClass = new SootClass(currentClass.getName(), currentClass.getModifiers());
		sClass.setSuperclass(currentClass.getSuperclass());
		
		sClass.getInterfaces().addAll(currentClass.getInterfaces());
		
		cloneField(currentClass, sClass);
		cloneMethod(currentClass, sClass, taint);
		
		flashClass(sClass);
	}
}


class ClassCloneMain
{
	public static void main(String[] ar)
	{
		SootClass currentClass = Scene.v().loadClassAndSupport("TestPack.Test2");
		ClassClone.currentClass =  currentClass;
		
		//ClassClone.addMethodPatched(currentClass);
		ClassClone.getClone(true, true);
	}
}
