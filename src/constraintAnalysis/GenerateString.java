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

import java.util.List;
import java.util.Random;


public class GenerateString 
{	
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
