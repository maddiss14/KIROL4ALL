package gui;

import businessLogic.BLFacade;

import domain.Factura;
import domain.Reserva;
import domain.Socio;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

public class PagarGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTable tableFacturas = new JTable();
	private DefaultTableModel tableModelFacturas;
	private String[] columnNames = { ResourceBundle.getBundle("Etiquetas").getString("PagarGUI.Codigo"),
			ResourceBundle.getBundle("Etiquetas").getString("ConsSesionesGUI.Fecha"),
			ResourceBundle.getBundle("Etiquetas").getString("PagarGUI.CosteTotal"), "Facturas" };
	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
	private JButton jButtonPagar = new JButton(ResourceBundle.getBundle("Etiquetas").getString("PagarGUI.Pagar"));

	private final JLabel jLabelEvents = new JLabel(
			ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.Rides"));
	private final JLabel lblNewLabel = new JLabel();

	public PagarGUI(Socio socio) {

		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("PagarGUI.Pagar"));
		this.setSize(new Dimension(646, 366));
		getContentPane().setLayout(null);
		jButtonPagar.setEnabled(false);

		tableModelFacturas = new DefaultTableModel(null, columnNames);
		tableFacturas.setModel(tableModelFacturas);
		JScrollPane scrollPane = new JScrollPane(tableFacturas);
		scrollPane.setBounds(56, 41, 481, 199);
		getContentPane().add(scrollPane);

		jButtonClose.setBounds(new Rectangle(518, 291, 104, 28));

		jButtonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton2_actionPerformed(e);
			}
		});

		this.getContentPane().add(jButtonClose, null);

		jButtonPagar.setBounds(213, 250, 121, 40);
		getContentPane().add(jButtonPagar);
		cargarFacturas(socio);

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

		tableFacturas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int selectedRow = tableFacturas.getSelectedRow();
					if (selectedRow >= 0) {
						jButtonPagar.setEnabled(true);
					} else {
						jButtonPagar.setEnabled(false);
					}
				}
			}
		});
		jButtonPagar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Factura factura = (Factura) tableModelFacturas.getValueAt(tableFacturas.getSelectedRow(), 3);

				BLFacade facade = MainGUI.getBusinessLogic();
				if (facade.cobrarFact(factura.getCodigo(), socio.getMPago())) {
					lblNewLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("PagarGUI.PagoAceptado"));
				} else {
					lblNewLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("PagarGUI.PagoDenegado"));
				}
				cargarFacturas(socio);
			}

		});

		jButtonPagar.setBounds(new Rectangle(355, 396, 130, 30));
		jButtonPagar.setBounds(219, 250, 188, 30);
		getContentPane().add(jButtonPagar);
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(56, 18, 481, 13);

		getContentPane().add(lblNewLabel);
	}

	private void cargarFacturas(Socio socio) {
		try {
			tableModelFacturas.setDataVector(null, columnNames);
			tableModelFacturas.setColumnCount(4);

			BLFacade facade = MainGUI.getBusinessLogic();
			List<Factura> facturas = facade.consFacturas(socio);

			if (facturas.isEmpty())
				jLabelEvents.setText(ResourceBundle.getBundle("Etiquetas").getString("ConsFacturasGUI.NoFacturas"));
			else
				jLabelEvents.setText(ResourceBundle.getBundle("Etiquetas").getString("ConsFacturasGUI.Facturas"));
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
	}

	private void jButton2_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}
