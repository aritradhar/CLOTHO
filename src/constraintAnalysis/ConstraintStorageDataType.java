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


public class ConstraintStorageDataType
{
	
	public Value minLength;
	public Value maxLength;
	
	public List<Value> prefix;
	public List<Value> contains;
	
	public List<Value> equals; 
	
	public ConstraintStorageDataType()
	{
		this.prefix = new ArrayList<>();
		this.contains = new ArrayList<>();
		this.equals = new ArrayList<>();
	}
	
	public ConstraintStorageDataType(Value minLength, Value maxLength, Value[] prefix, Value[] contains, Value[] equals)
	{
		this.maxLength = maxLength;
		this.minLength = minLength;
		
		this.prefix = Arrays.asList(prefix);
		this.contains = Arrays.asList(contains);
		
		this.equals = Arrays.asList(equals);
	}
	
}
