package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import tags.tags;

public class MainFrame extends Thread {
	private JFrame fr = new JFrame("Home");
	private JLabel lblNameUser = new JLabel();
	private JLabel avatar = new JLabel();
	private JTabbedPane tabPane = new JTabbedPane();
	private JPanel contentPane = new JPanel();
	private JPanel rankPane = new JPanel();
	private JLabel  lblRank = new JLabel();
	private ImageIcon rankicon;
	private JLabel lblStar = new JLabel();
	private ImageIcon starIcon;
	private String name;
	private String username;
	private int NumWin;
	private JList<String> listOnline;
	private DefaultListModel<String> modelOnline = new DefaultListModel<>();
	private JList<String> listPlaying;
	private DefaultListModel<String> modelPlaying = new DefaultListModel<>();
	private Socket client;
	private DataOutputStream dos;
	private DataInputStream din;
	
	public MainFrame(String username, int numWin)
	{
		this.username = username;
		this.NumWin = numWin;
		contentPane.setLayout(null);
		
		avatar.setIcon(new ImageIcon(MainFrame.class.getResource("/img/avt.jpg")));
		avatar.setBounds(10,10,50,50);
		contentPane.add(avatar);
		
		this.lblNameUser.setText(username);
		lblNameUser.setBounds(70,10,200,40);
		lblNameUser.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		contentPane.add(lblNameUser);
		
		lblStar.setOpaque(false);
		rankPane.setLayout(null);
		rankPane.add(lblRank);
		rankPane.add(lblStar);
		lblRank.setBounds(150, 10, 240, 300 );
		lblStar.setBounds(160, 320, 200, 50);			
		setRank(numWin);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,1));
		listOnline = new JList<>(modelOnline);
		listOnline.setBackground(Color.WHITE);
		listOnline.setForeground(Color.GREEN);
		listOnline.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		JScrollPane listPane = new JScrollPane(listOnline);
		
		listPlaying = new JList<>(modelPlaying);
		listPlaying.setBackground(Color.WHITE);
		listPlaying.setForeground(Color.RED);
		listPlaying.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		JScrollPane listPlayingPane = new JScrollPane(listPlaying);
		
		tabPane.add("Xếp hạng", rankPane);
		tabPane.add("Người Chơi online", listPane);
		tabPane.add("Người chơi trong trận", listPlayingPane);
		tabPane.setBounds(10,70,570,470);
		contentPane.add(tabPane);		
		
		fr.setContentPane(contentPane);
		fr.setBounds(400, 100, 600, 600);
		fr.setResizable(false);
		fr.setVisible(true);
		fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//Sự kiện listOnline
		listOnline.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int index = listOnline.locationToIndex(arg0.getPoint());
			    if (index >= 0 && index < modelOnline.getSize()) 
			    {
			    	name = modelOnline.getElementAt(index);			   
			        connectPlayer();			            
			    }				
			}
		});
	
		fr.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {		
				closeWindow();
		      }		
		});	
		
		//Xử lí socket
		try {
			this.client = new Socket("localhost", 8000);
			dos = new DataOutputStream(client.getOutputStream());
		  	din = new DataInputStream(client.getInputStream());
			sendMessage(tags.newPlayer(this.username));			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.start();
	}
	
	public void updateListPlayer(String message)
	{
		String name = message.substring( 
						message.indexOf(tags.SENDER_OPEN_TAG) + tags.SENDER_OPEN_TAG.length(), 
						message.indexOf(tags.SENDER_CLOSE_TAG));
		if (message.contains(tags.NEW_PLAYER_CONNECT_TAG))
			addPlayerToModel(name, modelOnline);
		else
			if (message.contains(tags.ONLINE_TAG))
			{
				removePlayerInModel(name, modelPlaying);
				addPlayerToModel(name, modelOnline);
			}
			else
				if (message.contains(tags.PLAYING_TAG))
				{
					removePlayerInModel(name, modelOnline);
					addPlayerToModel(name, modelPlaying);
				}
				else 
					if (message.contains(tags.EXIT_TAG))
					{
						removePlayerInModel(name, modelOnline);
						removePlayerInModel(name, modelPlaying);
					}					
	}
	
	public void setRank(int numWin)
	{
		if(numWin >=0 && numWin <=4)
			rankicon = new ImageIcon(MainFrame.class.getResource("/img/Fe.jpg"));
		else
			if(numWin >=5 && numWin <=8)
				rankicon = new ImageIcon(MainFrame.class.getResource("/img/AL.jpg"));
			else
				if(numWin >=9 && numWin <=12)
					rankicon = new ImageIcon(MainFrame.class.getResource("/img/Cu.jpg"));
				else
					if(numWin >=13 && numWin <=16)
						rankicon = new ImageIcon(MainFrame.class.getResource("/img/AU.jpg"));
		int star = numWin % 4;
		if (numWin ==0)
			starIcon = new ImageIcon();
		else
			switch(star)
			{
				case 1:
					starIcon = new ImageIcon(MainFrame.class.getResource("/img/star1.png"));
					break;
				case 2:
					starIcon = new ImageIcon(MainFrame.class.getResource("/img/star2.png"));
					break;
				case 3:
					starIcon = new ImageIcon(MainFrame.class.getResource("/img/star3.png"));
					break;
				case 0:
					starIcon = new ImageIcon(MainFrame.class.getResource("/img/star4.png"));
					break;
			}
		lblRank.setIcon(rankicon);
		lblStar.setIcon(starIcon);
	}
	
	public void removePlayerInModel (String PlayerName, DefaultListModel<String> model)
	{
		for(int i=0; i < model.getSize(); i++ )
		{
			  if (model.getElementAt(i).equals(PlayerName)) {
				  model.removeElementAt(i);
			        break;
			    }
		}
	}
	
	public void addPlayerToModel (String PlayerName,  DefaultListModel<String> model)
	{
		model.addElement(PlayerName);
	}
	
	public void connectPlayer()
	{
		int n = JOptionPane.showConfirmDialog(fr, "Bạn có muốn kết nối với người chơi này không?", "Kết nối"
												, JOptionPane.YES_NO_OPTION);
		if(n==0)
		{			
			String msg = tags.RequestPlay(username, name);
			sendMessage(msg);
			System.out.println(msg);
		}
		
	}
	
	public void requestProcess(String msg) throws IOException
	{
		String name = tags.getSenderName(msg);
		int n = JOptionPane.showConfirmDialog(fr,
				"Người chơi " + name + " muốn chơi với bạn. Bạn có muốn chơi cùng họ không?", "Yêu cầu chơi",
				JOptionPane.YES_NO_OPTION);
		if(n==0)		
		{
			sendMessage(tags.ReponRequest(username, name, tags.ACCEPT_REQ_PLAY_TAG));
			System.out.println(tags.ReponRequest(username, name, tags.ACCEPT_REQ_PLAY_TAG));
			dos.writeUTF(tags.PlayerInMatch(this.username));
			this.fr.setVisible(false);
			new GameFrame(this.username, name, false, this.fr);
		}
		else
			dos.writeUTF(tags.ReponRequest(username, name, tags.DENY_REQ_PLAY_TAG));		
		dos.flush();
	}
	
	public void reponProcess(String msg)
	{
		if(msg.contains(tags.ACCEPT_REQ_PLAY_TAG))
			try {
				dos.writeUTF(tags.PlayerInMatch(this.username));
				dos.flush();
				this.fr.setVisible(false);
				new GameFrame(this.username, tags.getSenderName(msg), true, this.fr);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		else
			JOptionPane.showMessageDialog(this.fr, "Người chơi từ chối yêu cầu!", "Thông báo", JOptionPane.OK_OPTION);
	}
	
	public void closeWindow() {
		sendMessage(tags.PlayerExit(username));
	    SwingUtilities.invokeLater(() -> {
	      try {
	        this.client.close();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	      this.interrupt();
	      fr.dispose(); 
	    });
	  }
	
	public void run() {
	    while (true) {
	        try {
//	            if (client.getInputStream().available() > 0) {
	          
	                String msg = din.readUTF();	                
	                if(msg.contains(tags.REQUEST_PLAY_TAG))
	                	requestProcess(msg);	   
	                if(msg.contains(tags.REPON_REQUEST_TAG))
	                		reponProcess(msg);
	                if(msg.contains(tags.SEND_ALL_TAG))
	                	updateListPlayer(msg);
	                else
	                	if(msg.contains(tags.WIN_TAG))
	                	{
	                		this.NumWin += 1;
	                		setRank(this.NumWin);
	                	}
//	            }
	            sleep(1000);
	        } catch (Exception e) {
	        	  e.printStackTrace();
	        	  System.err.println("Socket closed, Thread Stopped");
	            break;
	        }
	    }
	}
	
	public void sendMessage(String msg)
	{
		try {
			this.dos.writeUTF(msg);
			this.dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args)
	{
		new MainFrame("khi ho", 0);
	}
}

