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
package boundedAnalysis;


import java.util.ArrayList;
import java.util.HashMap;

import soot.Unit;
import soot.toolkits.scalar.FlowSet;


public class FlowInformation 
{
	public static HashMap<String, ArrayList<HashMap<Unit, FlowSet>>> flowInfo = new HashMap<>();

	
	public static void SetFlowInfo(String sMethod, Unit unit, FlowSet flowSet)
	{		
		HashMap<Unit, FlowSet> flowMap = new HashMap<>();
		flowMap.put(unit, flowSet);
		
		if(!flowInfo.containsKey(sMethod))
		{
			ArrayList<HashMap<Unit, FlowSet>> al = new ArrayList<>();
			al.add(flowMap);
			flowInfo.put(sMethod, al);
		}
		else
		{
			ArrayList<HashMap<Unit, FlowSet>> al = flowInfo.get(sMethod);
			al.add(flowMap);
			flowInfo.put(sMethod, al);
		}
	}
}
