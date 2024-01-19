package account;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import client.MainFrame;
import tags.PBKDF2Hasher;

public class connect {
	
	Connection con;
	Statement sttm;
	public connect() 
	{
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void connectsql() 
	{
		try {
			con = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-GVOLJTR\\SQLEXPRESS;user=sa;password=123456;databaseName=TicTacToe;encrypt=false;");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int execute(String s) {
		int record = 0;		
		try {
			connectsql();
			sttm = con.createStatement();
			record = sttm.executeUpdate(s);
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return record;		
	}
	public ResultSet executeQuery(String st) {		
		ResultSet rs=null;
		try {	
			connectsql();
			sttm = con.createStatement();
			rs = sttm.executeQuery(st);				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return rs;		
	}
	
	public void login (String username, char [] password, JFrame frame)
	{
		if(username.isBlank()||password.length==0)
			JOptionPane.showMessageDialog(null,"Vui lòng nhập đầy đủ nội dung","",JOptionPane.ERROR_MESSAGE);
		else
		{			
			ResultSet rs = executeQuery("select DisplayName, UserPassword, NumWin from Accounts "
					+ "where UserName='"+ username +"' ;");
			try {
				if(!rs.next())
					JOptionPane.showMessageDialog(null, "Tên tài khoản không đúng", "", JOptionPane.ERROR_MESSAGE);
				else 
				{	
					String pass = rs.getString(2);
					PBKDF2Hasher encrypt = new PBKDF2Hasher(16);
					if(encrypt.checkPassword(password, pass))
					{
						new MainFrame(rs.getString(1).trim(), rs.getInt(3));
						frame.dispose();
					}
					else
						JOptionPane.showMessageDialog(null, "Mật khẩu không đúng", "", JOptionPane.ERROR_MESSAGE);												
				}
			} catch (HeadlessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
			
	}
	
	public void signup (String username, String displayName, char[] password, char[] rePassword, JFrame frame)
	{
		if(password.length==0||rePassword.length==0||displayName.isBlank()||username.isBlank())
			JOptionPane.showMessageDialog(null,"Vui lòng nhập đầy đủ nội dung","",JOptionPane.ERROR_MESSAGE);
		else {
			if(!Arrays.equals(password, rePassword))
				JOptionPane.showMessageDialog(null,"Mật khẩu đã nhập khác nhau","",JOptionPane.ERROR_MESSAGE);
			else {
				ResultSet rs = executeQuery("Select * from Accounts where UserName='"+username+"'");
				try 
				{	if(rs.next())
						JOptionPane.showMessageDialog(null,"Tài khoản đã tồn tại","",JOptionPane.ERROR_MESSAGE);
					else 
						{	rs = executeQuery("Select * from Accounts where DisplayName='"+displayName+"'");
							if(rs.next())
								JOptionPane.showMessageDialog(null,"Tên hiển thị đã tồn tại","",JOptionPane.ERROR_MESSAGE);
							else 
								{										
									PBKDF2Hasher encrypt = new PBKDF2Hasher(16);
									String enPassword = encrypt.hash(password);
									
									execute("Insert into Accounts(UserName,DisplayName,UserPassword) values\r\n"
											+ "('"+username+"'"+",'"+displayName+"','"+enPassword+"')");
									JOptionPane.showMessageDialog(null,"Đăng kí tài khoản thành công","",JOptionPane.INFORMATION_MESSAGE);
									new MainFrame(displayName, 0);
									frame.dispose();
								}
						}
					
				} catch (HeadlessException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}		
	}
	
	public void updateNumWin (String displayName)
	{
		try {
			connectsql();
			execute("update Accounts set NumWin = NumWin + 1 where  DisplayName='" + displayName + "';");
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(1);
	}

}
