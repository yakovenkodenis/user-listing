package ua.nure.yakovenko.Task2;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.PropertyConfigurator;


@WebListener
public class ContextListener implements ServletContextListener {

    public ContextListener() {
    }


    /**
     * Initialise Log4j once, when the application starts is being started.
     */
    public void contextInitialized(ServletContextEvent event)  { 
         ServletContext context = event.getServletContext();
         String log4jConfigFile = context.getInitParameter("log4j-config-location");
         String fullPath = context.getRealPath("") + File.separator + log4jConfigFile;
         
         PropertyConfigurator.configure(fullPath);
    }


    public void contextDestroyed(ServletContextEvent event)  { 
    }
	
}
