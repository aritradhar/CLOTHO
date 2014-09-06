package ConstraintAutomata;

import java.util.Arrays;
import java.util.Map;

import soot.ArrayType;
import soot.Body;
import soot.BodyTransformer;
import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.VoidType;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;

public class MethodClone extends BodyTransformer
{
	SootClass currentClass;
	public MethodClone()
	{
		
	}
	
	public MethodClone(SootClass currentClass)
	{
		this.currentClass = currentClass;
	}
	
	protected void internalTransform(Body body, String phaseName, Map options) 
	{
		 //System.out.println(body);
		
		Scene.v().loadClassAndSupport("java.lang.Object");
		SootMethod sm = body.getMethod(); 
		
		if(! body.getMethod().getName().equalsIgnoreCase("<init>"))
		{
			System.out.println(body.getMethod().getName());
			
			SootMethod method = new SootMethod(sm.getName()+"_untainted", 
					sm.getParameterTypes(), sm.getReturnType());
		
			JimpleBody newbody = (JimpleBody) body.getMethod().getActiveBody();
			method.setActiveBody(newbody);
	    
			System.out.println(newbody);
			currentClass.addMethod(method);
			
			
		}
		//System.out.println(body.getLocals());
	}

}
