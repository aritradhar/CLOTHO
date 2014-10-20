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

public class ConstraintStorageMapDynamic 
{
	//signature -> [String value -> ConstraintStorageDataType] 
	public static HashMap<String, HashMap<String, ConstraintStorageDataTypeDynamic>> constraintStorageMapDynamic = new HashMap<>();


	public static void updateMinLength(String methodSignature, String stringObject, int minLength)
	{
		if(!constraintStorageMapDynamic.containsKey(methodSignature))
		{
			ConstraintStorageDataTypeDynamic CSDT = new ConstraintStorageDataTypeDynamic();
			CSDT.minLength = minLength;

			HashMap<String, ConstraintStorageDataTypeDynamic> tmp = new HashMap<>();
			tmp.put(stringObject, CSDT);
			
			constraintStorageMapDynamic.put(methodSignature, tmp);

		}
		else
		{
			HashMap<String, ConstraintStorageDataTypeDynamic> tmp = constraintStorageMapDynamic.get(methodSignature);

			if(!tmp.containsKey(stringObject))
			{
				ConstraintStorageDataTypeDynamic CSDT = new ConstraintStorageDataTypeDynamic();
				CSDT.minLength = minLength;
				tmp.put(stringObject, CSDT);
				
				constraintStorageMapDynamic.put(methodSignature, tmp);
			}

			else
			{
				ConstraintStorageDataTypeDynamic CSDT = tmp.get(stringObject);

				//makes sense
				if(CSDT.minLength == -1)
				{
					CSDT.minLength = minLength;
				}
				
				else
				{
					CSDT.minLength = (CSDT.minLength > minLength) ? minLength : CSDT.minLength;
				}
				
				tmp.put(stringObject, CSDT);
				constraintStorageMapDynamic.put(methodSignature, tmp);
				
			}

		}
	}
	
	
	public static void updateMaxLength(String methodSignature, String stringObject, int maxLength)
	{
		if(!constraintStorageMapDynamic.containsKey(methodSignature))
		{
			ConstraintStorageDataTypeDynamic CSDT = new ConstraintStorageDataTypeDynamic();
			CSDT.maxLength = maxLength;

			HashMap<String, ConstraintStorageDataTypeDynamic> tmp = new HashMap<>();
			tmp.put(stringObject, CSDT);

			constraintStorageMapDynamic.put(methodSignature, tmp);
		}
		else
		{
			HashMap<String, ConstraintStorageDataTypeDynamic> tmp = constraintStorageMapDynamic.get(methodSignature);

			if(!tmp.containsKey(stringObject))
			{
				ConstraintStorageDataTypeDynamic CSDT = new ConstraintStorageDataTypeDynamic();
				CSDT.maxLength = maxLength;
				tmp.put(stringObject, CSDT);
				
				constraintStorageMapDynamic.put(methodSignature, tmp);
			}

			else
			{
				ConstraintStorageDataTypeDynamic CSDT = tmp.get(stringObject);

				//makes sense
				if(CSDT.maxLength == -1)
				{
					CSDT.maxLength = maxLength;
				}
				
				else
				{
					CSDT.maxLength = (CSDT.maxLength < maxLength) ? maxLength : CSDT.maxLength;
				}
				
				tmp.put(stringObject, CSDT);
				constraintStorageMapDynamic.put(methodSignature, tmp);
			}

		}
	}

	public static void updateContains(String methodSignature, String stringObject, String containStr)
	{
		if(!constraintStorageMapDynamic.containsKey(methodSignature))
		{
			ConstraintStorageDataTypeDynamic CSDT = new ConstraintStorageDataTypeDynamic();
			CSDT.contains.add(containStr);

			HashMap<String, ConstraintStorageDataTypeDynamic> tmp = new HashMap<>();
			tmp.put(stringObject, CSDT);
			
			constraintStorageMapDynamic.put(methodSignature, tmp);
		}
		else
		{
			HashMap<String, ConstraintStorageDataTypeDynamic> tmp = constraintStorageMapDynamic.get(methodSignature);

			if(!tmp.containsKey(stringObject))
			{
				ConstraintStorageDataTypeDynamic CSDT = new ConstraintStorageDataTypeDynamic();
				CSDT.contains.add(containStr);
				tmp.put(stringObject, CSDT);
			}

			else
			{
				ConstraintStorageDataTypeDynamic CSDT = tmp.get(stringObject);
				CSDT.contains.add(containStr);
				tmp.put(stringObject, CSDT);
				
			}	
			constraintStorageMapDynamic.put(methodSignature, tmp);
		}
	}

