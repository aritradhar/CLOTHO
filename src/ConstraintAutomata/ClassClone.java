package ConstraintAutomata;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
		          
		          System.out.println("Method " + nxtMethod.getName() + " clonned");
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
			try
			{		 
				SootMethod newMethod = new SootMethod(nxtMethod.getName(),
						 nxtMethod.getParameterTypes(), nxtMethod.getReturnType(),
						 nxtMethod.getModifiers(), nxtMethod.getExceptions());
				
				 SootMethod newMethod_patched = new SootMethod(nxtMethod.getName() + "__patched__",
						 nxtMethod.getParameterTypes(), nxtMethod.getReturnType(),
						 nxtMethod.getModifiers(), nxtMethod.getExceptions());
				 
				 sClass.addMethod(newMethod);
				 sClass.addMethod(newMethod_patched);

		         JimpleBody body = Jimple.v().newBody(newMethod);
		         JimpleBody body_patched = Jimple.v().newBody(newMethod_patched);
		         
		         body.importBodyContentsFrom(nxtMethod.retrieveActiveBody());
		         newMethod.setActiveBody(body);
		         
		         body_patched.importBodyContentsFrom(nxtMethod.retrieveActiveBody());
		         newMethod_patched.setActiveBody(body_patched);
		          
		         System.out.println("Method " + newMethod.getName() + " clonned");
		         System.out.println("Method " + newMethod_patched.getName() + " patched added");
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
	    try
	    {
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
			return;
		
		Scene.v().loadClassAndSupport("java.lang.Object");
		
		sClass = new SootClass(currentClass.getName(), currentClass.getModifiers());
		sClass.setSuperclass(currentClass.getSuperclass());
		
		sClass.getInterfaces().addAll(currentClass.getInterfaces());
		
		cloneField(currentClass, sClass);
		cloneMethod(currentClass, sClass);
		
		flashClass(sClass);
	}
	
	
	public static void getClone(boolean flash, boolean taint)
	{
		if(! (flash && taint))
			throw new IllegalArgumentException("Wrong flag type");
		
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
		SootClass currentClass = Scene.v().loadClassAndSupport("Test2");
		ClassClone.currentClass =  currentClass;
		
		ClassClone.getClone(true, true);
	}
}
