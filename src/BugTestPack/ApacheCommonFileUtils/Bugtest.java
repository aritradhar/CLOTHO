package BugTestPack.ApacheCommonFileUtils;

public class Bugtest 
{
	public static void main(String[] args) 
	{
		 String path = "/foo.xml"; 
		 ApacheBug.getPathNoEndSeparator(path);
	}
}
