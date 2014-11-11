package BugTestPack.eclipseAspectJWeaverBug;

import java.util.Scanner;

import org.aspectj.weaver.BCException;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.TypeFactory;
import org.aspectj.weaver.UnresolvedType;

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
public class WeaverBug 
{
	public static UnresolvedType forSignatureP(String signature) 
	{
		//assert !(signature.startsWith("L") && signature.indexOf("<") != -1);

		char c = ' ';
		if(signature.length() > 0)
			c  = signature.charAt(0);
		
		switch (c) 
		{          
			case 'B':
				return ResolvedType.BYTE;
			case 'C':
				return ResolvedType.CHAR;
			case 'D':
				return ResolvedType.DOUBLE;
			case 'F':
				return ResolvedType.FLOAT;
			case 'I':
				return ResolvedType.INT;
			case 'J':
				return ResolvedType.LONG;
			case 'L':
				return TypeFactory.createTypeFromSignature(signature);
			case 'P':
				return TypeFactory.createTypeFromSignature(signature);
			case 'S':
				return ResolvedType.SHORT;
			case 'V':
				return ResolvedType.VOID;
			case 'Z':
				return ResolvedType.BOOLEAN;
			case '[':
				return TypeFactory.createTypeFromSignature(signature);
			case '+':
				return TypeFactory.createTypeFromSignature(signature);
			case '-':
				return TypeFactory.createTypeFromSignature(signature);
			case '?':
				return TypeFactory.createTypeFromSignature(signature);
			case 'T':
				return TypeFactory.createTypeFromSignature(signature);
			default:
				//System.out.println("Here");
				return null;
		}
	}
	
	public static UnresolvedType forSignature(String signature) 
	{
		//assert !(signature.startsWith("L") && signature.indexOf("<") != -1);
		
		switch (signature.charAt(0)) 
		{          
			case 'B':
				return ResolvedType.BYTE;
			case 'C':
				return ResolvedType.CHAR;
			case 'D':
				return ResolvedType.DOUBLE;
			case 'F':
				return ResolvedType.FLOAT;
			case 'I':
				return ResolvedType.INT;
			case 'J':
				return ResolvedType.LONG;
			case 'L':
				return TypeFactory.createTypeFromSignature(signature);
			case 'P':
				return TypeFactory.createTypeFromSignature(signature);
			case 'S':
				return ResolvedType.SHORT;
			case 'V':
				return ResolvedType.VOID;
			case 'Z':
				return ResolvedType.BOOLEAN;
			case '[':
				return TypeFactory.createTypeFromSignature(signature);
			case '+':
				return TypeFactory.createTypeFromSignature(signature);
			case '-':
				return TypeFactory.createTypeFromSignature(signature);
			case '?':
				return TypeFactory.createTypeFromSignature(signature);
			case 'T':
				return TypeFactory.createTypeFromSignature(signature);
			default:
				//System.out.println("Here");
				return null;
		}
	}
	
	public static void main(String[] args) 
	{
	
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		long s = System.currentTimeMillis();
		for(int i = 0; i<n; i++)
		{
			WeaverBug.forSignatureP("");
			//System.gc();
		}
		long e = System.currentTimeMillis();
		
		System.out.println((e - s) + " ms");
	}
}
