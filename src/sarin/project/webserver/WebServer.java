package sarin.project.webserver;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public final class WebServer {
	
	static ExecutorService threadPool;
	
	public static void main(String[] args) throws Exception{
		System.out.println("Inside main");
		
		int port = 1520;
		threadPool = Executors.newFixedThreadPool(10);
		ServerSocket serverSocket = new ServerSocket(port);
		
		while (true){
			Socket client = new Socket();
			client = serverSocket.accept();
		
			HttpRequest request = new HttpRequest(client);
			threadPool.execute(request);
		
		}
	
	
	}
}

