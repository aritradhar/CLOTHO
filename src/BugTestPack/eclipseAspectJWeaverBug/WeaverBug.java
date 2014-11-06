package BugTestPack.eclipseAspectJWeaverBug;

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
				System.out.println("Here");
				throw new BCException("Bad type signature " + signature);
		}
	}
	
	public static void main(String[] args) 
	{
		WeaverBug.forSignature("");
	}
}
