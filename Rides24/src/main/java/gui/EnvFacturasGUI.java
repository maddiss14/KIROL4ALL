package gui;

import businessLogic.BLFacade;

import domain.Socio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class EnvFacturasGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
	private JButton btnNewButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("EnvFacturaGUI.EnviarFactura"));

	public EnvFacturasGUI() {
		this.setSize(new Dimension(500, 300));
		getContentPane().setLayout(null);

		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("EnvFacturaGUI.EnviarFactura"));
		getContentPane().setLayout(null);

		jButtonClose.setBounds(new Rectangle(148, 223, 130, 30));

		jButtonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton2_actionPerformed(e);
			}
		});

		this.getContentPane().add(jButtonClose, null);

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BLFacade facade = MainGUI.getBusinessLogic();
				facade.envFacturas();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton.setBounds(83, 55, 255, 110);
		btnNewButton.setEnabled(false);

		//Verifica si hoy es lunes
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		int dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);
		if (dayOfWeek == java.util.Calendar.MONDAY) {
			btnNewButton.setEnabled(true);
		}

		getContentPane().add(btnNewButton);

	}

	private void jButton2_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}
