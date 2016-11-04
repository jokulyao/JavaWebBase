package cc.openhome;

import java.io.*;
import java.util.zip.GZIPOutputStream;
import javax.servlet.*;
import javax.servlet.http.*;

public class CompressionWrapper extends HttpServletResponseWrapper {
	private GZipServletOutputStream gzServletOutputStream;
	private PrintWriter printWriter;

	public CompressionWrapper(HttpServletResponse resp) {
		super(resp);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (printWriter != null) {
			throw new IllegalStateException();
		}
		if (gzServletOutputStream == null) {
			gzServletOutputStream = new GZipServletOutputStream(getResponse()
					.getOutputStream());
		}
		return gzServletOutputStream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (gzServletOutputStream != null) {
			throw new IllegalStateException();
		}

		if (printWriter == null) {
			gzServletOutputStream = new GZipServletOutputStream(getResponse()
					.getOutputStream());
			OutputStreamWriter osw = new OutputStreamWriter(
					gzServletOutputStream, getResponse().getCharacterEncoding());
			printWriter = new PrintWriter(osw);
		}
		return printWriter;
	}

	@Override
	public void setContentLength(int len) {
	}

	public GZIPOutputStream getGZIPOutputStream() {
		if (this.gzServletOutputStream == null) {
			return null;
		}
		return this.gzServletOutputStream.getGzipOutputStream();
	}
}