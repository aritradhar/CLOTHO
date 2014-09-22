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

import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Pack;
import soot.PackManager;
import soot.Transform;
import soot.Unit;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.graph.pdg.EnhancedUnitGraph;
import soot.toolkits.scalar.FlowSet;

public class BoundedForword extends BodyTransformer
{
	public static void main(String[] args) 
	{
		 String []className = {"StringTest"};
		  
	        Pack jtp = PackManager.v().getPack("jtp");
	        
	        jtp.add(new Transform("jtp.boundedforward", new BoundedForword()));
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
		
		
	}

	@Override
	protected void internalTransform(Body body, String phaseName, Map options) 
	{
		
		ForwordAnalysis fwA = new ForwordAnalysis(new BriefUnitGraph(body));

		 Iterator<Unit> sIt = body.getUnits().iterator();
	     
		 while( sIt.hasNext() ) 
	     {
	    	 Stmt stmt = (Stmt) sIt.next();
	    	 FlowSet indexVariableSet = (FlowSet) fwA.getFlowBefore(stmt);
	    	 //if(indexVariableSet.size() > 0)
	    		 //System.out.println(stmt + "\n" + indexVariableSet);
	     }
		 
		 
		 System.out.println(FlowInformation.flowInfo.keySet());

		
		
	}
}
