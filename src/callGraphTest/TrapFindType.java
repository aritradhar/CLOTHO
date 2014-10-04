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
package callGraphTest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import soot.Trap;
import soot.Unit;

public class TrapFindType 
{
	public static HashMap<String, HashSet<Trap>> trapFindType = new HashMap<>();
	
	public static void insertTrap(String subSignature, Trap trap)
	{
		if(!trapFindType.containsKey(subSignature))
		{
			HashSet<Trap> trapSet = new HashSet<>();
			trapSet.add(trap);
			
			trapFindType.put(subSignature, trapSet);
		}
		else
		{
			HashSet<Trap> trapSet = trapFindType.get(subSignature);
			trapSet.add(trap);
			trapFindType.put(subSignature, trapSet);
		}
	}
	
	public static Unit getBeginUnitFromTrap(String subSignature, Trap trap)
	{
		if(!trapFindType.containsKey(trap))
			throw new RuntimeException("Method subsignature not found");
		
		Iterator<Trap> iTrap = trapFindType.get(subSignature).iterator();
		
		while(iTrap.hasNext())
		{
			Trap t = iTrap.next();
			if(t.equals(trap))
			{
				return t.getBeginUnit();
			}
		}
		throw new RuntimeException("Trap missing");
	}
	
	public static Unit getEndUnitFromTrap(String subSignature, Trap trap)
	{
		if(!trapFindType.containsKey(trap))
			throw new RuntimeException("Method subsignature not found");
		
		Iterator<Trap> iTrap = trapFindType.get(subSignature).iterator();
		
		while(iTrap.hasNext())
		{
			Trap t = iTrap.next();
			if(t.equals(trap))
			{
				return t.getEndUnit();
			}
		}
		throw new RuntimeException("Trap missing");
	}
}
