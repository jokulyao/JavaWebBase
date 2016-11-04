package cc.openhome.controller;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login.do")
public class Login extends HttpServlet {
    private final String USERS = "c:/workspace/Gossip/users";
    private final String SUCCESS_VIEW = "member.view";
    private final String ERROR_VIEW = "index.html";
    
	protected void doPost(HttpServletRequest request, 
	                      HttpServletResponse response) 
	                        throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
	    String page = ERROR_VIEW;
	    if(checkLogin(username, password)) {
	        request.getSession().setAttribute("login", username);
	        page = SUCCESS_VIEW;
	    }
	    response.sendRedirect(page);
	}

    private boolean checkLogin(String username, String password) 
                          throws IOException {
    	if(username == null || "".equals(username)){
    		return false;
    	}
    	if(password == null || "".equals(password)){
    		return false;
    	}
        for (String file : new File(USERS).list()) {
        	if(!file.equals(username)){
        		continue;
        	}
            BufferedReader reader = new BufferedReader(new FileReader(USERS + "/" + file + "/profile"));
            String passwd = reader.readLine().split("\t")[1];
            if(passwd.equals(password)) {
                return true;
            }
        }
        return false;
    }
}
