package gui;

import java.text.DateFormat;

import java.util.*;
import java.util.List;

import javax.swing.*;

import com.toedter.calendar.JCalendar;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import businessLogic.BLFacade;
import configuration.UtilDate;
import domain.Actividades;
import domain.Sala;
import domain.Sesion;
import exceptions.SalaOcupadaException;
import exceptions.SesionMustBeLaterThanTodayException;

public class PlanSesGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private JLabel jLabelActividad = new JLabel(
			ResourceBundle.getBundle("Etiquetas").getString("PlanSesGUI.Actividad"));
	private JLabel jLabelHoraInicio = new JLabel(
			ResourceBundle.getBundle("Etiquetas").getString("PlanSesGUI.HoraInicio"));
	private JLabel jLabelHoraFin = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("PlanSesGUI.HoraFin"));
	private JLabel jLabelSala = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("PlanSesGUI.Sala"));

	private JCalendar jCalendar = new JCalendar();
	private Calendar calendarAct = null;
	private Calendar calendarAnt = null;

	private JScrollPane scrollPaneEvents = new JScrollPane();

	private JButton jButtonCreate = new JButton(
			ResourceBundle.getBundle("Etiquetas").getString("PlanSesGUI.PlanificarSesion"));
	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
	private JLabel jLabelMsg = new JLabel();
	private JLabel jLabelError = new JLabel();

	private JComboBox<String> horaInicioComboBox = new JComboBox<String>();
	private DefaultComboBoxModel<String> modeloHoraInicio = new DefaultComboBoxModel<String>();

	private JComboBox<String> horaFinComboBox = new JComboBox<String>();
	private DefaultComboBoxModel<String> modeloHoraFin = new DefaultComboBoxModel<String>();

	private JComboBox<String> actividadComboBox = new JComboBox<String>();
	private DefaultComboBoxModel<String> modeloActividades = new DefaultComboBoxModel<String>();

	private JComboBox<String> salaComboBox = new JComboBox<String>();
	private DefaultComboBoxModel<String> modeloSala = new DefaultComboBoxModel<String>();

	public PlanSesGUI() {

		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(604, 370));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("PlanSesGUI.PlanificarSesion"));

		jLabelActividad.setBounds(new Rectangle(6, 56, 92, 20));
		jLabelHoraInicio.setBounds(new Rectangle(6, 148, 92, 20));

		jLabelHoraFin.setBounds(new Rectangle(6, 180, 92, 20));

		jCalendar.setBounds(new Rectangle(300, 50, 225, 150));
		scrollPaneEvents.setBounds(new Rectangle(25, 44, 346, 116));

		jButtonCreate.setBounds(new Rectangle(100, 263, 130, 30));

		jButtonCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonCreate_actionPerformed(e);
			}
		});
		jButtonClose.setBounds(new Rectangle(275, 263, 130, 30));
		jButtonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonClose_actionPerformed(e);
			}
		});

		jLabelMsg.setBounds(new Rectangle(275, 214, 305, 20));
		jLabelMsg.setForeground(Color.red);

		jLabelError.setBounds(new Rectangle(6, 191, 320, 20));
		jLabelError.setForeground(Color.red);

		this.getContentPane().add(jLabelMsg, null);
		this.getContentPane().add(jLabelError, null);

		this.getContentPane().add(jButtonClose, null);
		this.getContentPane().add(jButtonCreate, null);

		this.getContentPane().add(jLabelHoraInicio, null);
		this.getContentPane().add(jLabelActividad, null);

		this.getContentPane().add(jCalendar, null);

		this.getContentPane().add(jLabelHoraFin, null);

		BLFacade facade = MainGUI.getBusinessLogic();

		facade = MainGUI.getBusinessLogic();

		List<Actividades> actividades = facade.getActividades();
		for (Actividades actividad : actividades)
			modeloActividades.addElement(actividad.getNombre());

		actividadComboBox.setModel(modeloActividades);
		actividadComboBox.setBounds(100, 56, 163, 21);
		getContentPane().add(actividadComboBox);

		List<Sala> salas = facade.getSalas();
		for (Sala sala : salas) {
			modeloSala.addElement(sala.getNombre());
		}

		salaComboBox.setModel(modeloSala);
		salaComboBox.setBounds(100, 100, 163, 21);
		getContentPane().add(salaComboBox);

		for (int i = 8; i <= 21; i++) {
			String hora = String.format("%02d:00", i);
			modeloHoraInicio.addElement(hora);
		}
		horaInicioComboBox.setModel(modeloHoraInicio);
		horaInicioComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actualizarHorasFin();
			}
		});
		horaInicioComboBox.setBounds(100, 148, 60, 21);
		getContentPane().add(horaInicioComboBox);

		horaFinComboBox.setBounds(100, 179, 60, 21);
		getContentPane().add(horaFinComboBox);

		jLabelSala.setBounds(new Rectangle(6, 148, 92, 20));
		jLabelSala.setBounds(6, 101, 92, 20);

		getContentPane().add(jLabelSala);

		this.jCalendar.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent propertychangeevent) {
//			
				if (propertychangeevent.getPropertyName().equals("locale")) {
					jCalendar.setLocale((Locale) propertychangeevent.getNewValue());
				} else if (propertychangeevent.getPropertyName().equals("calendar")) {
					calendarAnt = (Calendar) propertychangeevent.getOldValue();
					calendarAct = (Calendar) propertychangeevent.getNewValue();
					DateFormat dateformat1 = DateFormat.getDateInstance(1, jCalendar.getLocale());

					int monthAnt = calendarAnt.get(Calendar.MONTH);
					int monthAct = calendarAct.get(Calendar.MONTH);
					if (monthAct != monthAnt) {
						if (monthAct == monthAnt + 2) {
							// Si en JCalendar está 30 de enero y se avanza al mes siguiente, devolverá 2 de
							// marzo (se toma como equivalente a 30 de febrero)
							// Con este código se dejará como 1 de febrero en el JCalendar
							calendarAct.set(Calendar.MONTH, monthAnt + 1);
							calendarAct.set(Calendar.DAY_OF_MONTH, 1);
						}

						jCalendar.setCalendar(calendarAct);

					}
					jCalendar.setCalendar(calendarAct);
					int offset = jCalendar.getCalendar().get(Calendar.DAY_OF_WEEK);

					if (Locale.getDefault().equals(new Locale("es")))
						offset += 4;
					else
						offset += 5;
					Component o = (Component) jCalendar.getDayChooser().getDayPanel()
							.getComponent(jCalendar.getCalendar().get(Calendar.DAY_OF_MONTH) + offset);
				}
			}
		});

	}

	private void jButtonCreate_actionPerformed(ActionEvent e) {
		jLabelMsg.setText("");

		try {
			BLFacade facade = MainGUI.getBusinessLogic();
			Actividades actividad = facade
					.getActividad(modeloActividades.getElementAt(actividadComboBox.getSelectedIndex()));
			Sala sala = facade.getSala(modeloSala.getElementAt(salaComboBox.getSelectedIndex()));
			String horaI = modeloHoraInicio.getElementAt(horaInicioComboBox.getSelectedIndex());
			String horaF = modeloHoraFin.getElementAt(horaFinComboBox.getSelectedIndex());

			Sesion s = facade.planSes(actividad, UtilDate.trim(jCalendar.getDate()), horaI, horaF, sala);
			jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("PlanSesGUI.SesionCreada"));

		} catch (SesionMustBeLaterThanTodayException e1) {
			jLabelMsg.setText(e1.getMessage());
		} catch (SalaOcupadaException e1) {
			jLabelMsg.setText(e1.getMessage());
		}

	}

	private void jButtonClose_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	private void actualizarHorasFin() {
		modeloHoraFin.removeAllElements();
		String horaSeleccionada = (String) horaInicioComboBox.getSelectedItem();
		if (horaSeleccionada != null) {
			int horaInicio = Integer.parseInt(horaSeleccionada.split(":")[0]);
			for (int i = horaInicio + 1; i <= 22; i++) {
				String hora = String.format("%02d:00", i);
				modeloHoraFin.addElement(hora);
			}
		}
		horaFinComboBox.setModel(modeloHoraFin);
	}

}
