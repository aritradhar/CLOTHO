//*************************************************************************************
//*********************************************************************************** *
//author Aritra Dhar																* *																		* *
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.util.Chain;


public class dataDependency extends BodyTransformer
{
	public static HashMap<Local, ArrayList<Object>> localUseMap; 
	public static HashMap<String, Local> locStringMap;
	public static HashMap<Integer, Stmt> stmtIndex;
	
	protected static ArrayList<Object> programSlice(Local loc, Body body, HashMap<Local, ArrayList<Object>> localUseMap)
	{
		ArrayList<Object> stmt = localUseMap.get(loc);
		
		HashMap []hm = stmtMapper(body);
		HashMap<Stmt, Integer> stmtMap = hm[0];	
		HashMap<Integer, Stmt> stmtMap_1 = hm[1];
		
		Integer start_index = 0, end_Index = 0, i=0;
		Iterator it = stmt.iterator();
		
		int []index = new int[stmt.size()];
		
		System.out.println(stmtMap);
		
		while(it.hasNext())
		{
			int pos = stmtMap.get(it.next());
			index[i++] = pos;
			System.out.println(pos);
		}
		Arrays.sort(index);
		start_index = index[0];
		end_Index = index[index.length - 1];
		
		System.out.println(start_index+" : "+end_Index);
		ArrayList<Object> ret = new ArrayList<>();
		/* 
		 * little bit problem with this code
		 * rewrite in different way
		 * 
		Iterator<Stmt> it_stmt = stmtMap.keySet().iterator();
		Boolean flag = false;
		while(it_stmt.hasNext())
		{
			Stmt s = it_stmt.next();
			if(stmtMap.get(s) == start_index || flag == true)
				flag = true;
			if(stmtMap.get(s) == end_Index && flag == true)
				flag = false;
			
		}*/
		
		for(int j = start_index; j<=end_Index; j++)
		{
			ret.add(stmtMap_1.get(j));
			System.out.println("stmt "+j+" : "+stmtMap_1.get(j));
		}
		
		//gives back the slice
		return ret;
	}
	//produces 2 hashmaps, one indexed as statements -> integer 
	//and another as integer -> statements
	public static HashMap[] stmtMapper(Body body)
	{
		HashMap<Stmt, Integer> stmtMap = new HashMap<Stmt, Integer>();
		HashMap<Integer, Stmt> stmtMap_1 = new HashMap<Integer, Stmt>();
		Chain units = body.getUnits();
		Iterator<Stmt> stmtIt = units.iterator();
        Integer stmtPosition = 0;
		while(stmtIt.hasNext())
        {
			stmtPosition++;
			Stmt s = stmtIt.next();
        	stmtMap.put(s, stmtPosition);
        	stmtMap_1.put(stmtPosition, s);
        }
		HashMap []hm = new HashMap[2];
		hm[0] = stmtMap;
		hm[1] = stmtMap_1;
		return hm;
	}
	
	protected void internalTransform(Body body, String phase, Map options) 
	{
		System.out.println("-----------------------------------------------------------");
		SootMethod method = body.getMethod();
        System.out.println("Current method : " + method.getSignature());
        Chain units = body.getUnits();	
        
        String signature = method.getSubSignature();
        Chain<Local> localChain = body.getLocals();       
        //test
        //System.out.println(localChain);
        
        localUseMap = new HashMap<>();
        locStringMap = new HashMap<>();
        stmtIndex = new HashMap<>();
        
        Iterator<Local> it_local = localChain.iterator();
        while(it_local.hasNext())
        {
        	Local loc = it_local.next();
        	locStringMap.put(loc.getName(), loc);
        }
        //test
        //System.out.println(locStringMap);
        
        Iterator stmtIt = units.snapshotIterator();
        while(stmtIt.hasNext())
        {
        	Stmt stmt = (Stmt) stmtIt.next();  	
        	List list_UseDefBox = stmt.getUseAndDefBoxes();
        
        	
        	Iterator it_UseDefBox = list_UseDefBox.iterator();
        	while(it_UseDefBox.hasNext())
        	{
        		Object box = it_UseDefBox.next();
        		if(box.toString().contains("LinkedRValueBox") || box.toString().contains("InvokeExprBox"))
        			continue;
        		//System.out.println(box.toString());
        		String var_st = box.toString().substring(box.toString().indexOf("(") + 1, box.toString().indexOf(")"));
        		//System.out.println(var_st);
        		/* test
        		if(locStringMap.containsKey(var_st))
        			System.out.println(var_st+"   true");
        		else
        			System.out.println(var_st+" false");
        			*/
        		
        		if(locStringMap.containsKey(var_st))
        		{
        			//if the key is not there then create a new array list and put
        			//inside the map
        			if(!localUseMap.containsKey(locStringMap.get(var_st)))
        			{
        				ArrayList<Object> al_temp = new ArrayList<>();
        				al_temp.add(stmt);
        				localUseMap.put(locStringMap.get(var_st), al_temp);
        			}
        			//otherwise retrieve the old array list corresponding the key and modify it.
        			//then put it back to the map with the particular key
        			else
        			{
        				ArrayList<Object> al_temp = localUseMap.get(locStringMap.get(var_st));
        				al_temp.add(stmt);
        				localUseMap.put(locStringMap.get(var_st), al_temp);
        			}
        		}
        	}
        	/*test
        	System.out.println(stmt);
        	System.out.println("----");
        	*/
        }
        System.out.println(localUseMap.entrySet());
        
        
        Iterator<Local> it_temp = localUseMap.keySet().iterator();
        while(it_temp.hasNext())
        {
        	Local lc = it_temp.next();
        	ArrayList<Object> ar_t = localUseMap.get(lc);
        	System.out.println("variable : "+lc.toString());
        	//System.out.println(ar_t);
        	Iterator<Object> it_s = ar_t.iterator();
        	while(it_s.hasNext())
        		System.out.println(it_s.next());
        	System.out.println("========");
        }
     
        Local L = body.getLocals().getLast();
        System.out.println(L);
        System.out.println(programSlice(L,body, localUseMap));
        
        //System.out.println(body);
        
        /*
        clean
        must be at the end of the program
        no code should be written after this cleaning code
         * 
         */
        localUseMap = new HashMap<>();
        locStringMap = new HashMap<>();    
        
	}
}