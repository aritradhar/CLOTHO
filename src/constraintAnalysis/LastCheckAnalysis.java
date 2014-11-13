package constraintAnalysis;

import java.util.HashMap;
import java.util.Iterator;

import soot.Value;

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
public class LastCheckAnalysis 
{
	public static HashMap<MethodBond, OptimizationPayloadCheck> optimizationWatchDog = new HashMap<>();
	
	
	public static void setInWatchDog(MethodBond MB, OptimizationPayloadCheck OPC)
	{
		if(!optimizationWatchDog.containsKey(MB))
		{
			optimizationWatchDog.put(MB, OPC);
		}
		
		else
		{
			OptimizationPayloadCheck oldPayload = optimizationWatchDog.get(MB);
			oldPayload.args = OPC.args;
			oldPayload.isChanged = OPC.isChanged;
		}
	}
	
	public static void setUsed(Value base)
	{
		Iterator<MethodBond>it = optimizationWatchDog.keySet().iterator();
		
		while(it.hasNext())
		{
			MethodBond MB = it.next();
			if(MB.base == base)
			{
				OptimizationPayloadCheck OPC = optimizationWatchDog.get(MB);
				OPC.isChanged = true;
				optimizationWatchDog.put(MB, OPC);
			}
			/*
			 * else no change required
			 */
		}
	}
	
	public static Boolean triggerSubsqOptimization(MethodBond MB)
	{
		if(!optimizationWatchDog.containsKey(MB))
		{
			return false;
		}
		
		return optimizationWatchDog.get(MB).isChanged;
	}
	
}
