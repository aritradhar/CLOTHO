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
import java.util.List;


public class ConstraintStorageDataType
{
	
	int minLength;
	int maxLength;
	
	List<String> prefix;
	List<String> contains;
	
	public ConstraintStorageDataType()
	{
		this.prefix = new ArrayList<>();
		this.contains = new ArrayList<>();
	}
	
	public ConstraintStorageDataType(int minLength, int maxLength, String[] prefix, String[] contains)
	{
		this.maxLength = maxLength;
		this.minLength = minLength;
		
		this.prefix = Arrays.asList(prefix);
		this.contains = Arrays.asList(contains);
	}
	
}
