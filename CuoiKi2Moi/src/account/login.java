package account;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class login extends JFrame implements MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel palselect = new JPanel();
	JPanel pal = new JPanel();
	
	JLabel lblDangNhap = new JLabel("Đăng nhập");
	JLabel lblDangKi = new JLabel("Đăng kí");
	JLabel lblTaikhoan = new JLabel("Tài khoản");
	JLabel lblPass= new JLabel("Mật khẩu");
	JLabel lblDK = new JLabel();
	
	JTextField tfUsername = new JTextField(30);
	JPasswordField tfPass = new JPasswordField(30);
	
	JButton btnDn = new JButton("Đăng nhập");
	
	
	login(){
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(400,400);
		this.setLayout(new FlowLayout());
//		tfPass.setEchoChar((char)0);
		palselect.setLayout(new GridLayout(1,2));
		palselect.add(lblDangNhap);
		palselect.add(lblDangKi);
		lblDangKi.setForeground(Color.gray);
		lblDangKi.addMouseListener(this);
		pal.setLayout(new GridLayout(7,1));
		
		pal.add(palselect);
		pal.add(lblTaikhoan);
		pal.add(tfUsername);
		pal.add(lblPass);
		pal.add(tfPass);
		pal.add(lblDK);
		pal.add(btnDn);
		btnDn.setBackground(Color.red);
		btnDn.setForeground(Color.white);
		btnDn.setFont(new Font( null, ABORT, 18));
		btnDn.addActionListener(e->{
		  connect con = new connect();
		  con.login(tfUsername.getText(), tfPass.getPassword(), this);
		});
		this.add(pal);		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		new signup();
		this.dispose();
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	public static void main(String[] args) {
		new login();
	}

}
