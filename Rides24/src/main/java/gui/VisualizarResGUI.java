package gui;

import businessLogic.BLFacade;
import domain.Reserva;
import domain.Sesion;
import domain.Socio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class VisualizarResGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private final JLabel jLabelEvents = new JLabel();

	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));

	private JTable tableReservas = new JTable();
	private DefaultTableModel tableModelReservas;

	private String[] columnNamesReservas = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("PlanSesGUI.Actividad"),
			ResourceBundle.getBundle("Etiquetas").getString("PlanSesGUI.Sala"),
			ResourceBundle.getBundle("Etiquetas").getString("VisualizarResGUI.Precio"),
			ResourceBundle.getBundle("Etiquetas").getString("ConsSesionesGUI.Hora"),
			ResourceBundle.getBundle("Etiquetas").getString("ConsSesionesGUI.Fecha"),
			ResourceBundle.getBundle("Etiquetas").getString("VisualizarResGUI.Reservas") };

	public VisualizarResGUI(Socio socio) {

		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(646, 366));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("VisualizarResGUI.VisualizarRes"));

		jLabelEvents.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelEvents.setBounds(139, 50, 346, 16);

		this.getContentPane().add(jLabelEvents);

		jButtonClose.setBounds(new Rectangle(249, 274, 130, 30));

		jButtonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton2_actionPerformed(e);
			}
		});

		this.getContentPane().add(jButtonClose, null);

		JScrollPane scrollPane = new JScrollPane(tableReservas);
		scrollPane.setBounds(new Rectangle(139, 92, 346, 150));
		getContentPane().add(scrollPane);

		tableModelReservas = new DefaultTableModel(null, columnNamesReservas);
		tableReservas.setModel(tableModelReservas);
		try {
			BLFacade facade = MainGUI.getBusinessLogic();

			List<Reserva> reservas = facade.visualizarReservas(socio);
			tableModelReservas.setRowCount(0);

			int total = 0;
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

			for (Reserva reserva : reservas) {
				Vector<Object> row = new Vector<Object>();
				Sesion sesion = reserva.getSesion();

				row.add(sesion.getActividad().getNombre());
				row.add(sesion.getSala().getNombre());
				row.add(reserva.getCoste());
				row.add(sesion.getHoraI() + " - " + sesion.getHoraF());
				row.add(formato.format(sesion.getFecha()));
				row.add(reserva);

				tableModelReservas.addRow(row);
				total++;
			}

			if (total == 0) {
				jLabelEvents.setText(ResourceBundle.getBundle("Etiquetas").getString("VisualizarResGUI.NoReservas"));
			} else {
				jLabelEvents.setText(
						ResourceBundle.getBundle("Etiquetas").getString("VisualizarResGUI.NumReservas") + ": " + total);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		tableReservas.getColumnModel().getColumn(0).setPreferredWidth(45);
		tableReservas.getColumnModel().getColumn(1).setPreferredWidth(40);
		tableReservas.getColumnModel().getColumn(2).setPreferredWidth(25);
		tableReservas.getColumnModel().removeColumn(tableReservas.getColumnModel().getColumn(5));
	}

	private void jButton2_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}
