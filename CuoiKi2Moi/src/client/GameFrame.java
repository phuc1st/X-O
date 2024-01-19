package client;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

import tags.*;
public class GameFrame extends Thread implements ActionListener{

	private JFrame frame = new JFrame();
	private JPanel title_panel = new JPanel();
	private JPanel button_panel = new JPanel();
	private JLabel textfield = new JLabel();
	private JLabel battle = new JLabel();
	private JButton[] buttons = new JButton[9];	
	private JLabel lblEmo = new JLabel();
	private JLabel lblRivalEmo = new JLabel();
	private ImageIcon[] icon = new ImageIcon[6];
	private JPopupMenu popupMenu;
	private Boolean Turn;
	private Boolean TurnNextRound;
	private Socket socket;
	private DataOutputStream dos;
	private DataInputStream din;
	private String MyName;
	private String YourName;
	private Boolean open = true;
	private Boolean isReplay = true;
	private JFrame mainframe;
	
	// 0-8 represents 9 boxes .
	private int[][] winCombArray = {{0,1,2} , {3,4,5} , {6,7,8} , {0,3,6} , {1,4,7} , {2,5,8} , {0,4,8} , {2,4,6}} ;
	// ArrayList has useful tools(contains() and clear()) compared to array .
	private ArrayList<Integer> MyArrayList = new ArrayList<>() ;
	private ArrayList<Integer> YourArrayList = new ArrayList<>() ;
	// store the winning combination array (3 num) for change the color of that 3 boxes .
	private int winComb ;

	// check whether the winning condition is met .
public boolean check(ArrayList<Integer> arrayList) {
		
		boolean win = false ;
		
		// iterate through all nested arrays .
		for (int x=0 ; x<8 ; x++) 
		{
			for (int y=0 ; y<3 ; y++) 
			{
				if (arrayList.contains(winCombArray[x][y])) 
				{
					if (y == 2) 
					{
						win = true ;
						winComb = x ;
						x = 7 ;
					}
				}
				else 
				{
					win = false ;
					break ;
				}
			}
		}
		if (win == true)
			Turn = false;
		return win ;
	}


