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

import soot.Value;


public class ConstraintStorageDataTypeDynamic
{
	
	public int minLength;
	public int maxLength;
	
	public List<String> prefix;
	public List<String> contains;
	
	public List<String> equals; 
	
	public ConstraintStorageDataTypeDynamic()
	{
		minLength = -1;
		maxLength = - 1;
		this.prefix = new ArrayList<>();
		this.contains = new ArrayList<>();
		this.equals = new ArrayList<>();
	}
	
	public ConstraintStorageDataTypeDynamic(int minLength, int maxLength, String[] prefix, String[] contains, String[] equals)
	{
		this.maxLength = maxLength;
		this.minLength = minLength;
		
		this.prefix = Arrays.asList(prefix);
		this.contains = Arrays.asList(contains);
		
		this.equals = Arrays.asList(equals);
	}
	
}
