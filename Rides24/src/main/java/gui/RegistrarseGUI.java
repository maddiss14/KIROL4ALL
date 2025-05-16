package gui;

import businessLogic.BLFacade;

import configuration.UtilDate;

import com.toedter.calendar.JCalendar;

import exceptions.MetodoDePagoNoValidoException;
import exceptions.SocioAlreadyExistsException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.DateFormat;
import java.util.*;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class RegistrarseGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JButton btnRegistrar = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Registrarse"));

	private JTextField name;
	private JTextField mail;
	private JPasswordField password;
	private JTextField idPago;

	private JLabel lblNameLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegistrarseGUI.Name"));
	private JLabel lblMailLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Mail"));
	private JLabel lblPasswordLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Password"));
	private JLabel lblMPagoLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegistrarseGUI.MPago"));
	private JLabel lblIdPago = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegistrarseGUI.IdPago"));;

	private JRadioButton rdbtnPaypalRadioButton = new JRadioButton("Paypal");
	private JRadioButton rdbtnTarjetaRadioButton = new JRadioButton(
			ResourceBundle.getBundle("Etiquetas").getString("RegistrarseGUI.mPagoTarjeta"));

	private JTextArea textArea;
	private final ButtonGroup tipoMPago = new ButtonGroup();

	public RegistrarseGUI() {

		textArea = new JTextArea();
		textArea.setBounds(22, 10, 437, 22);
		getContentPane().add(textArea);
		textArea.setVisible(false);

		getContentPane().setLayout(null);
		this.setSize(new Dimension(529, 342));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Registrarse"));

		lblNameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNameLabel.setBounds(10, 38, 132, 26);
		getContentPane().add(lblNameLabel);

		lblMailLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblMailLabel.setBounds(10, 74, 132, 26);
		getContentPane().add(lblMailLabel);

		lblPasswordLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPasswordLabel.setBounds(10, 118, 132, 21);
		getContentPane().add(lblPasswordLabel);
		lblMPagoLabel.setHorizontalAlignment(SwingConstants.CENTER);

		lblMPagoLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblMPagoLabel.setBounds(98, 149, 378, 26);
		getContentPane().add(lblMPagoLabel);

		mail = new JTextField();
		mail.setBounds(152, 77, 277, 26);
		getContentPane().add(mail);
		mail.setColumns(10);

		password = new JPasswordField();
		password.setColumns(10);
		password.setBounds(152, 113, 277, 26);
		getContentPane().add(password);

		name = new JTextField();
		name.setColumns(10);
		name.setBounds(152, 41, 277, 26);
		getContentPane().add(name);

		tipoMPago.add(rdbtnPaypalRadioButton);
		rdbtnPaypalRadioButton.setBounds(183, 181, 103, 21);
		getContentPane().add(rdbtnPaypalRadioButton);

		tipoMPago.add(rdbtnTarjetaRadioButton);
		rdbtnTarjetaRadioButton.setBounds(326, 181, 103, 21);
		getContentPane().add(rdbtnTarjetaRadioButton);

		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					registrarUser();
				} catch (SocioAlreadyExistsException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		btnRegistrar.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnRegistrar.setBounds(191, 250, 187, 45);
		getContentPane().add(btnRegistrar);
		getContentPane().add(textArea);

		lblIdPago.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblIdPago.setBounds(10, 215, 132, 26);
		getContentPane().add(lblIdPago);

		idPago = new JTextField();
		idPago.setBounds(152, 215, 277, 26);
		getContentPane().add(idPago);

	}

	public void registrarUser() throws SocioAlreadyExistsException {
		String email = mail.getText();
		String contra = password.getText();
		String nombre = name.getText();
		String datosPago = idPago.getText();
		int paypalTarjeta;

		boolean paypal = rdbtnPaypalRadioButton.isSelected();
		boolean tarjeta = rdbtnTarjetaRadioButton.isSelected();

		if (email.isEmpty() || contra.isEmpty() || nombre.isEmpty() || (!paypal && !tarjeta) || datosPago.isEmpty()) {
			textArea.setText("Todos los campos son obligatorios.");
			textArea.setVisible(true);
			return;
		}
		if(paypal) paypalTarjeta = 0;
		else paypalTarjeta = 1;
		
		BLFacade facade = MainGUI.getBusinessLogic();
		try {
			facade.doesSocioExist(email);
			try {
				facade.registrarUser(nombre, email, contra, datosPago, paypalTarjeta);
				textArea.setText(ResourceBundle.getBundle("Etiquetas").getString("RegistrarseGUI.RegistroOK"));
				textArea.setVisible(true);
			}catch (MetodoDePagoNoValidoException i) {
				textArea.setText(ResourceBundle.getBundle("Etiquetas").getString("Banco.MetodoPagoNoValido"));
				textArea.setVisible(true);
			}
			
		} catch (SocioAlreadyExistsException i) {
			textArea.setText(ResourceBundle.getBundle("Etiquetas").getString("RegistrarseGUI.UserYaExiste"));
			textArea.setVisible(true);

		}
	}

	public void addMPago() {
		if (rdbtnPaypalRadioButton.isSelected()) {
			getContentPane().removeAll();
		} else {

		}
	}
}
