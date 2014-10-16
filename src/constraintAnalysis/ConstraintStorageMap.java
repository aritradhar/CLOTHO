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

import java.util.HashMap;

import soot.Value;

public class ConstraintStorageMap 
{
	//signature -> [String value -> ConstraintStorageDataType] 
	public static HashMap<String, HashMap<Value, ConstraintStorageDataType>> constraintStorageMap = new HashMap<>();


	public static void updateMinLength(String methodSignature, Value stringObject, Value minLength)
	{
		if(!constraintStorageMap.containsKey(methodSignature))
		{
			ConstraintStorageDataType CSDT = new ConstraintStorageDataType();
			CSDT.minLength = minLength;

			HashMap<Value, ConstraintStorageDataType> tmp = new HashMap<>();
			tmp.put(stringObject, CSDT);
			
			constraintStorageMap.put(methodSignature, tmp);

		}
		else
		{
			HashMap<Value, ConstraintStorageDataType> tmp = constraintStorageMap.get(methodSignature);

			if(!tmp.containsKey(stringObject))
			{
				ConstraintStorageDataType CSDT = new ConstraintStorageDataType();
				CSDT.minLength = minLength;
				tmp.put(stringObject, CSDT);
				
				constraintStorageMap.put(methodSignature, tmp);
			}

			else
			{
				ConstraintStorageDataType CSDT = tmp.get(stringObject);

				//makes sense
				if(CSDT.minLength == null)
				{
					CSDT.minLength = minLength;
				}
				
				else
				{
					CSDT.minLength = (Integer.parseInt(CSDT.minLength.toString()) > Integer.parseInt(minLength.toString())) ? minLength : CSDT.minLength;
				}
				
				tmp.put(stringObject, CSDT);
				constraintStorageMap.put(methodSignature, tmp);
				
			}

		}
	}
	
	
	public static void updateMaxLength(String methodSignature, Value stringObject, Value maxLength)
	{
		if(!constraintStorageMap.containsKey(methodSignature))
		{
			ConstraintStorageDataType CSDT = new ConstraintStorageDataType();
			CSDT.maxLength = maxLength;

			HashMap<Value, ConstraintStorageDataType> tmp = new HashMap<>();
			tmp.put(stringObject, CSDT);

			constraintStorageMap.put(methodSignature, tmp);
		}
		else
		{
			HashMap<Value, ConstraintStorageDataType> tmp = constraintStorageMap.get(methodSignature);

			if(!tmp.containsKey(stringObject))
			{
				ConstraintStorageDataType CSDT = new ConstraintStorageDataType();
				CSDT.maxLength = maxLength;
				tmp.put(stringObject, CSDT);
				
				constraintStorageMap.put(methodSignature, tmp);
			}

			else
			{
				ConstraintStorageDataType CSDT = tmp.get(stringObject);

				//makes sense
				if(CSDT.maxLength == null)
				{
					CSDT.maxLength = maxLength;
				}
				
				else
				{
					CSDT.maxLength = (Integer.parseInt(CSDT.maxLength.toString()) < Integer.parseInt(maxLength.toString())) ? maxLength : CSDT.maxLength;
				}
				
				tmp.put(stringObject, CSDT);
				constraintStorageMap.put(methodSignature, tmp);
			}

		}
	}

	public static void updateContains(String methodSignature, Value stringObject, Value containStr)
	{
		if(!constraintStorageMap.containsKey(methodSignature))
		{
			ConstraintStorageDataType CSDT = new ConstraintStorageDataType();
			CSDT.contains.add(containStr);

			HashMap<Value, ConstraintStorageDataType> tmp = new HashMap<>();
			tmp.put(stringObject, CSDT);
			
			constraintStorageMap.put(methodSignature, tmp);
		}
		else
		{
			HashMap<Value, ConstraintStorageDataType> tmp = constraintStorageMap.get(methodSignature);

			if(!tmp.containsKey(stringObject))
			{
				ConstraintStorageDataType CSDT = new ConstraintStorageDataType();
				CSDT.contains.add(containStr);
				tmp.put(stringObject, CSDT);
			}

			else
			{
				ConstraintStorageDataType CSDT = tmp.get(stringObject);
				CSDT.contains.add(containStr);
				tmp.put(stringObject, CSDT);
				
			}	
			constraintStorageMap.put(methodSignature, tmp);
		}
	}

	public static void updatePrefix(String methodSignature, Value stringObject, Value prefixStr)
	{
		if(!constraintStorageMap.containsKey(methodSignature))
		{
			ConstraintStorageDataType CSDT = new ConstraintStorageDataType();
			CSDT.prefix.add(prefixStr);

			HashMap<Value, ConstraintStorageDataType> tmp = new HashMap<>();
			tmp.put(stringObject, CSDT);
			constraintStorageMap.put(methodSignature, tmp);
		}
		else
		{
			HashMap<Value, ConstraintStorageDataType> tmp = constraintStorageMap.get(methodSignature);

			if(!tmp.containsKey(stringObject))
			{
				ConstraintStorageDataType CSDT = new ConstraintStorageDataType();
				CSDT.prefix.add(prefixStr);
				tmp.put(stringObject, CSDT);
			}

			else
			{
				ConstraintStorageDataType CSDT = tmp.get(stringObject);
				CSDT.prefix.add(prefixStr);
				tmp.put(stringObject, CSDT);
			}
			constraintStorageMap.put(methodSignature, tmp);

		}
	}



	public static void insetToMap(String methodSignature, Value stringObject, ConstraintStorageDataType constraintStorageDataType)
	{
		if(!constraintStorageMap.containsKey(methodSignature))
		{
			HashMap<Value, ConstraintStorageDataType> tmp = new HashMap<>();
			tmp.put(stringObject, constraintStorageDataType);
		}

		else
		{
			HashMap<Value, ConstraintStorageDataType> tmp = constraintStorageMap.get(methodSignature);
			tmp.put(stringObject, constraintStorageDataType);		
		}
	}
}