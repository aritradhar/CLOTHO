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

package constraintAnalysis.stringRepair;

import java.util.HashMap;

public class EncounterRepair 
{
	public static HashMap<String, HashMap<String, Boolean>> encounterRepairMap = new HashMap<>();
	
	public static void encounterRepairSet(String methodSignature, String StringObject)
	{
		if(!encounterRepairMap.containsKey(methodSignature))
		{
			HashMap<String, Boolean> temp = new HashMap<>();
			temp.put(StringObject, true);
			encounterRepairMap.put(methodSignature, temp);
		}
		else
		{
			HashMap<String, Boolean> temp = encounterRepairMap.get(methodSignature);
			if(!temp.containsKey(StringObject))
			{
				temp.put(StringObject, true);
				encounterRepairMap.put(methodSignature, temp);
			}
		}
	}
	public static Boolean encounterRepairGet(String methodSignature, String StringObject)
	{
		if(!encounterRepairMap.containsKey(methodSignature))
			return false;
		
		else
		{
			HashMap<String, Boolean> temp = encounterRepairMap.get(methodSignature);
			if(!temp.containsKey(StringObject))
			{
				return false;
			}
			
			return true;
		}
	}
}
