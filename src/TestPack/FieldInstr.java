package TestPack;

import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.jimple.Jimple;

public class FieldInstr extends BodyTransformer
{
	public static int counter = 0;
	protected void internalTransform(Body jbody, String phaseName, Map options) 
	{
		SootField sf = new SootField("variablList_Field",RefType.v("java.util.ArrayList"), Modifier.STATIC);
		
		SootClass s = Scene.v().loadClassAndSupport("java.lang.Object");
		counter++;		
		
		if(counter == 1)
			s.addField(sf);
		
		jbody.validate();
	}
}
