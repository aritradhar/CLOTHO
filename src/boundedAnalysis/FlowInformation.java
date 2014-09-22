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
import java.util.concurrent.ConcurrentHashMap;

import soot.Unit;
import soot.toolkits.scalar.FlowSet;


public class FlowInformation 
{
	public static ConcurrentHashMap<String, ArrayList<HashMap<Unit, FlowSet>>> flowInfo = new ConcurrentHashMap<>();

	
	public void SetFlowInfo(String sMethod, Unit unit, FlowSet flowSet)
	{
		HashMap<Unit, FlowSet> flowMap = new HashMap<>();
		flowMap.put(unit, flowSet);
		
		if(!flowInfo.contains(sMethod))
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
