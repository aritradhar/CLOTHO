package BugTestPack.ApacheWicket;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.protocol.http.servlet.ErrorAttributes;
import org.apache.wicket.request.Url;
import org.apache.wicket.util.string.Strings;
import org.springframework.mock.web.MockHttpServletRequest;

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
public class WicketBug 
{
	private HttpServletRequest httpServletRequest;
	private ErrorAttributes errorAttributes;
	
	public Url getContextRelativeUrl(String uri, String filterPrefix)
	{
		if (filterPrefix.length() > 0 && !filterPrefix.endsWith("/"))
		{
			filterPrefix += "/";
		}
		StringBuilder url = new StringBuilder();
		uri = Strings.stripJSessionId(uri);
		final int start = httpServletRequest.getContextPath().length() + filterPrefix.length() + 1;
		url.append(uri.substring(start));

		if (errorAttributes == null)
		{
			String query = httpServletRequest.getQueryString();
			if (!Strings.isEmpty(query))
			{
				url.append('?');
				url.append(query);
			}
		}

		return setParameters(Url.parse(url.toString(), getCharset()));
	}
	private Url setParameters(Url url)
	{
		url.setPort(httpServletRequest.getServerPort());
		url.setHost(httpServletRequest.getServerName());
		url.setProtocol(httpServletRequest.getScheme());
		return url;
	}
	public Charset getCharset()
	{
		return RequestUtils.getCharset(httpServletRequest);
	}
	
	public static void main(String[] args) 
	{
		MockHttpServletRequest request = new MockHttpServletRequest();
    	request.setServerName("www.example.com");
    	
    	WicketBug wb = new WicketBug();
    	wb.httpServletRequest = request;
    	
		wb.getContextRelativeUrl("a.b", "aa");
	}

}
