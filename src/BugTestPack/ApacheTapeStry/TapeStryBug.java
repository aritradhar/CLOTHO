package BugTestPack.ApacheTapeStry;

import org.apache.tapestry5.internal.test.TestableRequest;
import org.apache.tapestry5.internal.test.TestableRequestImpl;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;

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
public class TapeStryBug 
{
	private TestableRequest request;
	
	private void setupRequestFromURI(String URI)
	{ 
		String linkPath = stripContextFromPath(URI); 
		int comma = linkPath.indexOf('?'); 
		String path = comma < 0 ? linkPath : linkPath.substring(0, comma); 
		request.clear().setPath(path); 
		
		if (comma > 0) 
			decodeParametersIntoRequest(path.substring(comma + 1)); 
	}
	
	private String stripContextFromPath(String path)
    {
        String contextPath = request.getContextPath();

        if (contextPath.equals(""))
            return path;

        if (!path.startsWith(contextPath))
            throw new RuntimeException(String.format("Path '%s' does not start with context path '%s'.", path,
                    contextPath));

        return path.substring(contextPath.length());
    }
	
	private void decodeParametersIntoRequest(String queryString)
    {
        if (InternalUtils.isBlank(queryString))
            return;

        for (String term : queryString.split("&"))
        {
            int eqx = term.indexOf("=");

            String key = term.substring(0, eqx).trim();
            String value = term.substring(eqx + 1).trim();

            request.loadParameter(key, value);
        }
    }
	
	public static void main(String[] args) 
	{
		TestableRequest tr = new TestableRequestImpl("//folder");
		
		TapeStryBug tb = new TapeStryBug();
		tb.request = tr;
		
		tb.setupRequestFromURI("//folder//test?");
	}
	
}
