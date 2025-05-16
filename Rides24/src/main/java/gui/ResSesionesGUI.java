package gui;

import businessLogic.BLFacade;

import configuration.UtilDate;

import com.toedter.calendar.JCalendar;
import domain.Sesion;
import domain.Socio;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class ResSesionesGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JLabel jLabelEventDate = new JLabel(
			ResourceBundle.getBundle("Etiquetas").getString("ResSesionesGUI.FechaSesion"));
	private final JLabel jLabelEvents = new JLabel(
			ResourceBundle.getBundle("Etiquetas").getString("ConsSesionesGUI.Sesiones"));
	private final JLabel lblNewLabel = new JLabel();

	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));

	private JButton jButtonReservar = new JButton(
			ResourceBundle.getBundle("Etiquetas").getString("ResSesionesGUI.Reservar"));

	// Code for JCalendar
	private JCalendar jCalendar1 = new JCalendar();
	private Calendar calendarAnt = null;
	private Calendar calendarAct = null;
	private JScrollPane scrollPaneEvents = new JScrollPane();

	private List<Date> datesWithRidesCurrentMonth = new Vector<Date>();

	private JTable tableSesiones = new JTable();

	private DefaultTableModel tableModelRides;

	private String[] columnNamesRides = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("ConsSesionesGUI.Actividad"),
			ResourceBundle.getBundle("Etiquetas").getString("ConsSesionesGUI.Fecha"),
			ResourceBundle.getBundle("Etiquetas").getString("ConsSesionesGUI.Hora") };

	public ResSesionesGUI(Socio socio) {

		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(646, 500));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("ResSesionesGUI.ResSesiones"));
		jLabelEventDate.setHorizontalAlignment(SwingConstants.CENTER);

		jLabelEventDate.setBounds(new Rectangle(200, 15, 225, 25));
		jLabelEvents.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelEvents.setBounds(139, 210, 346, 16);

		this.getContentPane().add(jLabelEventDate, null);
		this.getContentPane().add(jLabelEvents);

		jButtonReservar.setEnabled(false);

		jButtonClose.setBounds(new Rectangle(355, 396, 130, 30));

		jButtonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton2_actionPerformed(e);
			}
		});

		this.getContentPane().add(jButtonClose, null);

		jCalendar1.setBounds(new Rectangle(200, 50, 225, 150));

		// Code for JCalendar
		jCalendar1.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent propertychangeevent) {

				if (propertychangeevent.getPropertyName().equals("locale")) {
					jCalendar1.setLocale((Locale) propertychangeevent.getNewValue());
				} else if (propertychangeevent.getPropertyName().equals("calendar")) {
					calendarAnt = (Calendar) propertychangeevent.getOldValue();
					calendarAct = (Calendar) propertychangeevent.getNewValue();

					DateFormat dateformat1 = DateFormat.getDateInstance(1, jCalendar1.getLocale());

					int monthAnt = calendarAnt.get(Calendar.MONTH);
					int monthAct = calendarAct.get(Calendar.MONTH);

					if (monthAct != monthAnt) {
						if (monthAct == monthAnt + 2) {
							// Si en JCalendar está 30 de enero y se avanza al mes siguiente, devolvería 2
							// de marzo (se toma como equivalente a 30 de febrero)
							// Con este código se dejará como 1 de febrero en el JCalendar
							calendarAct.set(Calendar.MONTH, monthAnt + 1);
							calendarAct.set(Calendar.DAY_OF_MONTH, 1);
						}

						jCalendar1.setCalendar(calendarAct);

					}

					try {
						tableModelRides.setDataVector(null, columnNamesRides);
						tableModelRides.setColumnCount(4); // another column added to allocate ride objects

						BLFacade facade = MainGUI.getBusinessLogic();
						List<Sesion> sesiones = facade.getSesiones(UtilDate.trim(jCalendar1.getDate()));

						if (sesiones.isEmpty())
							jLabelEvents.setText(
									ResourceBundle.getBundle("Etiquetas").getString("ConsSesionesGUI.NoSesiones") + ": "
											+ dateformat1.format(calendarAct.getTime()));
						else
							jLabelEvents
									.setText(ResourceBundle.getBundle("Etiquetas").getString("ConsSesionesGUI.Sesiones")
											+ ": " + dateformat1.format(calendarAct.getTime()));
						for (domain.Sesion sesion : sesiones) {
							Vector<Object> row = new Vector<Object>();

							row.add(sesion.getActividad().getNombre());

							// Formateo la fecha para que aparezca como dd/MM/yy
							SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yy");
							row.add(formato.format(sesion.getFecha()));

							row.add(sesion.getHoraI() + " - " + sesion.getHoraF());
							row.add(sesion);

							tableModelRides.addRow(row);
						}
						datesWithRidesCurrentMonth = facade.getThisMonthDatesWithSesiones(jCalendar1.getDate());
						paintDaysWithEvents(jCalendar1, datesWithRidesCurrentMonth, Color.CYAN);

					} catch (Exception e1) {

						e1.printStackTrace();
					}
					tableSesiones.getColumnModel().getColumn(0).setPreferredWidth(50);
					tableSesiones.getColumnModel().getColumn(1).setPreferredWidth(130);
					tableSesiones.getColumnModel().getColumn(1).setPreferredWidth(50);
					tableSesiones.getColumnModel().removeColumn(tableSesiones.getColumnModel().getColumn(3)); // not
																												// shown
																												// in
					// JTable

				}
			}

		});

		this.getContentPane().add(jCalendar1, null);

		scrollPaneEvents.setBounds(new Rectangle(139, 236, 346, 150));

		scrollPaneEvents.setViewportView(tableSesiones);
		tableModelRides = new DefaultTableModel(null, columnNamesRides);

		tableSesiones.setModel(tableModelRides);

		tableModelRides.setDataVector(null, columnNamesRides);
		tableModelRides.setColumnCount(4); // another column added to allocate ride objects

		tableSesiones.getColumnModel().getColumn(0).setPreferredWidth(50);
		tableSesiones.getColumnModel().getColumn(1).setPreferredWidth(120);
		tableSesiones.getColumnModel().getColumn(1).setPreferredWidth(50);

		tableSesiones.getColumnModel().removeColumn(tableSesiones.getColumnModel().getColumn(3)); // not shown in JTable

		tableSesiones.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int selectedRow = tableSesiones.getSelectedRow();
					if (selectedRow >= 0) {
						Sesion sesion = (Sesion) tableModelRides.getValueAt(selectedRow, 3);

						Calendar calSesion = Calendar.getInstance();
						calSesion.setTime(sesion.getFecha());

						String[] horaInicioParts = sesion.getHoraI().split(":");
						int hora = Integer.parseInt(horaInicioParts[0]);

						calSesion.set(Calendar.HOUR_OF_DAY, hora);
						calSesion.set(Calendar.MINUTE, 0);
						calSesion.set(Calendar.SECOND, 0);

						Date ahora = new Date();

						if (calSesion.getTime().before(ahora)) {
							jButtonReservar.setEnabled(false);
						} else {
							jButtonReservar.setEnabled(true);
						}
					} else {
						jButtonReservar.setEnabled(false);
					}
				}
			}
		});

		jButtonReservar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sesion sesion = (Sesion) tableModelRides.getValueAt(tableSesiones.getSelectedRow(), 3); // La columna 3
																										// tiene el
				// objeto Sesion
				BLFacade facade = MainGUI.getBusinessLogic();
				if (facade.resDisponible(socio)) {
					if (facade.estaLlena(sesion)) {
						facade.addListaEspera(socio, sesion);
						System.out.println("Añadido a la lista de espera de: " + sesion);
						lblNewLabel
								.setText(ResourceBundle.getBundle("Etiquetas").getString("ResSesionesGUI.ListaEspera"));
					} else {
						int id = facade.reservarSesion(socio, sesion);
						System.out.println("Reservado correctamente, idReserva: " + id);
						lblNewLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("ResSesionesGUI.Reservado")
								+ ": " + id);
					}

				} else {
					System.out.println("Ha superado el número máximo de reservas");
					lblNewLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("ResSesionesGUI.NumMaxRes"));
				}
			}
		});
		this.getContentPane().add(scrollPaneEvents, null);
		paintDaysWithEvents(jCalendar1, datesWithRidesCurrentMonth, Color.CYAN);

		jButtonReservar.setBounds(new Rectangle(355, 396, 130, 30));
		jButtonReservar.setBounds(139, 396, 130, 30);
		getContentPane().add(jButtonReservar);
		lblNewLabel.setBounds(49, 430, 541, 23);

		getContentPane().add(lblNewLabel);

	}

	public static void paintDaysWithEvents(JCalendar jCalendar, List<Date> datesWithEventsCurrentMonth, Color color) {
		// // For each day with events in current month, the background color for that
		// day is changed to cyan.

		Calendar calendar = jCalendar.getCalendar();

		int month = calendar.get(Calendar.MONTH);
		int today = calendar.get(Calendar.DAY_OF_MONTH);
		int year = calendar.get(Calendar.YEAR);

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int offset = calendar.get(Calendar.DAY_OF_WEEK);

		if (Locale.getDefault().equals(new Locale("es")))
			offset += 4;
		else
			offset += 5;

		for (Date d : datesWithEventsCurrentMonth) {

			calendar.setTime(d);

			// Obtain the component of the day in the panel of the DayChooser of the
			// JCalendar.
			// The component is located after the decorator buttons of "Sun", "Mon",... or
			// "Lun", "Mar"...,
			// the empty days before day 1 of month, and all the days previous to each day.
			// That number of components is calculated with "offset" and is different in
			// English and Spanish
			// Component o=(Component)
			// jCalendar.getDayChooser().getDayPanel().getComponent(i+offset);;
			Component o = (Component) jCalendar.getDayChooser().getDayPanel()
					.getComponent(calendar.get(Calendar.DAY_OF_MONTH) + offset);
			o.setBackground(color);
		}

		calendar.set(Calendar.DAY_OF_MONTH, today);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);

	}

	private void jButton2_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}
