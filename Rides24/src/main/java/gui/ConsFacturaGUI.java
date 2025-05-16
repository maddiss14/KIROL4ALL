package gui;

import businessLogic.BLFacade;
import domain.Factura;
import domain.Reserva;
import domain.Socio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

public class ConsFacturaGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private JTable tableFacturas = new JTable();
	private DefaultTableModel tableModelFacturas;
	private String[] columnNames = { ResourceBundle.getBundle("Etiquetas").getString("PagarGUI.Codigo"),
			ResourceBundle.getBundle("Etiquetas").getString("ConsSesionesGUI.Fecha"),
			ResourceBundle.getBundle("Etiquetas").getString("PagarGUI.CosteTotal"), "Facturas" };
	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
	private final JLabel jLabelEvents = new JLabel(
			ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.Rides"));

	public ConsFacturaGUI(Socio socio) {

		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("ConsFacturaGUI.ConsFactura"));
		getContentPane().setLayout(null);
		this.setSize(new Dimension(624, 422));

		tableModelFacturas = new DefaultTableModel(null, columnNames);
		tableFacturas.setModel(tableModelFacturas);
		JScrollPane scrollPane = new JScrollPane(tableFacturas);
		scrollPane.setBounds(50, 50, 500, 250);
		getContentPane().add(scrollPane);

		jButtonClose.setBounds(new Rectangle(232, 323, 130, 30));

		jButtonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton2_actionPerformed(e);
			}
		});

		this.getContentPane().add(jButtonClose, null);

		try {
			tableModelFacturas.setDataVector(null, columnNames);
			tableModelFacturas.setColumnCount(4);

			BLFacade facade = MainGUI.getBusinessLogic();
			List<Factura> facturas = facade.consFacturas(socio);

			if (facturas.isEmpty())
				jLabelEvents.setText(ResourceBundle.getBundle("Etiquetas").getString("ConsSesionesGUI.NoSesiones"));
			else
				jLabelEvents.setText(ResourceBundle.getBundle("Etiquetas").getString("ConsSesionesGUI.Sesiones"));
			for (domain.Factura factura : facturas) {
				Vector<Object> row = new Vector<Object>();

				row.add(factura.getCodigo());

				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yy");
				row.add(formato.format(factura.getFecha()));

				row.add(factura.getCoste());

				row.add(factura);

				tableModelFacturas.addRow(row);
			}
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		tableFacturas.getColumnModel().getColumn(0).setPreferredWidth(50);
		tableFacturas.getColumnModel().getColumn(1).setPreferredWidth(130);
		tableFacturas.getColumnModel().getColumn(1).setPreferredWidth(50);
		tableFacturas.getColumnModel().removeColumn(tableFacturas.getColumnModel().getColumn(3));
		tableFacturas.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int selectedRow = tableFacturas.getSelectedRow();
				if (selectedRow >= 0) {
					Factura factura = (Factura) tableModelFacturas.getValueAt(selectedRow, 3);
					List<String> info = new ArrayList<>();

					for (Reserva reserva : factura.getReserva()) {
						String inforR = "- Actividad: " + reserva.getSesion().getActividad().getNombre() + " | Sala: "
								+ reserva.getSesion().getSala().getNombre() + " | Coste: " + reserva.getCoste();
						info.add(inforR);
					}

					String mensaje = String.join("\n", info);
					JOptionPane.showMessageDialog(null, mensaje, "Reservas de la factura",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

	}

	private void jButton2_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}
