package cc.openhome;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
	urlPatterns = {"/avatar.view"}
	/**
	initParams = {
		@WebInitParam(name="AVATAR_DIR", value = "/avatars")
	}
	*/
)
public class Avatar extends HttpServlet {
    private String AVATAR_DIR;

    @Override
    public void init() throws ServletException {
//        this.AVATAR_DIR = getInitParameter("AVATAR_DIR");
        this.AVATAR_DIR = (String)getServletContext().getAttribute("avatars");
    }

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Õ∑œÒœ‘ æ</title>");
        out.println("</head>");
        out.println("<body>");
        for (String avatar : getServletContext()
                                .getResourcePaths(AVATAR_DIR)) {
            avatar = avatar.replaceFirst("/", "");
            out.println("<img src='" + avatar + "'>");
        }
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
