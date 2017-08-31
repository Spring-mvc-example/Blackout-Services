package com.cox.fscm.logfilter;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.logging.log4j.Logger;

public class FsServletRequestWrapper extends HttpServletRequestWrapper{
	
	private Map<String, String> headerMap;
	
	public void addHeader(String name, String value, final Logger logger){
		headerMap.put(name, new String(value));
		logger.trace("added new key/value" + name + ":" + value);
	}
	
	public FsServletRequestWrapper(HttpServletRequest request){
		super(request);
		headerMap = new HashMap<String,String>();
		
		//List all Headers
		//List all Params
		//List all Attributes.
		//List all Content.
		
		//Modify transId with New TransID.
		//List New Content.
		
	}
	
	public Enumeration<String> getHeaderNames(){
		HttpServletRequest request = (HttpServletRequest)getRequest();
		List<String> list = new ArrayList<String>();
		for( Enumeration<String> e = request.getHeaderNames() ;  e.hasMoreElements() ; )
			list.add(e.nextElement().toString());
		for( Iterator<String> i = headerMap.keySet().iterator() ; i.hasNext() ; ){
			list.add(i.next());
		}
		
		return Collections.enumeration(list);
	}

	@Override
	public String getHeader(String name){
		Object value;
		if((value = headerMap.get(""+name)) != null)
			return value.toString();
		else
			return ((HttpServletRequest)getRequest()).getHeader(name);
	}
}