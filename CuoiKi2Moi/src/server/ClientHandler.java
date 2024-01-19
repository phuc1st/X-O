package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import account.connect;
import tags.xml;

import tags.tags;

class ClientHandler extends Thread {
    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dos;
    private String clientName;
    private String status;
    private connect conDatabase = new connect();
    private static ArrayList<ClientHandler> clients = new ArrayList<>();

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.status = tags.ONLINE;
        clients.add(this);
        din = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public DataInputStream getDin() {
		return din;
	}

	public void setDin(DataInputStream din) {
		this.din = din;
	}

	public DataOutputStream getDos() {
		return dos;
	}

	public void setDos(DataOutputStream dos) {
		this.dos = dos;
	}
	
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void run() {
        try {
            while (true) {
                String message = din.readUTF();                   
                System.out.println(message);
                if (message.contains(tags.NEW_PLAYER_CONNECT_TAG) || message.contains(tags.NEW_MATCH_TAG))
                	this.clientName = tags.getSenderName(message);
                if(message.contains(tags.SEND_ALL_TAG))
                {	
                	updatestatus(message);	
                	sendMessageToAll(tags.getSenderName(message), message);
                	if(message.contains(tags.NEW_PLAYER_CONNECT_TAG))
                	{                		
                		sendAllPlayerToNewPlayer();
                		System.out.println("WHITE");
                	}
                }
                else
                	if(message.contains("<?xml"))
                	{
                		System.out.println(message);
                		sendPositionMarked(message);
                	}
                	else
                		if(message.contains(tags.NEW_MATCH_TAG))
                			updatestatus(message);
                		else
                			if(message.contains(tags.WIN_TAG))
                			{
                				conDatabase.updateNumWin(tags.getReceiverName(message));
                				sendMessage(tags.getReceiverName(message), message);
                			}
                			else                				
                				sendMessage(tags.getReceiverName(message), message);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
            clients.remove(this);
            this.interrupt();
//            try {
//				this.join();
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
        }
    }

    
    /*public void sendRequest(String msg) throws IOException
    {
		String name = msg.substring(msg.indexOf(tags.PLAY_REQ_OPEN_TAG) + tags.PLAY_REQ_OPEN_TAG.length(),
				msg.indexOf(tags.PLAY_REQ_CLOSE_TAG));
		for (ClientHandler c : clients) 
			if (c.getClientName().equals(name)) 
				c.send(tags.sendreq(this.getClientName()));
    }
    
    public void sendReponsive(String msg) 
    {
    	String name = msg.substring( 0, msg.indexOf(tags.REPONSIVE_REQ_TAG) );
    	if(msg.contains(tags.PLAY_ACCEPT_TAG))
    	{
    		sendMessage(name, this.getClientName() + tags.RECEIVE_REPON_TAG + tags.PLAY_ACCEPT_TAG);
    	}
    	else
    		sendMessage(name, this.getClientName() + tags.RECEIVE_REPON_TAG + tags.PLAY_DENY_TAG);
    }*/
	
	public void updatestatus (String message)
	{	
		if(this.clientName.equals(tags.getSenderName(message)))
			if (message.contains(tags.PLAYING_TAG))
			{
				System.out.println("Staus playin");
				this.status = tags.PLAYING;
			}
			else
				if(message.contains(tags.NEW_MATCH_TAG))
				{
					System.out.println("Staus mat");
					this.status = tags.MATCH;
				}
				else {
					System.out.println("Staus online");
					this.status = tags.ONLINE;
				}
		else
			for(ClientHandler c : clients)
				if(c.getClientName().equals(tags.getSenderName(message)))
					c.status = tags.ONLINE;
	}
    
	public void sendAllPlayerToNewPlayer()
	{
		for(ClientHandler c : clients)
			if(!c.getClientName().equals(this.clientName))
				if(c.getStatus().equals(tags.ONLINE))
				{
					send(tags.PlayerOnline(c.getClientName()));
					System.out.println("on");
				}
				else
					if(c.getStatus().equals(tags.PLAYING))
						send(tags.PlayerInMatch(c.getClientName()));		
	}
	
    public void sendPositionMarked(String msg)
    {	
    	msg = xml.parseXML(msg);
    	String name = msg.substring(0, msg.indexOf(tags.POSITION_MARKED_TAG));
    	System.out.println(name);
    	sendMessage(name, msg);
    }
    
    public void sendReplay(String msg)
    {
    	String name = msg.substring(0, msg.indexOf("<"));
    	sendMessage(name, msg);
    }
    
    public void send(String message)  {
        try {
			dos.writeUTF(message);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void sendMessage(String name, String message)
    {
    	for (ClientHandler c : clients) 
			if (c.getClientName().equals(name))
				c.send(message);
    }
    
    public void sendMessageToAll(String sender, String message)
    {
    	for (ClientHandler c : clients) 
			if (!c.getClientName().equals(sender))
				c.send(message);
    	
    }
    
}
