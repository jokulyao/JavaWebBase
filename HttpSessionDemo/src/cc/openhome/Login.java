package cc.openhome;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login.do")
public class Login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
        String user = request.getParameter("user");
        String passwd = request.getParameter("passwd");
        if ("caterpillar".equals(user) && "123456".equals(passwd)) {
            request.getSession().setAttribute("login", user);
            request.getRequestDispatcher("/user.view").forward(request,
                    response);
        } else {
            response.sendRedirect("login.html");
        }
    }
}
