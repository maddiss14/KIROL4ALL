package gui;

import businessLogic.BLFacade;
import configuration.UtilDate;

import com.toedter.calendar.JCalendar;

import domain.Actividades;
import domain.Reserva;
import domain.Socio;
import exceptions.ActividadAlreadyExistsException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.DateFormat;
import java.util.*;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class AñadirActividadGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JButton btnAddAct = new JButton();
	private JSlider gExigencia = new JSlider();
	private JLabel lblValorSlider = new JLabel("1"); 
	

	private JLabel lblPrecio = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("AñadirActividadGUI.Precio"));
	private JLabel lblNombreActividad = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("AñadirActividadGUI.NomActi"));
	private JLabel lblGradoExigencia = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("AñadirActividadGUI.GExig"));
	
	private JTextField nombreActividad;
	private JTextField precio;
	
	private JTextArea textArea;
	
	public AñadirActividadGUI() {

		textArea = new JTextArea();
		textArea.setBounds(10, 231, 466, 22);
		getContentPane().add(textArea);
		textArea.setVisible(false);
		textArea.setEditable(false);

		getContentPane().setLayout(null);
		this.setSize(new Dimension(500, 300));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("AñadirActividadGUI.AddActividad"));

		nombreActividad = new JTextField();
		nombreActividad.setBounds(103, 46, 277, 26);
		getContentPane().add(nombreActividad);
		nombreActividad.setColumns(10);
		lblNombreActividad.setHorizontalAlignment(SwingConstants.CENTER);

		lblNombreActividad.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNombreActividad.setBounds(10, 10, 466, 26);
		getContentPane().add(lblNombreActividad);
		lblGradoExigencia.setHorizontalAlignment(SwingConstants.CENTER);

		lblGradoExigencia.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblGradoExigencia.setBounds(10, 82, 230, 22);
		getContentPane().add(lblGradoExigencia);
		
		lblValorSlider.setHorizontalAlignment(SwingConstants.CENTER);
		lblValorSlider.setBounds(10, 139, 230, 20);
		lblValorSlider.setFont(new Font("Tahoma", Font.PLAIN, 16));
		getContentPane().add(lblValorSlider);

		gExigencia.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int valor = gExigencia.getValue();
			    lblValorSlider.setText(String.valueOf(valor));
			}
		});
		gExigencia.setValue(1);
		gExigencia.setMaximum(5);
		gExigencia.setMinimum(1);
		gExigencia.setBounds(20, 114, 206, 26);
		getContentPane().add(gExigencia);
		
		lblPrecio.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrecio.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPrecio.setBounds(246, 82, 230, 22);
		getContentPane().add(lblPrecio);
		
		precio = new JTextField();
		precio.setBounds(313, 114, 96, 19);
		getContentPane().add(precio);
		precio.setColumns(10);

		btnAddAct = new JButton(ResourceBundle.getBundle("Etiquetas").getString("AñadirActividadGUI.AddActividad"));
		btnAddAct.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnAddAct.setBounds(145, 187, 187, 34);
		getContentPane().add(btnAddAct);
		btnAddAct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!(nombreActividad.getText().isEmpty() && precio.getText().isEmpty())) {
					BLFacade facade = MainGUI.getBusinessLogic();
					try {
						facade.añadirActividad(nombreActividad.getText(), gExigencia.getValue(), Integer.parseInt(precio.getText()));
						textArea.setText(ResourceBundle.getBundle("Etiquetas").getString("AñadirActividadGUI.ActividadAñadida"));
					} catch (NumberFormatException e1) {
						textArea.setText(ResourceBundle.getBundle("Etiquetas").getString("AñadirActividadGUI.FormatoIncorrecto"));
						textArea.setVisible(true);
					} catch (ActividadAlreadyExistsException e1) {
						textArea.setText(ResourceBundle.getBundle("Etiquetas").getString("AñadirActividadGUI.ActividadExistente"));
						textArea.setVisible(true);
					}
					textArea.setVisible(true);
				}
			}
		});
	}
}
