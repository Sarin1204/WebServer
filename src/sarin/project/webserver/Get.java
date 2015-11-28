package sarin.project.webserver;
import java.io.FileInputStream;

final class Get{
	String statusLine;
	String contentTypeLine;
	String entityBody;
	FileInputStream fis;
	public FileInputStream getFis() {
		return fis;
	}
	public void setFis(FileInputStream fis) {
		this.fis = fis;
	}
	boolean fileExists;
	
	public boolean getFileExists() {
		return fileExists;
	}
	public void setFileExists(boolean fileExists2) {
		this.fileExists = fileExists2;
	}
	public String getStatusLine() {
		return statusLine;
	}
	public void setStatusLine(String statusLine) {
		this.statusLine = statusLine;
	}
	public String getContentTypeLine() {
		return contentTypeLine;
	}
	public void setContentTypeLine(String contentTypeLine) {
		this.contentTypeLine = contentTypeLine;
	}
	public String getEntityBody() {
		return entityBody;
	}
	public void setEntityBody(String entityBody) {
		this.entityBody = entityBody;
	}
}