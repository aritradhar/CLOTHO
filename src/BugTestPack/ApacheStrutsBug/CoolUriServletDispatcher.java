package BugTestPack.ApacheStrutsBug;
/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
/*
 * Created on 2/10/2003
 *
 */


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * A custom servlet dispatcher that maps servlet paths to actions. The format is the following:
 * <p/>
 * <p/>
 * <ul><tt>http://HOST/ACTION_NAME/PARAM_NAME1/PARAM_VALUE1/PARAM_NAME2/PARAM_VALUE2</tt></ul>
 * <p/>
 * You can have as many parameters you'd like to use. Alternatively the URL can be shortened to the following:
 * <p/>
 * <ul><tt>http://HOST/ACTION_NAME/PARAM_VALUE1/PARAM_NAME2/PARAM_VALUE2</tt></ul>
 * <p/>
 * This is the same as:
 * <p/>
 * <ul><tt>http://HOST/ACTION_NAME/ACTION_NAME/PARAM_VALUE1/PARAM_NAME2/PARAM_VALUE2</tt></ul>
 * <p/>
 * Suppose for example we would like to display some articles by id at using the following URL sheme:
 * <p/>
 * <ul><tt>http://HOST/article/ID</tt></ul>
 * <p/>
 * All we would have to do is to map the <tt>/article/*</tt> to this servlet and declare in WebWork an
 * action named <tt>article</tt>. This action would set its <tt>article</tt> parameter <tt>ID</tt>.
 *
 * @author <a href="mailto:cameron@datacodex.net">Cameron Braid</a>
 * @author <a href="mailto:jerome.bernard@xtremejava.com">Jerome Bernard</a>
 */
public class CoolUriServletDispatcher{
    //~ Constructors ///////////////////////////////////////////////////////////

    public CoolUriServletDispatcher() {
        super();
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Parses the servlet path for the action name and parameters (see the class description for an
     * explanation of the format). Once parsed this method passes off control to the
     * {@link #serviceAction(HttpServletRequest, HttpServletResponse, String, String, Map, Map, Map, Map)}
     * method for action execution.
     *
     * @param request  the http servlet request.
     * @param response the http servlet response.
     * @throws ServletException if an error occurs parsing the action name or parameters or if
     *                          an action occurs whene executing the action.
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
    	String actionName = request.getServletPath().substring(1, request.getServletPath().indexOf('/', 1));
    	System.out.println("First success");
        Map parameters = new HashMap();
  	
        try {
        	
        	int t1 = request.getServletPath().indexOf('/', 1);
        	System.out.println("Second" );
        	System.out.println(t1);
        	System.out.println(request.getServletPath());
        	System.out.println(request.getServletPath().length());
        	String t = request.getServletPath().substring(t1);
        	System.out.println("Second : " + t);
            StringTokenizer st = new StringTokenizer(t, "/");
            boolean isNameTok = true;
            String paramName = null;
            String paramValue = null;

            // check if we have the first parameter name
            if ((st.countTokens() % 2) != 0) {
                isNameTok = false;
                paramName = actionName;
            }

            while (st.hasMoreTokens()) {
                if (isNameTok) {
                    paramName = URLDecoder.decode(st.nextToken());
                    isNameTok = false;
                } else {
                    paramValue = URLDecoder.decode(st.nextToken());

                    if ((paramName != null) && (paramName.length() > 0)) {
                        parameters.put(paramName, paramValue);
                    }

                    isNameTok = true;
                }
            }
        } catch (Exception e) 
        {
        	System.out.println("Caught here");
            e.printStackTrace();
            
        }
    }
    
    public static void main(String[] args) throws ServletException 
    {
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	request.setServerName("www.example.com");

    	
    	MockHttpServletResponse response = new MockHttpServletResponse();
    	
    	new CoolUriServletDispatcher().service(request, response);
	}
}
