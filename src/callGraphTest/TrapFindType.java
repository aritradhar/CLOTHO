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

import soot.PatchingChain;
import soot.SootClass;
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
	
	public static SootClass getExeptionClassFromUnit(String subSignature, Unit unit, PatchingChain<Unit> pc)
	{
				
		Trap trap = TrapFindType.unitExistsInTrap(subSignature, unit, pc);
				
		return  (trap == null) ? null : trap.getException();				
	}
	
	public static Trap unitExistsInTrap(String subSignature, Unit unit, PatchingChain<Unit> pc)
	{
		if(!trapFindType.containsKey(subSignature))
			return null;
		
		Iterator<Trap> iTrap = trapFindType.get(subSignature).iterator();
		
		while(iTrap.hasNext())
		{
			Trap trap = iTrap.next();
			
			Unit begin = trap.getBeginUnit();
			Unit end = trap.getEndUnit();
			
			Iterator<Unit> it = pc.iterator();
			boolean flag = false;
			
			while(it.hasNext())
			{
				Unit currentUnit = it.next();
				
				if(currentUnit.equals(begin))
					flag = true;
				
				if(currentUnit.equals(end))
					break;
				
				if(flag)
				{
					if(currentUnit.equals(unit))
						return trap;
				}
			}
		}
		
		return null;
	}
	
	public static HashMap<String, HashMap<Unit, Trap>> unitTrapMap = new HashMap<>();
	
	public static void setUnitTrapInfo(String subSignature, Unit unit, PatchingChain<Unit> pc)
	{
		Trap trap = TrapFindType.unitExistsInTrap(subSignature, unit, pc);
		
		if(!unitTrapMap.containsKey(subSignature))
		{
			HashMap<Unit, Trap> hm = new HashMap<>();
			
			hm.put(unit, trap);
			unitTrapMap.put(subSignature, hm);
		}
		
		else
		{
			HashMap<Unit, Trap> hm = unitTrapMap.get(subSignature);
			
			hm.put(unit, trap);
			unitTrapMap.put(subSignature, hm);
			
		}
	}
}
