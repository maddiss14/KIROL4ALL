package domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class Sala implements Serializable {

	private static final long serialVersionUID = 1L;
	@XmlID
	@Id
	private String nombre;
	private int aforoMax;

	@XmlIDREF
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Sesion> sesion = new Vector<Sesion>();

	public Sala() {
		super();
	}

	public Sala(String nombre, int aforoMax) {
		super();
		this.nombre = nombre;
		this.aforoMax = aforoMax;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getAforoMax() {
		return aforoMax;
	}

	public void setAforoMax(int aforoMax) {
		this.aforoMax = aforoMax;
	}

	public List<Sesion> getSesion() {
		return sesion;
	}

	public void setSesion(List<Sesion> sesion) {
		this.sesion = sesion;
	}

	public boolean isSalaOcupada(Date fecha, String horaI, String horaF) {
		SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

		try {
			for (Sesion ses : sesion) {
				if (formatoFecha.format(ses.getFecha()).equals(formatoFecha.format(fecha))) {
					Date nuevaInicio = formatoHora.parse(horaI);
					Date nuevaFin = formatoHora.parse(horaF);

					Date existenteInicio = formatoHora.parse(ses.getHoraI());
					Date existenteFin = formatoHora.parse(ses.getHoraF());

					if (nuevaInicio.before(existenteFin) && nuevaFin.after(existenteInicio)) {
						return true;
					}

				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
