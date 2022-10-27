package convari.upload;

import java.io.InputStream;

import javax.servlet.ServletContext;

public class ImageUploaderVO {

	private ServletContext servletContext;
	private int userId;
	private long size;
	private String contentType;
	private InputStream inputStream;
	private ImageUploaderCallback callback;

	public ImageUploaderCallback getCallback() {
		return callback;
	}

	public void setCallback(ImageUploaderCallback callback) {
		this.callback = callback;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}
