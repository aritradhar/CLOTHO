package BugTestPack.EclipseAspectWeaverBcelBug;

import java.util.Collections;
import java.util.List;

import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.weaver.ConcreteTypeMunger;
import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;

public class AspectJBcel {

	protected ReferenceType resolvedTypeX;
	public JavaClass javaClass;

	private String className;
	private boolean isNested;  

	private List<ConcreteTypeMunger> typeMungers = Collections.emptyList();
	 
	
	public ResolvedType getOuterClass() {
		if (!isNested()) {
			throw new IllegalStateException("Can't get the outer class of non-nested type: " + className);
		}
		/*
// try finding outer class name from InnerClasses attribute assigned to this class
for (Attribute attr : javaClass.getAttributes()) {
if (attr instanceof InnerClasses) {
// search for InnerClass entry that has current class as inner and some other class as outer
InnerClass[] innerClss = ((InnerClasses) attr).getInnerClasses();
ConstantPool cpool = javaClass.getConstantPool();
for (InnerClass innerCls : innerClss) {

// skip entries that miss any necessary component, 0 index means "undefined", from JVM Spec 2nd ed. par. 4.7.5
if (innerCls.getInnerClassIndex() == 0 || innerCls.getOuterClassIndex() == 0) {
continue;
}

// resolve inner class name, check if it matches current class name
ConstantClass innerClsInfo = (ConstantClass) cpool.getConstant(innerCls.getInnerClassIndex());

// class names in constant pool use '/' instead of '.', from JVM Spec 2nd ed. par. 4.2
String innerClsName = cpool.getConstantUtf8(innerClsInfo.getNameIndex()).getValue().replace('/', '.');

if (innerClsName.compareTo(className) == 0) {
// resolve outer class name
ConstantClass outerClsInfo = (ConstantClass) cpool.getConstant(innerCls.getOuterClassIndex());

// class names in constant pool use '/' instead of '.', from JVM Spec 2nd ed. par. 4.2
String outerClsName = cpool.getConstantUtf8(outerClsInfo.getNameIndex()).getValue().replace('/', '.');

UnresolvedType outer = UnresolvedType.forName(outerClsName);
return outer.resolve(getResolvedTypeX().getWorld());
}
}
}
}

for (Attribute attr : javaClass.getAttributes()) { // bug339300
ConstantPool cpool = javaClass.getConstantPool();
if (attr instanceof EnclosingMethod) {
EnclosingMethod enclosingMethodAttribute = (EnclosingMethod) attr;
if (enclosingMethodAttribute.getEnclosingClassIndex() != 0) {
ConstantClass outerClassInfo = enclosingMethodAttribute.getEnclosingClass();
String outerClassName = cpool.getConstantUtf8(outerClassInfo.getNameIndex()).getValue().replace('/', '.');
UnresolvedType outer = UnresolvedType.forName(outerClassName);
return outer.resolve(getResolvedTypeX().getWorld());
}
}
}
		 */
		// try finding outer class name by assuming standard class name mangling convention of javac for this class
		int lastDollar = className.lastIndexOf('$');
		String superClassName = className.substring(0, lastDollar);
		UnresolvedType outer = UnresolvedType.forName(superClassName);
		return outer.resolve(getResolvedTypeX().getWorld());
	}

	public boolean isNested() {
		return this.isNested;
	}
	/*     */   public ReferenceType getResolvedTypeX() {
		/*  61 */     return this.resolvedTypeX;
	/*     */   }

	public static void main(String[] args) 
	{
		AspectJBcel r = new AspectJBcel();
		//JavaClass jc = new JavaClass(0, 0, "TestClass", 12, 12, 10, new ConstantPool(), null, null, null, new Attribute[]{});
		//r.javaClass = jc;
		r.isNested = true;
		r.className = "TestClass";
		r.getOuterClass();



	}
}