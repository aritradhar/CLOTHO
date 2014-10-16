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
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import soot.Value;


public class GenerateString 
{	
	
	public static String init(String signature, Value stringObject, HashMap<String, HashMap<Value, ConstraintStorageDataType>> constraintStorageMap)
	{
		HashMap<Value, ConstraintStorageDataType> CSDTmap = constraintStorageMap.get(signature);
		ConstraintStorageDataType CSDT = CSDTmap.get(stringObject);
		
		int minLength = Integer.parseInt(CSDT.minLength.toString());
		int maxLength = Integer.parseInt(CSDT.maxLength.toString());
		
		List<String> prefix = new ArrayList<>();
		List<String> contains = new ArrayList<>();
		
		for(int i = 0; i < CSDT.prefix.size(); i++)
		{
			prefix.add(CSDT.prefix.get(i).toString().substring(1, CSDT.prefix.get(i).toString().length() - 2));
		}
		
		for(int i = 0; i < CSDT.contains.size(); i++)
		{
			contains.add(CSDT.contains.get(i).toString().substring(1, CSDT.contains.get(i).toString().length() - 2));
		}
		
		return generateString(minLength, maxLength, prefix, contains);
	}
	
	
	@SuppressWarnings("unused")
	public static String generateString(int minLength, int maxLength, List<String> prefix, List<String> contain)
	{
		String gen = "";
		int length = 0;
		
		if(minLength == maxLength)
			length = minLength;
		
		else
		{
			Random rand = new Random();
			length = rand.nextInt(maxLength - minLength + 1) + minLength;
		}
		
		
		
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
			
			Character[] charStr = new Character[restLength];
			
			for(int i=0; i< restLength; i++)
			{
				Random r = new Random();
				int charI = r.nextInt(255);
				charStr[i] = (char) charI;
			}
			
			gen = gen.concat(charStr.toString());
			
			return gen;
		}
		else
		{
			return gen;
		}
	}
}