	public static void updatePrefix(String methodSignature, String stringObject, String prefixStr)
	{
		if(!constraintStorageMapDynamic.containsKey(methodSignature))
		{
			ConstraintStorageDataTypeDynamic CSDT = new ConstraintStorageDataTypeDynamic();
			CSDT.prefix.add(prefixStr);

			HashMap<String, ConstraintStorageDataTypeDynamic> tmp = new HashMap<>();
			tmp.put(stringObject, CSDT);
			constraintStorageMapDynamic.put(methodSignature, tmp);
		}
		else
		{
			HashMap<String, ConstraintStorageDataTypeDynamic> tmp = constraintStorageMapDynamic.get(methodSignature);

			if(!tmp.containsKey(stringObject))
			{
				ConstraintStorageDataTypeDynamic CSDT = new ConstraintStorageDataTypeDynamic();
				CSDT.prefix.add(prefixStr);
				tmp.put(stringObject, CSDT);
			}

			else
			{
				ConstraintStorageDataTypeDynamic CSDT = tmp.get(stringObject);
				CSDT.prefix.add(prefixStr);
				tmp.put(stringObject, CSDT);
			}
			constraintStorageMapDynamic.put(methodSignature, tmp);

		}
	}
	
	public static void updateEquals(String methodSignature, String stringObject, String equlalStr)
	{
		if(!constraintStorageMapDynamic.containsKey(methodSignature))
		{
			ConstraintStorageDataTypeDynamic CSDT = new ConstraintStorageDataTypeDynamic();
			CSDT.equals.add(equlalStr);

			HashMap<String, ConstraintStorageDataTypeDynamic> tmp = new HashMap<>();
			tmp.put(stringObject, CSDT);
			constraintStorageMapDynamic.put(methodSignature, tmp);
		}
		else
		{
			HashMap<String, ConstraintStorageDataTypeDynamic> tmp = constraintStorageMapDynamic.get(methodSignature);

			if(!tmp.containsKey(stringObject))
			{
				ConstraintStorageDataTypeDynamic CSDT = new ConstraintStorageDataTypeDynamic();
				CSDT.equals.add(equlalStr);
				tmp.put(stringObject, CSDT);
			}

			else
			{
				ConstraintStorageDataTypeDynamic CSDT = tmp.get(stringObject);
				CSDT.equals.add(equlalStr);
				tmp.put(stringObject, CSDT);
			}
			constraintStorageMapDynamic.put(methodSignature, tmp);

		}
	}

	/*
	 * Not required for string
	public static ConstraintStorageDataType CSDTget(Value value, HashMap<Value, ConstraintStorageDataType> CSDTmap)
	{
		int argHashCode = value.hashCode();
		
		if(CSDTmap == null)
			return null;

		for(Value val : CSDTmap.keySet())
		{
			int valHashCode = val.hashCode();

			if((valHashCode == argHashCode) && (val.toString().equals(value.toString())) &&  (val.getType().toString().equals(value.getType().toString())))
			{
				return CSDTmap.get(val);
			}
		}
		
		return null;
	}
*/

	public static void insetToMap(String methodSignature, String stringObject, ConstraintStorageDataTypeDynamic constraintStorageDataType)
	{
		if(!constraintStorageMapDynamic.containsKey(methodSignature))
		{
			HashMap<String, ConstraintStorageDataTypeDynamic> tmp = new HashMap<>();
			tmp.put(stringObject, constraintStorageDataType);
		}

		else
		{
			HashMap<String, ConstraintStorageDataTypeDynamic> tmp = constraintStorageMapDynamic.get(methodSignature);
			tmp.put(stringObject, constraintStorageDataType);		
		}
	}
}
