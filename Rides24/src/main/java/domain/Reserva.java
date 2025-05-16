package domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Reserva implements Serializable {

	private static final long serialVersionUID = 1L;
	@XmlID
	@Id
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@GeneratedValue
	private Integer idReserva;
	private int coste;
	private Date fechaReserva;

	@XmlIDREF
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private Sesion sesion;

	@XmlIDREF
	@ManyToOne
	private Socio socio;
	
	public Reserva() {
		super();
	}
	
	public Reserva(int coste, Date fechaReserva, Socio socio, Sesion sesion) {
		super();
		this.coste = coste;
		this.fechaReserva = fechaReserva;
		this.socio = socio;
		this.sesion = sesion;
	}

	public int getIdReserva() {
		return idReserva;
	}

	public void setIdReserva(int idReserva) {
		this.idReserva = idReserva;
	}

	public Date getFechaReserva() {
		return fechaReserva;
	}

	public void setFechaReserva(Date fechaReserva) {
		this.fechaReserva = fechaReserva;
	}

	public Socio getSocio() {
		return socio;
	}

	public void setSocio(Socio socio) {
		this.socio = socio;
	}

	public int getCoste() {
		return coste;
	}

	public void setCoste(int coste) {
		this.coste = coste;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reserva other = (Reserva) obj;
		if (idReserva != other.idReserva)
			return false;
		return true;
	}

	public Sesion getSesion() {
		return sesion;
	}

	public void setSesion(Sesion sesion) {
		this.sesion = sesion;
	}

	public void setIdReserva(Integer idReserva) {
		this.idReserva = idReserva;
	}
}