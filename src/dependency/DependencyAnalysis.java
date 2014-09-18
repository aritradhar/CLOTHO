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
package dependency;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import soot.Body;
import soot.BodyTransformer;
import soot.Pack;
import soot.PackManager;
import soot.PatchingChain;
import soot.Transform;
import soot.Unit;
import soot.options.Options;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.graph.pdg.EnhancedUnitGraph;
import soot.toolkits.graph.pdg.HashMutablePDG;
import soot.toolkits.graph.pdg.PDGNode;
import soot.toolkits.graph.pdg.ProgramDependenceGraph;


public class DependencyAnalysis extends BodyTransformer
{
	public static void main(String[] args) 
	{
        
        String []className = {"StringTest"};
  
        Pack jtp = PackManager.v().getPack("jtp");

        jtp.add(new Transform("jtp.instrumenter", new DependencyAnalysis()));
        Options.v().setPhaseOption("jb", "use-original-names:true");
        
        Scanner s = new Scanner(System.in);
        String st = s.next();
        
        if(st.equalsIgnoreCase("j"))
        	Options.v().set_output_format(Options.output_format_jimple);
        
        if(st.equalsIgnoreCase("c"))
        	Options.v().set_output_format(Options.output_format_class);     
        
        soot.Main.main(className);
	}

	/* (non-Javadoc)
	 * @see soot.BodyTransformer#internalTransform(soot.Body, java.lang.String, java.util.Map)
	 */
	@Override
	protected void internalTransform(Body body, String phaseName, Map options) 
	{
		UnitGraph unitGraph = new EnhancedUnitGraph(body);
		//System.out.println(unitGraph);
		ProgramDependenceGraph pdg = new HashMutablePDG(unitGraph);
		
		FileWriter fwrite = null;
		try
		{
			fwrite = new FileWriter("PDG.txt", true);
		
			fwrite.append(pdg.toString());
			fwrite.close();
		}
		catch(IOException ex)
		{
		}
		
		Iterator<PDGNode> it = pdg.iterator();
		
		while(it.hasNext())
		{
			PDGNode pdgNode = it.next();
			
			System.out.println(pdgNode.toString() +" ====> " + pdg.getDependents(pdgNode)+"\n--------------------------");
		}
		
	}
}
