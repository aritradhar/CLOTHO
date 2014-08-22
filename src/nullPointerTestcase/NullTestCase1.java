package nullPointerTestcase;

class SalaryStructure
{
	private Double salary;
	private String grade;
	
	SalaryStructure(Double salary)
	{
		this.salary = salary;
		this.setGreade();
	}
	
	private void setGreade()
	{
		if(this.salary < 10)
			this.grade = "C";
		
		else if(this.salary >= 10 && this.salary < 100)
			this.grade = "B";
		
		else if(this.salary > 100)
			this.grade = "A";
		
		else
			this.grade = "O";
		
	}
	
	public String getGrade()
	{
		return this.grade;
	}
}

class Employee 
{
	private String name;
	private String position;
	private Double basic;
	private Double salary;
	
	public Employee(String name)
	{
		this.name = name;
		this.position = null;
		this.basic = null;
	}
	
	public Employee(String name, String position)
	{
		this.name = name;
		this.position = position;
		this.basic = null;
	}
	
	public Employee(String name, String position, Double basic)
	{
		this.name = name;
		this.position = position;
		this.basic = basic;
		this.salary = 2*this.basic;
	}
	
	public Double getSalary()
	{
		return this.salary;
	}
}

public class NullTestCase1
{
	public static void main(String[] ar)
	{
		Employee em = new Employee("Aritra", "programmer");

		Double sal = 0.0;
		try
		{
			sal = em.getSalary() + 5.0;
		}
		catch(NullPointerException ex)
		{
			System.err.println("<Exception patch>");
			//get proper constructor
			em = new Employee("Aritra", "programmer",100.0);
			sal = em.getSalary();
		}
		
		catch(RuntimeException ex)
		{
			
		}
		SalaryStructure ss = new SalaryStructure(sal);
		System.out.println(ss.getGrade());
	}
}
