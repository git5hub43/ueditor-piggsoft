package com.piggsoft.ueditor;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class UeditorContextListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		String rootPath = context.getRealPath("/").replace( "\\", "/" );;
		String contextPath = context.getContextPath();
		if ( contextPath.length() > 0 ) {
			rootPath = rootPath.substring( 0, rootPath.length() - contextPath.length() );
		}
		System.setProperty("ueditor-piggsoft-rootPath", rootPath);
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
