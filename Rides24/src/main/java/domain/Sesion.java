package domain;

import java.io.Serializable;


import java.time.LocalTime;
import java.util.Date;
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
public class Sesion implements Serializable {

	private static final long serialVersionUID = 1L;
	@XmlID
	@Id
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@GeneratedValue
	private Integer idSesion;
	private Date fecha;
	private String horaI;
	private String horaF;

	@XmlIDREF
	@ManyToOne(fetch = FetchType.EAGER)
	private Sala sala;

	@XmlIDREF
	@ManyToOne(fetch = FetchType.EAGER)
	private Actividades actividad;

	@XmlIDREF
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Reserva> listaEspera = new Vector<Reserva>();

	@XmlIDREF
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Reserva> reserva = new Vector<Reserva>();

	
	public Sesion() {
		super();
	}
	//Constructora
	public Sesion(Actividades actividad, Date fecha, String horaI, String horaF, Sala sala) {
		super();
		this.actividad = actividad;
		this.fecha = fecha;
		this.horaI = horaI;
		this.horaF = horaF;
		this.sala = sala;
	}

	//Getters y setters
	public int getIdSesion() {
		return idSesion;
	}

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public Actividades getActividad() {
		return actividad;
	}

	public void setActividad(Actividades actividad) {
		this.actividad = actividad;
	}

	public void setIdSesion(int idSesion) {
		this.idSesion = idSesion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getHoraI() {
		return horaI;
	}

	public void setHoraI(String horaI) {
		this.horaI = horaI;
	}

	public String getHoraF() {
		return horaF;
	}

	public void setHoraF(String horaF) {
		this.horaF = horaF;
	}

	public List<Reserva> getListaEspera() {
		return listaEspera;
	}

	public void setListaEspera(List<Reserva> listaEspera) {
		this.listaEspera = listaEspera;
	}

	public List<Reserva> getReserva() {
		return reserva;
	}

	public void setReserva(List<Reserva> reserva) {
		this.reserva = reserva;
	}
	
	public void addListaEspera(Reserva reserva) {
		listaEspera.add(reserva);
	}

}
