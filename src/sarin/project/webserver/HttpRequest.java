package sarin.project.webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

public final class HttpRequest implements Runnable
{
	final static String CRLF = "\r\n";
	Socket socket;

	public HttpRequest(Socket socket) throws Exception {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			processRequest();
		} catch(Exception e){
			
			System.out.println("Exception thrown to run "+e);
		}
		
	}
	
	private void processRequest() throws Exception
	{
		InputStream is = socket.getInputStream();
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
	
		InputStreamReader isReader = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isReader);
		String requestLine = br.readLine();
		
		System.out.println();
		System.out.println(requestLine);
		
		String headerLine = null;
		while((headerLine = br.readLine()).length() != 0){
			System.out.println(headerLine);
		}

		String type = requestLine.substring(0, requestLine.indexOf(' ')); 
		switch(type){
		
		case "GET":
				Get response = new Get();
				response = respondWithGet(requestLine);
				os.writeBytes(response.getStatusLine());
				os.writeBytes(response.getContentTypeLine());
				os.writeBytes(CRLF);
				
				if (response.getFileExists()){
					sendBytes(response.getFis(), os);
					response.getFis().close();
				}
				else{
					os.writeBytes(response.getEntityBody());
				}
			break;
		case "POST":
			StringBuilder payload = new StringBuilder();
			while(br.ready()){
				payload.append((char) br.read());
				}
				System.out.println("post payload: "+payload);
				String[] parameters = payload.toString().split("&");
				String firstname = parameters[0].split("=")[1];
				String lastname = parameters[1].split("=")[1];
				String statusLine = "HTTP/1.1 200 "+CRLF;
				/*statusLine = "File "+fileName+" does not exist.";*/
				String contentTypeLine = "Content-type:"+ "text/html" + CRLF;
				String entityBody = "<HTML>" +
						"<HEAD><TITLE>FORM POST</TITLE></HEAD>" +
						"<BODY>Firstname is: "+ firstname +"<br> Lastname is: "+ lastname+"</BODY></HTML>";
				os.writeBytes(statusLine);
				os.writeBytes(contentTypeLine);
				os.writeBytes(CRLF);
				os.writeBytes(entityBody);
				break;
				
		}
				
		os.close();
		br.close();
		socket.close();
	}
	
	private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception{
			
		byte[] buffer = new byte[1024];
		int bytes = 0;
		
		while((bytes = fis.read(buffer)) != -1){
			os.write(buffer, 0, bytes);
		}
	}
	
	private static String contentType(String fileName){
		
		if(fileName.endsWith(".htm") || fileName.endsWith(".html")){
			return "text/html";
		}
		else if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")){
			return "image/jpeg";
		}
		else if(fileName.endsWith(".gif")){
			return "image/gif";
		}
		else if(fileName.endsWith(".ico")){
			return "image/x-icon";
		}
		else if(fileName.endsWith(".css")){
			return "text/css";
		}
		else if(fileName.endsWith(".map")){
			return "application/x-navimap";
		}
		else if(fileName.endsWith(".mp4")){
			return "video/mp4";
		}
		else{
			return "application/octet-stream";
		}
	}
	
	public Get respondWithGet(String requestLine){
			StringTokenizer tokens = new StringTokenizer(requestLine);
			tokens.nextToken();
			String fileName = tokens.nextToken();
			
			fileName = "." + fileName;
			System.out.println("Filename is"+fileName);
			FileInputStream fis = null;
			boolean fileExists = true;
			
			try 
			{
				fis = new FileInputStream(fileName);
			} 
			catch(FileNotFoundException e){
				System.out.println("Inside FileNotFoundException" + e);
					fileExists = false;
			}
			
			String statusLine = null;
			String contentTypeLine = null;
			String entityBody = null;
			
			if(fileExists){
				statusLine = "HTTP/1.1 200 "+CRLF;
				contentTypeLine = "Content-type: " +
						contentType( fileName ) + CRLF;
			}
			else {
				statusLine = "HTTP/1.1 404 "+CRLF;
				/*statusLine = "File "+fileName+" does not exist.";*/
				contentTypeLine = "Content-type:"+ "text/html" + CRLF;
				entityBody = "<HTML>" +
						"<HEAD><TITLE>NOT FOUND</TITLE></HEAD>" +
						"<BODY>Not Found</BODY></HTML>";
			}
			
			Get response = new Get();
			response.setContentTypeLine(contentTypeLine);
			response.setEntityBody(entityBody);
			response.setStatusLine(statusLine);
			response.setFileExists(fileExists);
			response.setFis(fis);
			return response;
	}
	
}