package sarin.project.webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadPoolServer extends Thread {
	
	 public final static int defaultPort = 2347;
	  ServerSocket theServer;
	  static int numberOfThreads = 10;
	  
	  
	  public ThreadPoolServer(ServerSocket ss) {
		// TODO Auto-generated constructor stub
		  this.theServer = ss;
	}

	public static void main(String[] args) {
	  
	    int port = defaultPort;
	    
	    try {
	      port = Integer.parseInt(args[0]);
	    }
	    catch (Exception ex) {
	    }
	    if (port <= 0 || port >= 65536) port = defaultPort;
	    
	    try {
	      ServerSocket ss = new ServerSocket(port);
	      for (int i = 0; i < numberOfThreads; i++) {
	    	  ThreadPoolServer tps = new ThreadPoolServer(ss); 
	        tps.start();
	      }
	    }
	    catch (IOException ex) {
	      System.err.println(ex);
	    }

	  }
	  
	  public void run() {
	    while (true) {
	      try {
	        Socket s = theServer.accept();
	        OutputStream out = s.getOutputStream();
	        InputStream in = s.getInputStream();
	        System.out.println("Inside true 1");
	        while (true) {
	        	System.out.println("Inside true 2");
	          int n = in.read();
	          if (n == -1) break;
	          out.write(n);
	          out.flush();
	        } // end while
	      } // end try
	      catch (IOException ex) {
	      }       
	    } // end while
	    
	  } // end run

}
