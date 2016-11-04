package cc.openhome;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/guestbook")
public class GuestBook extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String name = request.getParameter("name");
        String message = request.getParameter("message");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet GuestBook</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("Name: " + name + "<br>");
        out.println("Message: " + message + "<br>");
        out.println("</body>");
        out.println("</html>");
        out.close();
	}

}
