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

package ConstraintAutomata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.Pack;
import soot.PackManager;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.SootResolver;
import soot.Transform;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import soot.options.Options;


public class MethodRefChanger extends BodyTransformer
{
	
	private SootClass getProperClass(Value v)
	{
		Type t = v.getType();
		
		if(t instanceof IntType)
			return null;
		
		else if(t instanceof FloatType)
			return null;
		
		else if(t instanceof DoubleType)
			return null;
		
		else if(t instanceof LongType)
			return null;
		
		else if(t instanceof ShortType)
			return null;
		
		else if(t instanceof CharType)
			return null;
		
		else if(t instanceof IntType)
			return null;
		
		else if(t instanceof ByteType)
			return null;
		
		else
		{
			RefType ref = (RefType) t;
			return ref.getSootClass();
		}
	}
	
	private SootMethod processAssignStatement(VirtualInvokeExpr virtualInvokeExpr)
	{
		List<Value> args = virtualInvokeExpr.getArgs();
		System.out.println(args);
		SootMethod calledMethod = virtualInvokeExpr.getMethod();
		
		List<Type> calledMethodArgTypes = calledMethod.getParameterTypes();
		
		Value baseObject = virtualInvokeExpr.getBase();
		//System.out.println(baseObject.getType());
		
		
		if(getProperClass(baseObject) == null)
			return null;
		
		SootClass baseClass = getProperClass(baseObject);
		//Retrieve the patched version of the method reference from the populated map
		
		//System.out.println(baseClass.getName());
		
		HashSet<SootMethod> hs = Constants.patchedMethodMap.get(baseClass);
		
		//get the patched twin
		//also match the argument list types
		Iterator<SootMethod> itm = hs.iterator();
		
		ArrayList<SootMethod> alm = new ArrayList<>();
		
		while(itm.hasNext())
		{
			SootMethod sm = itm.next();
			List<Type> argTypes = sm.getParameterTypes();
			
			String patchedMethodNameFromCalledMethod = calledMethod.getName().concat(Constants.patcheClause);
			
			//if collide them get them in a list for parameter type check
			if(sm.getName().equals(patchedMethodNameFromCalledMethod))
			{
				alm.add(sm);
			}
		}
		
		return null;
		
	}
	
