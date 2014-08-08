package DaikonTest;

public class TestClass1 
{
	private String name;
	private String id;
	private Integer age;
	private Integer salary;
	
	public TestClass1(String name, String id)
	{
		this.name = name;
		this.id = id;
	}
	
	public void setname(String name) throws Exception
	{
		if(name.length() == 0)
			throw new Exception();
		this.name = name;
	}
	
	public void setid(String id) throws Exception
	{
		if(id.length() == 0)
			throw new Exception();
		this.id = id;
	}
	
	public void setsalary(Integer salary) throws Exception
	{
		if(salary <=0)
			throw new Exception();
		this.salary = salary;
	}
	
	public void setage(Integer age) throws Exception
	{
		if(age <=0)
			throw new Exception();
		this.age = age;
	}
	
	public float getNum()
	{
		return (float)10000/this.salary;
	}
	
	
	public static void main(String[] ag) throws Exception
	{
		TestClass1 ts = new TestClass1("", "");
		TestClass1 ts2 = new TestClass1("Aritra", "abcd");
		
		ts.setage(10);
		ts.setsalary(5);
		ts.getNum();
	}
}
