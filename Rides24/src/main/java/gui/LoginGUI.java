package gui;

import businessLogic.BLFacade;
import configuration.UtilDate;

import com.toedter.calendar.JCalendar;

import domain.Reserva;
import domain.Socio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.DateFormat;
import java.util.*;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class LoginGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JButton btnLogin;
	private JTextField mail;
	private JPasswordField contra;
	private JLabel lblUserNameLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Mail"));
	private JLabel lblPasswordLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Password"));
	private MainGUI mainGUI;
	private JTextArea textArea = new JTextArea(
			ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.MailIncorrecto"));

	public LoginGUI(MainGUI mainGUI) {
		this.mainGUI = mainGUI;
		textArea.setEditable(false);

		textArea.setBounds(122, 29, 277, 22);
		getContentPane().add(textArea);
		textArea.setVisible(false);

		getContentPane().setLayout(null);
		this.setSize(new Dimension(500, 300));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Login"));

		mail = new JTextField();
		mail.setBounds(122, 61, 277, 36);
		getContentPane().add(mail);
		mail.setColumns(10);

		lblUserNameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblUserNameLabel.setBounds(10, 65, 102, 26);
		getContentPane().add(lblUserNameLabel);

		lblPasswordLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPasswordLabel.setBounds(10, 124, 84, 21);
		getContentPane().add(lblPasswordLabel);

		contra = new JPasswordField();
		contra.setColumns(10);
		contra.setBounds(122, 117, 277, 36);
		getContentPane().add(contra);

		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comprobarUsuario();
			}
		});

		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnLogin.setBounds(132, 190, 187, 45);
		getContentPane().add(btnLogin);

	}

	private void comprobarUsuario() {
		String correo = mail.getText();
		String contraseña = contra.getText();
		
		if (correo != null && contraseña != null) {
			BLFacade facade = MainGUI.getBusinessLogic();
			Socio socio = facade.getSocio(correo, contraseña);
			if (socio != null) {
				mainGUI.loginSocio(socio);
				dispose();
			} else {
				textArea.setVisible(true);
			}
		}
	}

}
