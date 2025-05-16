package gui;

/**
 * @author Software Engineering teachers
 */

import javax.swing.*;

import domain.Reserva;
import domain.Socio;
import businessLogic.BLFacade;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane;
	private JButton jButtonLogin;
	private JButton jButtonRegis;
	private JButton jButtonConsSes;
	private JButton btnEncargadoButton;

	private JButton jButtonCancelarRes;
	private JButton jButtonConsFact;
	private JButton jButtonPagFact;

	private static BLFacade appFacadeInterface;

	public static BLFacade getBusinessLogic() {
		return appFacadeInterface;
	}

	public static void setBussinessLogic(BLFacade afi) {
		appFacadeInterface = afi;
	}

	protected JLabel jLabelSelectOption;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private JRadioButton rdbtnNewRadioButton_2;
	private JPanel panel;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * This is the default constructor
	 */

	public MainGUI() {
		super();
		getContentPane().setLayout(null);

		this.setSize(new Dimension(500, 300));

		jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jLabelSelectOption.setBounds(0, 1, 486, 65);
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelSelectOption.setForeground(Color.BLACK);
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);

		rdbtnNewRadioButton = new JRadioButton("English");
		rdbtnNewRadioButton.setBounds(319, 5, 101, 21);
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("en"));
				System.out.println("Locale: " + Locale.getDefault());
				paintAgain();
			}
		});
		buttonGroup.add(rdbtnNewRadioButton);

		rdbtnNewRadioButton_1 = new JRadioButton("Euskara");
		rdbtnNewRadioButton_1.setBounds(101, 5, 108, 21);
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Locale.setDefault(new Locale("eus"));
				System.out.println("Locale: " + Locale.getDefault());
				paintAgain();
			}
		});
		buttonGroup.add(rdbtnNewRadioButton_1);

		rdbtnNewRadioButton_2 = new JRadioButton("Castellano");
		rdbtnNewRadioButton_2.setBounds(211, 5, 106, 21);
		rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("es"));
				System.out.println("Locale: " + Locale.getDefault());
				paintAgain();
			}
		});
		buttonGroup.add(rdbtnNewRadioButton_2);

		panel = new JPanel();
		panel.setBounds(0, 208, 486, 53);
		panel.setLayout(null);

		btnEncargadoButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Encargado"));
		btnEncargadoButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnEncargadoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new EncargadoGUI(MainGUI.this);
				a.setVisible(true);
			}
		});
		btnEncargadoButton.setBounds(0, 32, 101, 21);
		panel.add(btnEncargadoButton);
		panel.add(rdbtnNewRadioButton_1);
		panel.add(rdbtnNewRadioButton_2);
		panel.add(rdbtnNewRadioButton);

		jButtonLogin = new JButton();
		jButtonLogin.setBounds(0, 168, 486, 41);
		jButtonLogin.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Login"));
		jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new LoginGUI(MainGUI.this);
				a.setVisible(true);
			}
		});

		jButtonRegis = new JButton();
		jButtonRegis.setBounds(0, 117, 486, 41);
		jButtonRegis.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Registrarse"));
		jButtonRegis.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new RegistrarseGUI();

				a.setVisible(true);
			}
		});

		jButtonConsSes = new JButton();
		jButtonConsSes.setBounds(0, 66, 486, 41);
		jButtonConsSes.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ConsultarSesiones"));
		jButtonConsSes.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new ConSesionesGUI();
				a.setVisible(true);
			}
		});

		jContentPane = new JPanel();
		jContentPane.setLayout(null);
		jContentPane.add(jLabelSelectOption);
		jContentPane.add(jButtonLogin);
		jContentPane.add(jButtonRegis);
		jContentPane.add(jButtonConsSes);
		jContentPane.add(panel);

		setContentPane(jContentPane);
		setTitle("KIROL4ALL");

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
	}

	public void logEncargado() {
		btnEncargadoButton.setVisible(false);
		rdbtnNewRadioButton.setVisible(false);
		rdbtnNewRadioButton_1.setVisible(false);
		rdbtnNewRadioButton_2.setVisible(false);

		jContentPane.remove(btnEncargadoButton);
		jContentPane.remove(rdbtnNewRadioButton);
		jContentPane.remove(rdbtnNewRadioButton_1);
		jContentPane.remove(rdbtnNewRadioButton_2);

		for (ActionListener al : jButtonRegis.getActionListeners()) {
			jButtonRegis.removeActionListener(al);
		}

		for (ActionListener al : jButtonLogin.getActionListeners()) {
			jButtonLogin.removeActionListener(al);
		}
		for (ActionListener al : jButtonConsSes.getActionListeners()) {
			jButtonConsSes.removeActionListener(al);
		}

		jButtonConsSes.setText(ResourceBundle.getBundle("Etiquetas").getString("AñadirActividadGUI.AddActividad"));
		jButtonConsSes.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new AñadirActividadGUI();
				a.setVisible(true);
			}
		});

		jButtonRegis.setText(ResourceBundle.getBundle("Etiquetas").getString("PlanSesGUI.PlanificarSesion"));
		jButtonRegis.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new PlanSesGUI();
				a.setVisible(true);
			}
		});

		jButtonLogin.setText(ResourceBundle.getBundle("Etiquetas").getString("EnvFacturaGUI.EnviarFactura"));
		jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new EnvFacturasGUI();
				a.setVisible(true);
			}
		});

	}

	public void loginSocio(Socio socio) {
		btnEncargadoButton.setVisible(false);
		rdbtnNewRadioButton.setVisible(false);
		rdbtnNewRadioButton_1.setVisible(false);
		rdbtnNewRadioButton_2.setVisible(false);

		jContentPane.remove(btnEncargadoButton);
		jContentPane.remove(rdbtnNewRadioButton);
		jContentPane.remove(rdbtnNewRadioButton_1);
		jContentPane.remove(rdbtnNewRadioButton_2);

		jButtonConsSes.setBounds(0, 66, 243, 41);
		for (ActionListener al : jButtonRegis.getActionListeners()) {
			jButtonRegis.removeActionListener(al);
		}

		jButtonRegis.setBounds(0, 117, 243, 41);
		jButtonRegis.setText(ResourceBundle.getBundle("Etiquetas").getString("VisualizarResGUI.VisualizarRes"));
		jButtonRegis.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new VisualizarResGUI(socio);
				a.setVisible(true);
			}
		});
		for (ActionListener al : jButtonLogin.getActionListeners()) {
			jButtonLogin.removeActionListener(al);
		}
		jButtonLogin.setBounds(244, 66, 244, 41);
		jButtonLogin.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Reservar"));
		jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new ResSesionesGUI(socio);
				a.setVisible(true);
			}
		});
		jButtonCancelarRes = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CancelarResGUI.CancelarRes"));
		jButtonCancelarRes.setBounds(244, 117, 244, 41);
		jButtonCancelarRes.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new CancelarResGUI(socio);
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonCancelarRes);

		jButtonConsFact = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ConsFacturaGUI.ConsFactura"));
		jButtonConsFact.setBounds(0, 168, 243, 41);
		jButtonConsFact.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new ConsFacturaGUI(socio);
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonConsFact);

		jButtonPagFact = new JButton(ResourceBundle.getBundle("Etiquetas").getString("PagarGUI.Pagar"));
		jButtonPagFact.setBounds(244, 168, 244, 41);
		jButtonPagFact.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new PagarGUI(socio);
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonPagFact);
	}

	private void paintAgain() {
		jLabelSelectOption.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jButtonLogin.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Login"));
		jButtonRegis.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Registrarse"));
		jButtonConsSes.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ConsultarSesiones"));
		btnEncargadoButton.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Encargado"));

	}
}