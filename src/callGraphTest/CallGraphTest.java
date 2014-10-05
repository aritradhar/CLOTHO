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

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootMethod;
import soot.Transform;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;
import soot.options.Options;

public class CallGraphTest 
{
	public static void main(String[] args) 
	{
		// create a list from args
		List<String> argsList = new ArrayList<String>(Arrays.asList(args));
		// add on the following arguments
		argsList.addAll(Arrays.asList(new String[]{"-w", "StringTest"}));
		// PackManager manages the packs containing the various phases and their options
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.myTransform", new SceneTransformer() {
			
			@Override
			protected void internalTransform(String phaseName, Map options) 
			{
				
				CHATransformer.v().transform();
				SootMethod src = Scene.v().getMainClass().getMethodByName("main");
				ArrayList<SootMethod> entryPoints = new ArrayList<SootMethod>();
				SootMethod src1 = Scene.v().getMainClass().getMethodByName("main");
				entryPoints.add(src1);
				Scene.v().setEntryPoints(entryPoints);
				CallGraph cg = Scene.v().getCallGraph();
				
				try
				{
					FileWriter fw = new FileWriter("CGDump.txt");
					fw.append(cg.toString());
					fw.close();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
				SootMethod testM = Scene.v().getMainClass().getMethodByName("foo");
				
				System.out.println(CallGraphDFS.callGraphDFS(cg, Scene.v().getMainClass().getMethodByName("foo")) + "\n----");
				
				
				Iterator<MethodOrMethodContext> targets1 = new Targets(cg.edgesOutOf(testM));
				while (targets1.hasNext()) 
				{
					SootMethod tgt = (SootMethod)targets1.next();
					System.out.println(testM + " may call " + tgt);
				}
				
				Iterator<MethodOrMethodContext> targets = new Targets(cg.edgesOutOf(src));
				while (targets.hasNext()) 
				{
					SootMethod tgt = (SootMethod)targets.next();
					System.out.println(src + " may call " + tgt);
				}
				
			}
		}));
		
		Options.v().set_exclude(Arrays.asList(new String[]{"java", "sun", "java.lang"}));
		args = argsList.toArray(new String[0]);
		soot.Main.main(args);
	}
}
