package server;

import java.io.*;
import java.net.*;

public class Server extends Thread{
    private ServerSocket server;
    public Server()
    {
    	try {
			server = new ServerSocket(8000);
			System.out.println("Server is running");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) throws Exception {
        Server sv = new Server();
        sv.start();  
    } 
    public void run()
    {
    	while(true)
    	{
    		try {
				Socket socket = server.accept();
//				client = new ClientHandler(socket);
//				client.start();
				new ClientHandler(socket).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}
