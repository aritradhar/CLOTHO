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

package asmbug;

import java.lang.reflect.Constructor;


public class MainTest 
{
	public static void main(String[] args) throws ClassNotFoundException 
	{
		Class<?> classN = Class.forName("asmbug.Test");
		Constructor<?>[] constructors = classN.getDeclaredConstructors();
		String constructorString = constructors[0].toGenericString();
		
		System.out.println(constructorString);
		
		Method constructor = Method.getMethod(constructors[0].toGenericString());	 
		System.out.println(constructor);
		 
		 
		String constructorString1 = constructors[1].toGenericString();		
		System.out.println(constructorString1);
			
		Method constructor1 = Method.getMethod(constructors[1].toGenericString());	 
		System.out.println(constructor1);
	}
}
