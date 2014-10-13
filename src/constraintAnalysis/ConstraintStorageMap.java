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

public class ConstraintStorageMap 
{
	//signature -> [Stringname -> ConstraintStorageDataType] 
	public static HashMap<String, HashMap<String, ConstraintStorageDataType>> constraintStorageMap = new HashMap<>();


	public static void updateMinLength(String methodSignature, String stringObject, int minLength)
	{
		if(!constraintStorageMap.containsKey(methodSignature))
		{
			ConstraintStorageDataType CSDT = new ConstraintStorageDataType();
			CSDT.minLength = minLength;

			HashMap<String, ConstraintStorageDataType> tmp = new HashMap<>();
			tmp.put(stringObject, CSDT);

		}
		else
		{
			HashMap<String, ConstraintStorageDataType> tmp = constraintStorageMap.get(methodSignature);

			if(!tmp.containsKey(stringObject))
			{
				ConstraintStorageDataType CSDT = new ConstraintStorageDataType();
				CSDT.minLength = minLength;
				tmp.put(stringObject, CSDT);
			}

			else
			{
				ConstraintStorageDataType CSDT = tmp.get(stringObject);

				//makes sense
				CSDT.minLength = (CSDT.minLength > minLength) ? minLength : CSDT.minLength;
			}

		}
	}
	
	
	public static void updateMaxLength(String methodSignature, String stringObject, int maxLength)
	{
		if(!constraintStorageMap.containsKey(methodSignature))
		{
			ConstraintStorageDataType CSDT = new ConstraintStorageDataType();
			CSDT.maxLength = maxLength;

			HashMap<String, ConstraintStorageDataType> tmp = new HashMap<>();
			tmp.put(stringObject, CSDT);

		}
		else
		{
			HashMap<String, ConstraintStorageDataType> tmp = constraintStorageMap.get(methodSignature);

			if(!tmp.containsKey(stringObject))
			{
				ConstraintStorageDataType CSDT = new ConstraintStorageDataType();
				CSDT.maxLength = maxLength;
				tmp.put(stringObject, CSDT);
			}

			else
			{
				ConstraintStorageDataType CSDT = tmp.get(stringObject);

				//makes sense
				CSDT.maxLength = (CSDT.maxLength < maxLength) ? maxLength : CSDT.maxLength;
			}

		}
	}

	public static void updateContains(String methodSignature, String stringObject, String containStr)
	{
		if(!constraintStorageMap.containsKey(methodSignature))
		{
			ConstraintStorageDataType CSDT = new ConstraintStorageDataType();
			CSDT.contains.add(containStr);

			HashMap<String, ConstraintStorageDataType> tmp = new HashMap<>();
			tmp.put(stringObject, CSDT);

		}
		else
		{
			HashMap<String, ConstraintStorageDataType> tmp = constraintStorageMap.get(methodSignature);

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
			}

		}
	}

	public static void updatePrefix(String methodSignature, String stringObject, String prefixStr)
	{
		if(!constraintStorageMap.containsKey(methodSignature))
		{
			ConstraintStorageDataType CSDT = new ConstraintStorageDataType();
			CSDT.prefix.add(prefixStr);

			HashMap<String, ConstraintStorageDataType> tmp = new HashMap<>();
			tmp.put(stringObject, CSDT);

		}
		else
		{
			HashMap<String, ConstraintStorageDataType> tmp = constraintStorageMap.get(methodSignature);

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
			}

		}
	}



	public static void insetToMap(String methodSignature, String stringObject, ConstraintStorageDataType constraintStorageDataType)
	{
		if(!constraintStorageMap.containsKey(methodSignature))
		{
			HashMap<String, ConstraintStorageDataType> tmp = new HashMap<>();
			tmp.put(stringObject, constraintStorageDataType);
		}

		else
		{
			HashMap<String, ConstraintStorageDataType> tmp = constraintStorageMap.get(methodSignature);
			tmp.put(stringObject, constraintStorageDataType);		
		}
	}
}
