package cc.openhome;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextParameterReader implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
    	ServletContext context = sce.getServletContext();
    	String avatars = context.getInitParameter("AVATAR");
    	context.setAttribute("avatars", avatars);
    }

    public void contextDestroyed(ServletContextEvent arg0) {
    }
}
