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
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.html.parser.Entity;

import soot.Body;
import soot.BodyTransformer;
import soot.Pack;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.graph.pdg.EnhancedUnitGraph;
import soot.toolkits.scalar.FlowSet;

public class BoundedFarword extends BodyTransformer
{
	public static void main(String[] args) 
	{		
		
		
		 	String []className = {"StringTest"};
		  
	        Pack jtp = PackManager.v().getPack("jtp");
	        
	        jtp.add(new Transform("jtp.boundedforward", new BoundedFarword()));
	        Options.v().setPhaseOption("jb", "use-original-names:true");
	     
	        /* DEBUG
	        Scanner s = new Scanner(System.in);
	        String st = s.next();
	        */
	        String st = "c";
	        
	        if(st.equalsIgnoreCase("j"))
	        	Options.v().set_output_format(Options.output_format_jimple);
	        
	        if(st.equalsIgnoreCase("c"))
	        	Options.v().set_output_format(Options.output_format_class);     
	        
	        soot.Main.main(className);
		
	        /*
	        Iterator<Entry<String, ArrayList<HashMap<Unit, FlowSet>>>> it =  FlowInformation.flowInfo.entrySet().iterator();
	        
	        while(it.hasNext())
	        {
	        	Entry<String, ArrayList<HashMap<Unit, FlowSet>>> entry = it.next();
	        	
	        	Iterator<HashMap<Unit, FlowSet>> it_in = entry.getValue().iterator();
	        	System.out.println(entry.getKey()+"\n====================================================================");
	        	
	        	while(it_in.hasNext())
	        	{
	        		System.out.println(it_in.next().entrySet());
	        	}
	        }
	        */
	}

	@Override
	protected void internalTransform(Body body, String phaseName, Map options) 
	{
		
		ForwardAnalysis fwA = new ForwardAnalysis(new BriefUnitGraph(body));

		 Iterator<Unit> sIt = body.getUnits().iterator();
	     
		 while( sIt.hasNext() ) 
	     {
	    	 Stmt stmt = (Stmt) sIt.next();
	    	 FlowSet indexVariableSet = (FlowSet) fwA.getFlowBefore(stmt);
	    	 //if(indexVariableSet.size() > 0)
	    		 //System.out.println(stmt + "\n" + indexVariableSet);
	     }
		 
		 
		

		
		
	}
}
