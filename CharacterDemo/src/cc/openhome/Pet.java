package cc.openhome;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/pet")
public class Pet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

    	request.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html; charset=UTF-8"); 
    	PrintWriter out = response.getWriter();
    	
        out.println("<html>");
        out.println("<head>");
        out.println("<title>感谢填写</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("联系人：<a href='mailto:"+
                request.getParameter("email") + "'>" +
                request.getParameter("user") + "</a>");
        out.println("<br>喜欢的宠物类型");
        out.println("<ul>");
        for(String type : request.getParameterValues("type")){
        	out.println("<li>" + type + "</li>");
        }
        out.println("</ul>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