	GameFrame(String MyName, String YourName, Boolean Turn, JFrame mainframe) 
	{			
		this.mainframe = mainframe;
		try {
			this.socket = new Socket("localhost", 8000);
			this.din = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.Turn = Turn;	
		sendMessage(tags.newMatch(MyName+YourName));
		
		this.MyName = MyName;
		this.YourName = YourName;
		if(Turn)
		{
			this.TurnNextRound = false;
			textfield.setText("Lượt của bạn");
		}
		else 
		{
			this.TurnNextRound = true;
			textfield.setText("Lượt của " + YourName);
		}
		
		icon[0] = new ImageIcon(GameFrame.class.getResource("/img/lol.png"));
		icon[1] = new ImageIcon(GameFrame.class.getResource("/img/angry.png"));
		icon[2] = new ImageIcon(GameFrame.class.getResource("/img/chicken.png"));
		icon[3] = new ImageIcon(GameFrame.class.getResource("/img/conju.png"));
		icon[4] = new ImageIcon(GameFrame.class.getResource("/img/cry.png"));
		icon[5] = new ImageIcon(GameFrame.class.getResource("/img/wow.png"));

		frame.setSize(600,600);
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);
		frame.setResizable(false);
		
//		textfield.setBackground(new Color(25,25,25));
		textfield.setForeground(new Color(25,255,0));
		textfield.setFont(new Font("Tahoma",Font.BOLD,25));
		textfield.setHorizontalAlignment(JLabel.CENTER);
		textfield.setOpaque(false);
		
//		battle.setBackground(new Color(25,25,25));
		battle.setForeground(new Color(25,255,0));
		battle.setFont(new Font("Tahoma",Font.BOLD,40));
		battle.setHorizontalAlignment(JLabel.CENTER);
		battle.setText("Bạn đấu với " + YourName);
		battle.setOpaque(false);
		
		lblEmo.setIcon(icon[0]);
		lblEmo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(lblEmo, 0, 50);
            }
        });
		
		popupMenu = new JPopupMenu();
        popupMenu.setLayout(new GridLayout(2, 3));
        for (int i = 0; i < 6; i++) {
            JLabel label = new JLabel(icon[i]);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	  JLabel selectedLabel = (JLabel) e.getSource();
                      int index = popupMenu.getComponentZOrder(selectedLabel);
                      sendMessage(tags.sendEmoji(MyName + YourName, YourName + MyName, index));                      
                    // Ẩn menu popup
                    popupMenu.setVisible(false);
                }
            });
            popupMenu.add(label);
        }
        popupMenu.setBackground(new Color(25,25,25));
        
        title_panel.setLayout(new BorderLayout());
		title_panel.setPreferredSize( new Dimension(600,100));
		title_panel.setBackground(new Color(25,25,25));
        JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(600, 100));
		layeredPane.setOpaque(false);
		title_panel.add(layeredPane, BorderLayout.CENTER);

		// Thêm các thành phần vào layeredPane
		layeredPane.add(battle, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(textfield, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(lblEmo, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(lblRivalEmo, JLayeredPane.DEFAULT_LAYER);

		// Đặt vị trí của các thành phần
		battle.setBounds(0, 0, 600, 50);
		textfield.setBounds(0, 50, 600, 50);
		lblEmo.setBounds(0, 50, 50, 50);
		lblRivalEmo.setBounds(450, 50, 50, 50);

		// Đưa lblEmo và lblRivalEmo lên trên cùng
		layeredPane.setComponentZOrder(lblEmo, 0);
		layeredPane.setComponentZOrder(lblRivalEmo, 0);		
		
		button_panel.setLayout(new GridLayout(3,3));
		button_panel.setBackground(new Color(150,150,150));
		
		for(int i=0;i<9;i++) 
		{
			buttons[i] = new JButton();
			button_panel.add(buttons[i]);
			buttons[i].setBackground(Color.white);
			buttons[i].setFont(new Font("MV Boli",Font.BOLD,120));
			buttons[i].setFocusable(false);
			buttons[i].addActionListener(this);
		}
		
		frame.add(title_panel,BorderLayout.NORTH);
		frame.add(button_panel);	
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				sendMessage( tags.PlayerOnline(MyName) );
		        closeWindow();
		      }		
		});
		this.start();
	}

	public void run()
	{	
		try {
			sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Thread stopped");
		}
		String msg;
		while(this.open)
		{
			try {
				if (socket.getInputStream().available() > 0)
				{					
					msg = din.readUTF();
					if(msg.contains(tags.POSITION_MARKED_TAG))
						updateYourTurn(msg);
					else
					{
						if(msg.contains(tags.REPLAY_TAG))
							receiveReplay(msg);
						else
							if(msg.contains(tags.NOT_REPLAY_TAG))
							{
								receiveReplay(msg);
								break;
							}
						if(msg.contains(tags.EMOJI_TAG))
							receiveEmoji(msg);
					}
						
				}
			} catch (IOException e) {
				System.out.println("im fsll");
				e.printStackTrace();
				System.out.println("socket đã đóng");
				break;
			}
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Thread stop 100");
				break;
			}
		}
	}
	
	public void updateYourTurn(String msg)
	{
		int i = Integer.parseInt(msg.substring(msg.length()-1));
		buttons[i].setForeground(new Color(0,0,255));
		buttons[i].setText("O");
		YourArrayList.add(i);
		Turn = true;
		textfield.setText("Lượt của bạn");
		if(check(YourArrayList))
		{				
			textfield.setText(this.YourName + " Thắng");
			for (int x : winCombArray[winComb]) 
			{
				buttons[x].setBackground(Color.pink);
			}
			
			sendReplay();
		}
		else
			gamedraw();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		for(int i=0;i<9;i++) {
			if(e.getSource()==buttons[i]) 
			{
				if(Turn) 
				
					if(buttons[i].getText()=="") 
					{
						buttons[i].setForeground(new Color(255,0,0));
						buttons[i].setText("X");
						Turn=false;
						textfield.setText("Lượt của " + this.YourName);					
						MyArrayList.add(i) ;					
						sendMessage(xml.createXMLMessage(YourName, MyName, i));						
						if (check(MyArrayList)) 
						{	
							sendMessage(tags.sendWin(MyName));
							System.out.println("Bạn thắng");
							for (int id : MyArrayList)
								System.out.print(" "+id);
							textfield.setText("Bạn thắng !!!") ;							
							for (int x : winCombArray[winComb]) 
								buttons[x].setBackground(Color.green);							
							sendReplay();
						}
						else gamedraw();
					}											
			}			
		}
	}
	
	public void gamedraw() {
		for (int x=0 ; x<9 ; x++) {
			if (buttons[x].getText().isBlank()) {
				break ;
			}
			if (x == 8) 
			{
				textfield.setText("Hòa") ;
				Turn = false;
				sendReplay();
			}
		}
	}
	public void sendReplay()
	{
		int n =JOptionPane.showConfirmDialog(frame, "Bạn có muốn chơi lại không ?", "Chơi lại.", JOptionPane.YES_NO_OPTION);
		if(n==0)		
		{
			if(isReplay)
			{
				sendMessage(tags.replay(MyName + YourName, YourName + MyName, tags.REPLAY_TAG));						
				replay();
			}
		}
		else
		{								
			System.out.println(this.MyName + "đóng");
			if(isReplay)
			{				
				sendMessage(tags.replay(MyName + YourName, YourName + MyName, tags.NOT_REPLAY_TAG));			
				sendMessage( tags.PlayerOnline(this.MyName) );
				closeWindow();
			}
		}
	}
	
	public void receiveReplay(String msg) throws IOException
	{
		if(msg.contains(tags.REPLAY_TAG))
			replay();
		else
		{
			this.isReplay = false;
			JOptionPane.showMessageDialog(this.frame, "Đối thủ của bạn không muốn chơi lại :(", "Thông báo", JOptionPane.OK_OPTION);
			sendMessage( tags.PlayerOnline(this.MyName) );
			closeWindow();
		}
	}
	
	public void replay()
	{
		this.isReplay = true;
		this.MyArrayList.clear();
		this.YourArrayList.clear();
		for(int i=0; i<9; i++)
		{
			buttons[i].setBackground(Color.white);
			buttons[i].setText("");
		}
		if(this.TurnNextRound)
			this.Turn = true;
		else
			this.Turn = false;
		this.TurnNextRound = !this.TurnNextRound;
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
	
	public void receiveEmoji(String msg)
	{
		int emoji = Integer.parseInt(msg.substring(msg.length()-1 ) );
		lblRivalEmo.setVisible(true);
        lblRivalEmo.setIcon(icon[emoji]);
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblRivalEmo.setVisible(false);
            }
        });
        timer.setRepeats(false);
        timer.start();
	}
	
	public void closeWindow()
	{	
		mainframe.setVisible(true);
        SwingUtilities.invokeLater(() -> {
        	open = false;
        	interrupt();
        	try {
        		socket.close();
        	} catch (IOException e2) {
        		e2.printStackTrace();
        	}
        	frame.dispose(); 
        });	
	}
//	public static void main(String[] args) {
//		try {
//			new GameFrame("P", "Quocggggg", false, new JFrame());
//		} catch (HeadlessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}