package cc.openhome;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/encoding")
public class EncodingServlet extends HttpServlet {
	// ≤‚ ‘ ß∞‹
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
    	String name = request.getParameter("nameGet");
//    	name = new String(name.getBytes("UTF-8"), "UTF-8");
    	name = new String(name.getBytes("ISO-8859-1"), "BIG5");
        System.out.println("GET: " + name);
    }

    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("BIG5");
    	String name = request.getParameter("namePost");
        System.out.println("POST: " + name);
    }
}
