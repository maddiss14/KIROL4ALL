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
public class Actividades implements Serializable {

	private static final long serialVersionUID = 1L;
	@XmlID
	@Id
	private String nombre;
	private int gExigencia;
	private int precio;

	@XmlIDREF
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Sesion> sesion= new Vector<Sesion>();
	
	public Actividades() {
		super();
	}
	
	
	public Actividades(String nombre, int gExigencia, int precio) {
		super();
		this.nombre = nombre;
		this.gExigencia = gExigencia;
		this.precio = precio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getgExigencia() {
		return gExigencia;
	}

	public List<Sesion> getSesion() {
		return sesion;
	}

	public void setSesion(List<Sesion> sesion) {
		this.sesion = sesion;
	}


	public void setgExigencia(int gExigencia) {
		this.gExigencia = gExigencia;
	}

	public int getPrecio() {
		return precio;
	}

	public void setPrecio(int precio) {
		this.precio = precio;
	}

}
