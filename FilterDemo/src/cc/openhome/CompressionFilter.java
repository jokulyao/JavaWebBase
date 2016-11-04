package cc.openhome;

import java.io.*;
import java.util.zip.GZIPOutputStream;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebFilter;

@WebFilter(filterName = "CompressionFilter", urlPatterns = { "/*" })
public class CompressionFilter implements Filter {
	public void init(FilterConfig filterConfig) {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String encodings = req.getHeader("accept-encoding");
		if ((encodings != null) && (encodings.indexOf("gzip") > -1)) {
			CompressionWrapper responseWrapper = new CompressionWrapper(res);
			responseWrapper.setHeader("content-encoding", "gzip");

			chain.doFilter(request, responseWrapper);

			GZIPOutputStream gzipOutputStream = responseWrapper.getGZIPOutputStream();
			if (gzipOutputStream != null) {
				gzipOutputStream.finish();
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	public void destroy() {
	}
}