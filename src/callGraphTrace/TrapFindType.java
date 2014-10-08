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
package callGraphTrace;

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
	
	public static void insertTrap(String methodSignature, Trap trap)
	{
		if(!trapFindType.containsKey(methodSignature))
		{
			HashSet<Trap> trapSet = new HashSet<>();
			trapSet.add(trap);
			
			trapFindType.put(methodSignature, trapSet);
		}
		else
		{
			HashSet<Trap> trapSet = trapFindType.get(methodSignature);
			trapSet.add(trap);
			trapFindType.put(methodSignature, trapSet);
		}
	}
	
	public static Unit getBeginUnitFromTrap(String methodSignature, Trap trap)
	{
		if(!trapFindType.containsKey(trap))
			throw new RuntimeException("Method subsignature not found");
		
		Iterator<Trap> iTrap = trapFindType.get(methodSignature).iterator();
		
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
	
	public static Unit getEndUnitFromTrap(String methodSignature, Trap trap)
	{
		if(!trapFindType.containsKey(trap))
			throw new RuntimeException("Method subsignature not found");
		
		Iterator<Trap> iTrap = trapFindType.get(methodSignature).iterator();
		
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
	
	public static SootClass getExeptionClassFromUnit(String methodSignature, Unit unit, PatchingChain<Unit> pc)
	{
				
		Trap trap = TrapFindType.unitExistsInTrap(methodSignature, unit, pc);
				
		return  (trap == null) ? null : trap.getException();				
	}
	
	public static Trap unitExistsInTrap(String methodSignature, Unit unit, PatchingChain<Unit> pc)
	{
		if(!trapFindType.containsKey(methodSignature))
			return null;
		
		Iterator<Trap> iTrap = trapFindType.get(methodSignature).iterator();
		
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
	
	public static void setUnitTrapInfo(String methodSignature, Unit unit, PatchingChain<Unit> pc)
	{
		Trap trap = TrapFindType.unitExistsInTrap(methodSignature, unit, pc);
		
		if(!unitTrapMap.containsKey(methodSignature))
		{
			HashMap<Unit, Trap> hm = new HashMap<>();
			
			hm.put(unit, trap);
			unitTrapMap.put(methodSignature, hm);
		}
		
		else
		{
			HashMap<Unit, Trap> hm = unitTrapMap.get(methodSignature);
			
			hm.put(unit, trap);
			unitTrapMap.put(methodSignature, hm);
			
		}
	}
}
