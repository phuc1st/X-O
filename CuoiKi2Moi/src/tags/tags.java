package tags;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class tags {
	public static int IN_VALID = -1;
	public static final String ONLINE = "<ONLINE>";
	public static final String PLAYING = "<PLAYING>";
	public static final String MATCH = "<MATCH>";
	public static final String SEND_ALL_TAG		 = "<SEND_ALL>";
	public static final String SENDER_OPEN_TAG 	 = "<SENDER_OPEN>";
	public static final String SENDER_CLOSE_TAG 	 = "<SENDER_CLOSE />";
	public static final String RECEIVER_OPEN_TAG 	 = "<RECEIVER_OPEN>";
	public static final String RECEIVER_CLOSE_TAG  = "<RECEIVER_CLOSE />";
	public static final String REQUEST_PLAY_TAG 	 = "<REQUEST_PLAY>";
	public static final String DENY_REQ_PLAY_TAG   = "<DENY_PLAY />";
	public static final String ACCEPT_REQ_PLAY_TAG = "<ACCECPT_PLAY />";
	public static final String REPON_REQUEST_TAG	 = "<REPONSE>";
	public static final String NEW_PLAYER_CONNECT_TAG = "<NEW_PLAYER_CONNECT>";
	public static final String POSITION_MARKED_TAG 	= "<POSITION>";
	public static final String REPLAY_TAG 			= "<REPLAY>";
	public static final String NOT_REPLAY_TAG 		= "<NOT_REPLAY>";
	public static final String NEW_MATCH_TAG 		= "<NEW_MATCH>";
	public static final String PLAYING_TAG 			= "<PLAYING>";
	public static final String ONLINE_TAG 			= "<ONLINE>";
	public static final String EXIT_TAG 			= "<EXIT>";
	public static final String WIN_TAG 				= "<WIN>";
	public static final String EMOJI_TAG 			= "<EMOJI>";
	public static final String INTO_GAME_TAG 		= "<INTO_GAME>";
	public static final String LOGIN_TAG			= "<LOGIN_TAG>";
	public static final String SIGNUP_TAG			= "<SIGNUP_TAG>";
	public static final String USERNAME_OPEN_TAG 	= "<USERNAME_OPEN_TAG>";
	public static final String USERNAME_CLOSE_TAG 	= "<USERNAME_CLOSE_TAG />";
	public static final String DISPLAYNAME_OPEN_TAG 	= "<DISPLAYNAME_OPEN_TAG>";
	public static final String DISPLAYNAME_CLOSE_TAG 	= "<DISPLAYNAME_CLOSE_TAG />";
	public static final String PASSWORD_OPEN_TAG 	= "<PASSWORD_OPEN_TAG>";
	public static final String PASSWORD_CLOSE_TAG 	= "<PASSWORD_CLOSE_TAG />";
	
	
	public static int show(JFrame frame, String msg, boolean type) {
		if (type)
			return JOptionPane.showConfirmDialog(frame, msg, null, JOptionPane.YES_NO_OPTION);
		JOptionPane.showMessageDialog(frame, msg);
		return IN_VALID;
	}
	
	public static String RequestPlay(String senderName, String receiverName)
	{
		return tags.SENDER_OPEN_TAG + senderName + tags.SENDER_CLOSE_TAG 
			 + tags.RECEIVER_OPEN_TAG + receiverName + tags.RECEIVER_CLOSE_TAG + tags.REQUEST_PLAY_TAG;
	}
	
	public static String ReponRequest (String senderName, String receiverName, String choose)
	{
		return tags.SENDER_OPEN_TAG + senderName + tags.SENDER_CLOSE_TAG 
				 + tags.RECEIVER_OPEN_TAG + receiverName + tags.RECEIVER_CLOSE_TAG 
				 + tags.REPON_REQUEST_TAG + choose;
	}
	
	public static String replay (String senderName, String receiverName, String choose)
	{
		return tags.SENDER_OPEN_TAG + senderName + tags.SENDER_CLOSE_TAG 
				 + tags.RECEIVER_OPEN_TAG + receiverName + tags.RECEIVER_CLOSE_TAG 
				 + choose;
	}
	
	public static String PlayerInMatch (String name)
	{
		return tags.SEND_ALL_TAG + tags.SENDER_OPEN_TAG + name + tags.SENDER_CLOSE_TAG + tags.PLAYING_TAG ;
	}
	
	public static String PlayerOnline (String name)
	{
		return tags.SEND_ALL_TAG + tags.SENDER_OPEN_TAG + name + tags.SENDER_CLOSE_TAG + tags.ONLINE_TAG ;
	}
	public static String PlayerExit (String name)
	{
		return tags.SEND_ALL_TAG + tags.SENDER_OPEN_TAG + name + tags.SENDER_CLOSE_TAG + tags.EXIT_TAG ;
	}
	
	public static String newPlayer (String name)
	{
		return tags.SEND_ALL_TAG + tags.SENDER_OPEN_TAG + name + tags.SENDER_CLOSE_TAG + tags.NEW_PLAYER_CONNECT_TAG ;
	}
	
	public static String newMatch (String name)
	{
		return tags.SENDER_OPEN_TAG + name + tags.SENDER_CLOSE_TAG + tags.NEW_MATCH_TAG;
	}
	
	public static String sendEmoji (String senderName, String receiverName, int emoji)
	{
		return tags.SENDER_OPEN_TAG + senderName + tags.SENDER_CLOSE_TAG 
				 + tags.RECEIVER_OPEN_TAG + receiverName + tags.RECEIVER_CLOSE_TAG 
				 + tags.EMOJI_TAG + emoji;
	}
	
	public static String sendWin (String senderName)
	{
		return tags.RECEIVER_OPEN_TAG + senderName + tags.RECEIVER_CLOSE_TAG + tags.WIN_TAG;
	}
	
//	public static String intoGame (String name)
//	{
//		return tags.SENDER_OPEN_TAG + senderName + tags.SENDER_CLOSE_TAG 
//	}
	
	public static String login (String username, char[] password)
	{
		String passwordString = new String(password); 
		return tags.LOGIN_TAG + tags.USERNAME_OPEN_TAG + username + tags.USERNAME_CLOSE_TAG
				+ tags.PASSWORD_OPEN_TAG + passwordString + tags.PASSWORD_CLOSE_TAG;
	}
	
	public static String signup (String username, String displayname, char [] password)
	{
		String passwordString = new String(password); 
		return tags.SIGNUP_TAG + tags.USERNAME_OPEN_TAG + username + tags.USERNAME_CLOSE_TAG
				+tags.DISPLAYNAME_OPEN_TAG + displayname + tags.DISPLAYNAME_CLOSE_TAG
				+ tags.PASSWORD_OPEN_TAG + passwordString + tags.PASSWORD_CLOSE_TAG;
	}
	
	public static int getNumWin (String message)
	{
		return Integer.parseInt(message.substring( message.indexOf(tags.WIN_TAG) + tags.WIN_TAG.length() ));
	}
	
	public static String getSenderName (String message)
	{
		return message.substring( 
				message.indexOf(tags.SENDER_OPEN_TAG) + tags.SENDER_OPEN_TAG.length(), 
				message.indexOf(tags.SENDER_CLOSE_TAG));
	}
	
	public static String getReceiverName (String message)
	{
		return message.substring( 
				message.indexOf(tags.RECEIVER_OPEN_TAG) + tags.RECEIVER_OPEN_TAG.length(), 
				message.indexOf(tags.RECEIVER_CLOSE_TAG));
	}	
	
	public static String getUserName (String message)
	{
		return message.substring( 
				message.indexOf(tags.USERNAME_OPEN_TAG) + tags.USERNAME_OPEN_TAG.length(), 
				message.indexOf(tags.USERNAME_CLOSE_TAG));
	}
	
	public static String getDisplayName (String message)
	{
		return message.substring( 
				message.indexOf(tags.DISPLAYNAME_OPEN_TAG) + tags.DISPLAYNAME_OPEN_TAG.length(), 
				message.indexOf(tags.DISPLAYNAME_CLOSE_TAG));
	}
	
	public static char[] getPassWord (String message)
	{
		String pass = message.substring( 
						message.indexOf(tags.PASSWORD_OPEN_TAG) + tags.PASSWORD_OPEN_TAG.length(), 
						message.indexOf(tags.PASSWORD_CLOSE_TAG));
		return pass.toCharArray();
	}
	public static void main(String[] args) {
		char [] c = {'1','0','p'};
		System.out.println(tags.login("ohuc", c));
	}
}
