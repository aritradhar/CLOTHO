package ConstraintAutomata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

public class PatchedMethodLoader 
{
	
	public PatchedMethodLoader(String loadSource)
	{
		loadMethods(loadSource);
	}
	
	private void loadMethods(String loadSource)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(loadSource));
			
			String str = "";
			while((str =  br.readLine()) != null)
			{
				/*
				 there is am empty line at the end of the file
				*/
				
				if(str.length() == 0)
					continue;
				
				String[] lineArr = str.split(" ");
				SootClass sc = Scene.v().loadClassAndSupport(lineArr[0]);
				//Scene.v().loadNecessaryClasses();

				//System.out.println(sc.getMethods().get(4).getName());
				SootMethod patchedMethod = sc.getMethodByName(lineArr[1]);
				
				if(!Constants.patchedMethodMap.containsKey(sc))
				{
					HashSet<SootMethod> al = new HashSet<SootMethod>();
					al.add(patchedMethod);
					Constants.patchedMethodMap.put(sc, al);
				}
				else
				{
					HashSet<SootMethod> al = Constants.patchedMethodMap.get(sc);
					al.add(patchedMethod);
					Constants.patchedMethodMap.put(sc, al);
				}
			}
		}
		
		catch(IOException ex)
		{
			System.err.println("Exception in reading patched method index file");
		}
	}
}
