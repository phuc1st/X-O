package account;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class signup implements MouseListener, ActionListener{
	
	JFrame f = new JFrame();
	JPanel palselect = new JPanel();
	JPanel pal = new JPanel();
	
	JLabel lblDangNhap = new JLabel("Đăng nhập");
	JLabel lblDangKi = new JLabel("Đăng kí");
	JLabel lblTaikhoan = new JLabel("Tài khoản");
	JLabel lblEmail = new JLabel("Tên hiển thị");
	JLabel lblPass= new JLabel("Mật khẩu");
	JLabel lblXnPass = new JLabel("Xác nhận mật khẩu");
	JLabel lblDK = new JLabel();
	
	JTextField tfUsername = new JTextField(30);
	JTextField tfdisplayName = new JTextField(30);
	JPasswordField tfPass = new JPasswordField(30);
	JPasswordField tfRepass = new JPasswordField(30);
	
	JButton btnDk = new JButton("Đăng kí");
	signup(){		
		f.setSize(400,400);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(new FlowLayout());
		
		palselect.setLayout(new GridLayout(1,2));
		palselect.add(lblDangNhap);
		palselect.add(lblDangKi);
		lblDangNhap.setForeground(Color.gray);
		lblDangNhap.addMouseListener(this);
		pal.setLayout(new GridLayout(11,1));
		
		pal.add(palselect);
		pal.add(lblTaikhoan);
		pal.add(tfUsername);
		pal.add(lblEmail);
		pal.add(tfdisplayName);
		pal.add(lblPass);
		pal.add(tfPass);
		pal.add(lblXnPass);
		pal.add(tfRepass);
		pal.add(lblDK);
		pal.add(btnDk);
		btnDk.setBackground(Color.red);
		btnDk.setForeground(Color.white);
		btnDk.setFont(new Font( null, Font.PLAIN, 18));
		btnDk.addActionListener(this);
		f.add(pal);		
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);		
	}
	
	public static void main(String[] args) {

		new signup();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//this.anGiaodien(f);
		new login();
		f.dispose();
	}
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void actionPerformed(ActionEvent e) {
		  connect con = new connect();
		  con.signup(tfUsername.getText(), tfdisplayName.getText(), tfPass.getPassword(), tfRepass.getPassword(), f);
	}
}