	@Override
	protected void internalTransform(Body body, String phaseName, Map options)
	{		
		SootMethod sMethod = body.getMethod();
		System.out.println("<<current method : " + sMethod.getName() + " >>");
		
		PatchingChain<Unit> pc = body.getUnits();
		
		Iterator<Unit> it = pc.snapshotIterator();
		//System.out.println(body);
		
		while(it.hasNext())
		{
			Unit unit = it.next();
			Stmt stmt = (Stmt) unit;
			
			boolean flag = false;
			/*
			only look at the function call which may happen as Invoke statement
			of which may be at the left hand side of an assignment statement
			flag = true if it has method call. I also ignore any special Invoke
			*/
			
			if(stmt instanceof InvokeStmt)
			{
				InvokeExpr invokeExpr = stmt.getInvokeExpr();
				
				if(invokeExpr instanceof SpecialInvokeExpr)	
				{
					continue;
				}
				
				if(invokeExpr instanceof VirtualInvokeExpr)
				{
					System.out.println(stmt);
					flag = true;
					
					VirtualInvokeExpr virtualInvokeExpr = (VirtualInvokeExpr) invokeExpr;
					List<Value> args = virtualInvokeExpr.getArgs();
					SootMethod calledMethod = virtualInvokeExpr.getMethod();
					Value baseObject = virtualInvokeExpr.getBase();
					//System.out.println(baseObject.getType());
					List<Type> calledMethodArgTypes = calledMethod.getParameterTypes();
					
					//basic type exit
					if(getProperClass(baseObject) == null)
						continue;
					
					SootClass baseClass = getProperClass(baseObject);
					//Retrieve the patched version of the method reference from the populated map
					
					HashSet<SootMethod> hs = Constants.patchedMethodMap.get(baseClass);
					
					//not in patched list
					if(hs == null)
						continue;
					
					//System.out.println(hs);
					
					//get the patched twin
					//also match the argument list types
					Iterator<SootMethod> itm = hs.iterator();
					
					ArrayList<SootMethod> alm = new ArrayList<>();
					
					while(itm.hasNext())
					{
						SootMethod sm = itm.next();
						//List<Type> argTypes = sm.getParameterTypes();
						
						String patchedMethodNameFromCalledMethod = calledMethod.getName().concat(Constants.patcheClause);
						
						//if collide them get them in a list for parameter type check
						if(sm.getName().equals(patchedMethodNameFromCalledMethod))
						{
							alm.add(sm);
						}
					}
					
					itm = alm.iterator();
					while(itm.hasNext())
					{
						SootMethod sm = itm.next();
						List<Type> argTypes = sm.getParameterTypes();
						if(calledMethodArgTypes.size() == argTypes.size())
						{
							int counter = 0;
							for(int i = 0; i<calledMethodArgTypes.size();i++)
							{
								if(calledMethodArgTypes.get(i).toString().equals(argTypes.get(i).toString()))
									counter++;
							}
							if(counter == calledMethodArgTypes.size())
							{
								System.out.println("Called " + sm.getSubSignature());
								break;
							}
						}
					}
					
					//delete old
					//pc.remove(unit);
				}
			}
			
			if(stmt instanceof AssignStmt)
			{
				AssignStmt astmt = (AssignStmt) stmt;
				Value value = astmt.getRightOp();
				
				if(value instanceof VirtualInvokeExpr)
				{				
					System.out.println(stmt);
					flag = true;
					
					VirtualInvokeExpr virtualInvokeExpr = (VirtualInvokeExpr) value;
					List<Value> args = virtualInvokeExpr.getArgs();
					//System.out.println(args);
					SootMethod calledMethod = virtualInvokeExpr.getMethod();
					
					List<Type> calledMethodArgTypes = calledMethod.getParameterTypes();
					
					Value baseObject = virtualInvokeExpr.getBase();
					//System.out.println(baseObject.getType());
					
					
					if(getProperClass(baseObject) == null)
						continue;
					
					SootClass baseClass = getProperClass(baseObject);
					//Retrieve the patched version of the method reference from the populated map
					
					//System.out.println(baseClass.getName());
					
					HashSet<SootMethod> hs = Constants.patchedMethodMap.get(baseClass);
					
					//not in patched list
					if(hs == null)
						continue;
					
					//get the patched twin
					//also match the argument list types
					Iterator<SootMethod> itm = hs.iterator();
					
					ArrayList<SootMethod> alm = new ArrayList<>();
					
					while(itm.hasNext())
					{
						SootMethod sm = itm.next();
						//List<Type> argTypes = sm.getParameterTypes();
						
						String patchedMethodNameFromCalledMethod = calledMethod.getName().concat(Constants.patcheClause);
						
						//if collide them get them in a list for parameter type check
						if(sm.getName().equals(patchedMethodNameFromCalledMethod))
						{
							alm.add(sm);
						}
					}
					
					itm = alm.iterator();
					while(itm.hasNext())
					{
						SootMethod sm = itm.next();
						List<Type> argTypes = sm.getParameterTypes();
						if(calledMethodArgTypes.size() == argTypes.size())
						{
							int counter = 0;
							for(int i = 0; i<calledMethodArgTypes.size();i++)
							{
								if(calledMethodArgTypes.get(i).toString().equals(argTypes.get(i).toString()))
									counter++;
							}
							if(counter == calledMethodArgTypes.size())
							{
								System.out.println("Called " + sm.getSubSignature());
								break;
							}
						}
					}
					
					//System.out.println(alm);
					
				
					
				}
			}
			
		}
		
		//System.out.println(Constants.patchedMethodMap.entrySet());

	}
	
}

class Main_refChanger
{
	public static void main(String[] args) 
	{	            
        String []className = {"Test_Caller"};
        
        /* add a phase to transformer pack by call Pack.add */
        Pack jtp = PackManager.v().getPack("jtp");

        jtp.add(new Transform("jtp.instrumenter", new MethodRefChanger()));
        Options.v().setPhaseOption("jb", "use-original-names:true");
        
        /*
        Options.v().set_whole_program(true);
        Options.v().set_main_class(className[0]);
        */
        SootResolver.v().resolveClass("TestPack.Test2", SootClass.SIGNATURES);
        SootResolver.v().resolveClass("java.util.Comparator", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.StringBuilder",SootClass.SIGNATURES);
        
        
        // load the patched methods
        
        PatchedMethodLoader pml = new PatchedMethodLoader(Constants.methodListSileName);
        
        soot.Main.main(className);   
	}
}
