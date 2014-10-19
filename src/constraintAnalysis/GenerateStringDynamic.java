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
package constraintAnalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import soot.Value;


public class GenerateStringDynamic 
{	
	
	public static String init(String signature, String stringObject, ConstraintStorageDataTypeDynamic CSDTdynamic)
	{
		//HashMap<Value, ConstraintStorageDataType> CSDTmap = constraintStorageMap.get(signature);
		//ConstraintStorageDataType CSDT = CSDTmap.get(stringObject);
		
		/*
		 * In case the object with no constrain list,
		 */
		
		Integer minLength =CSDTdynamic.minLength;
		Integer maxLength = CSDTdynamic.maxLength;
		
		List<String> prefix = CSDTdynamic.prefix;
		List<String> contains = CSDTdynamic.contains;
		
		List<String> equals = CSDTdynamic.equals;
		
		return generateString(minLength, maxLength, prefix, contains, equals);
	}
	
	
	@SuppressWarnings("unused")
	public static String generateString(int minLength, int maxLength, List<String> prefix, List<String> contain, List<String> equals)
	{
		if(equals.size() > 0)
		{
			int len = 0;
			String str = "";
			for(int i = 0; i<equals.size(); i++)
			{
				if(equals.get(i).length() > len)
				{
					str = equals.get(i);
					len = equals.get(i).length();
				}
			}
			
			return str;
		}
		
		String gen = "";
		int length = 0;
		
		if(minLength == maxLength)
		{
			length = minLength;
		}
		
		else if(minLength > maxLength)
		{
			length = minLength;
		}
		
		else
		{
			Random rand = new Random();
			length = rand.nextInt(maxLength - minLength + 1) + minLength;
		}
		//System.out.println(length);
		
		
		int prefixLength = 0;
		String selectPrefix = "";
		for(int i = 0; i< prefix.size();i++)
		{
			String str = prefix.get(i);
			if(str.length() > prefixLength)
			{
				selectPrefix = str;
				prefixLength = str.length();
			}
		}
		
		gen = selectPrefix;
		
		
		int k = 0;
		for(int i = 0; i < contain.size(); i++)
		{
			String contString = contain.get(i);
			if(gen.contains(contString))
			{
				continue;
			}
			
			gen = gen.concat(contString);
		}
		if (gen.length() >= length && gen.length() <= maxLength)	
		{
			return gen;
		}
		
		else if(gen.length() < length)
		{
			int restLength = length - gen.length();
			
			char[] charStr = new char[restLength];
			
			for(int i=0; i< restLength; i++)
			{
				Random r = new Random();
				/*
				 * Keep only useful characters 
				 */
				int charI = r.nextInt(127 - 33 + 1) + 33;
				charStr[i] = (char) charI;
			}
		
			gen = gen.concat(String.valueOf(charStr));
			
			return gen;
		}
		else
		{
			return gen;
		}
	}
	
//	public static void main(String[] args) 
//	{
//		for(int i=0; i<1000;i++)
//		{
//			String s = generateString(0, i, Arrays.asList(new String[]{"ab","abba"}), Arrays.asList(new String[]{":", ">>"}), Arrays.asList(new String[]{"aaaa>><<%"}));
//			
//			System.out.println(s);
//		}
//	}
}
