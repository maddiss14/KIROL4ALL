package gui;

/**
 * @author Software Engineering teachers
 */

import javax.swing.*;

import domain.Socio;
import businessLogic.BLFacade;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EncargadoGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private static BLFacade appFacadeInterface;

	public static BLFacade getBusinessLogic() {
		return appFacadeInterface;
	}

	public static void setBussinessLogic(BLFacade afi) {
		appFacadeInterface = afi;
	}

	private JLabel jLabelIntroCred = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("EncargadoGUI.Credenciales"));
	private JPasswordField passwordField;
	private JButton btnEnterButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("EncargadoGUI.Enter")); 

	/**
	 * This is the default constructor
	 */
	private MainGUI mainGUI;

	public EncargadoGUI(MainGUI mainGUI) {
		super();
		this.mainGUI = mainGUI;
		getContentPane().setLayout(null);

		this.setSize(new Dimension(500, 300));

		jLabelIntroCred.setBounds(0, 1, 486, 65);
		jLabelIntroCred.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelIntroCred.setForeground(Color.BLACK);
		jLabelIntroCred.setHorizontalAlignment(SwingConstants.CENTER);


		jContentPane = new JPanel();
		jContentPane.setLayout(null);
		jContentPane.add(jLabelIntroCred);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(128, 76, 249, 19);
		jContentPane.add(passwordField);

		setContentPane(jContentPane);
		
		btnEnterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cont = passwordField.getText();
				autEncargado(cont);
			}
		});
		btnEnterButton.setBounds(134, 183, 224, 53);
		jContentPane.add(btnEnterButton);
	
		
		setTitle("KIROL4ALL");

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
	}
	
	public void autEncargado(String cont) {
		if(cont.equals("Encargado123")) {
			mainGUI.logEncargado();
			dispose();
		}
		
	}
}