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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;

import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;


public class CallGraphDFS 
{
	public static CallGraph cg;
	
	public static ArrayList<SootMethod> callGraphDFS(CallGraph _cg, SootMethod startMethod)
	{
		cg = _cg;
		
		ArrayList<SootMethod> trace = new ArrayList<>();
		
		Queue<SootMethod> DFSq = new ArrayDeque<>();
		
		HashSet<SootMethod> visited = new HashSet<>();
		
		//initialization
		DFSq.add(startMethod);
		visited.add(startMethod);
		
		while(DFSq.peek()!=null)
		{
			SootMethod poppedMethod = DFSq.poll();
			trace.add(poppedMethod);
			
			/*
			 * in case it is a constructor or library
			 * do not add it to trace
			 * but don't explore it
			 */
			String pkg = poppedMethod.getDeclaringClass().getPackageName();
			if(poppedMethod.getName().startsWith("<") || pkg.contains("java.lang") 
					|| pkg.contains("java.util") || pkg.contains("sun.security")
					|| pkg.contains("java.security") || pkg.contains("sun.reflect")
					|| pkg.contains("sun.net") || pkg.contains("java.nio")
					|| pkg.contains("sun.misc") || pkg.contains("java.nio"))
				continue;
			
			Iterator<MethodOrMethodContext> targets = new Targets(cg.edgesOutOf(poppedMethod));
			while (targets.hasNext()) 
			{
				SootMethod targetMethod = (SootMethod)targets.next();								
				
				if(!visited.contains(targetMethod))
				{
					DFSq.add(targetMethod);
					visited.add(targetMethod);
				}
				else
					continue;
			}
		}
		
		return trace;
	}
}
