package TestPack;

import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.util.Chain;


public class StmtAalysis extends BodyTransformer 
{
	protected void internalTransform(Body body, String phase, Map options) 
	{
		SootMethod sm=body.getMethod();
		System.out.println(sm.getName());
		JimpleBody jbody= (JimpleBody) sm.retrieveActiveBody();
		Chain<Unit> ch=jbody.getUnits();
		Iterator<Unit> it=ch.snapshotIterator();
		CaughtExceptionRef stmt= Jimple.v().newCaughtExceptionRef();
		//System.out.println(stmt);
		Chain<Local> ch1=jbody.getLocals();
		
	    System.out.println(ch1);
	}
}
