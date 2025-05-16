package domain;

import java.io.Serializable;

import java.util.List;
import java.util.Vector;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Socio implements Serializable {

	private static final long serialVersionUID = 1L;
	@XmlID
	@Id
	private String mail;
	private String contra;
	private String nom;
	private String mPago;
	private int numReservas;
	private int numMaxReserva;

	@XmlIDREF
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Reserva> reservas = new Vector<Reserva>();
	
	@XmlIDREF
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Factura> facturas = new Vector<Factura>();
	
	public Socio() {
		super();
	}

	public Socio(String nom, String mail, String contra, String mPago) {
		super();
		this.mail = mail;
		this.contra = contra;
		this.nom = nom;
		this.mPago = mPago;
		this.numMaxReserva = 10;
		numReservas = 0;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getContra() {
		return contra;
	}

	public void setContra(String contra) {
		this.contra = contra;
	}

	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	
	public List<Reserva> getReservas() {
		return reservas;
	}
	
	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}

	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	public String getMPago() {
		return mPago;
	}

	public void setMPago(String mPago) {
		this.mPago = mPago;
	}

	public int getNumReservas() {
		return numReservas;
	}

	public void setNumReservas(int numReservas) {
		this.numReservas = numReservas;
	}

	public int getNumMaxReserva() {
		return numMaxReserva;
	}

	public void setNumMaxReserva(int numMaxReserva) {
		this.numMaxReserva = numMaxReserva;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Socio other = (Socio) obj;
		if (mail != other.mail)
			return false;
		return true;
	}
}
