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
	
	public static String init(String signature, String stringObject)
	{
		//HashMap<Value, ConstraintStorageDataType> CSDTmap = constraintStorageMap.get(signature);
		//ConstraintStorageDataType CSDT = CSDTmap.get(stringObject);
		
		/*
		 * In case the object with no constrain list,
		 */
		/*
		for(String m : ConstraintStorageMapDynamic.constraintStorageMapDynamic.keySet())
		{
			System.out.println(m);
			HashMap<String , ConstraintStorageDataTypeDynamic> t = ConstraintStorageMapDynamic.constraintStorageMapDynamic.get(m);
			for(String s : t.keySet())
			{
				ConstraintStorageDataTypeDynamic c = t.get(s);
				System.out.println("Max :" +c.maxLength);
				System.out.println("Min :" +c.minLength);
				System.out.println("prefix :" +c.prefix);
				System.out.println("Contains :" +c.contains);
				System.out.println("Equals :" +c.equals);
			}
		}
		*/
		
		HashMap<String, ConstraintStorageDataTypeDynamic> tempMap = ConstraintStorageMapDynamic.constraintStorageMapDynamic.get(signature);
		ConstraintStorageDataTypeDynamic CSDTdynamic = tempMap.get(stringObject);
		
		
		Integer minLength =CSDTdynamic.minLength;
		Integer maxLength = CSDTdynamic.maxLength;
		
		List<String> prefix = CSDTdynamic.prefix;
		List<String> contains = CSDTdynamic.contains;
		
		List<String> equals = CSDTdynamic.equals;
		
		return generateString(minLength, maxLength, prefix, contains, equals);
	}
	
	
	@SuppressWarnings("unused")
	public static String generateString(Integer minLength, Integer maxLength, List<String> prefix, List<String> contain, List<String> equals)
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
		
		if(minLength == null && maxLength == null)
		{
			String selectString = "";
			String selectPrefix = "";
			if(prefix.size() > 0)
			{
				int prefixLength = 0;
				for(int i = 0; i< prefix.size();i++)
				{
					String str = prefix.get(i);
					if(str.length() > prefixLength)
					{
						selectPrefix = str;
						prefixLength = str.length();
					}
				}
			}
			selectString = selectString.concat(selectPrefix);
			
			if(contain.size() > 0)
			{
				for(int i = 0; i< contain.size(); i++)
				{
					selectString = selectString.concat(contain.get(i));
				}
			}
			
			return selectString;
		}
		
		int length = 0;
		String gen = "";
		
		if(minLength == null && maxLength != null)
		{
			length = maxLength;
		}
		
		else if(minLength != null && maxLength == null)
		{
			length = minLength;
		}
		
		else if(minLength == maxLength)
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
