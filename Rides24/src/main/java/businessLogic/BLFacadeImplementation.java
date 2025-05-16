package businessLogic;

import java.time.LocalTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.jws.WebMethod;
import javax.jws.WebService;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.Sala;
import domain.Sesion;
import domain.Socio;
import domain.Actividades;
import domain.Factura;
import domain.Reserva;
import exceptions.SesionMustBeLaterThanTodayException;
import exceptions.SocioAlreadyExistsException;
import exceptions.ActividadAlreadyExistsException;
import exceptions.MetodoDePagoNoValidoException;
import exceptions.SalaOcupadaException;

/**
 * It implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation implements BLFacade {
	DataAccess dbManager;

	public BLFacadeImplementation() {
		System.out.println("Creating BLFacadeImplementation instance");

		dbManager = new DataAccess();

		// dbManager.close();

	}

	public BLFacadeImplementation(DataAccess da) {

		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		ConfigXML c = ConfigXML.getInstance();

		dbManager = da;
	}

	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public Sesion planSes(Actividades actividad, Date fecha, String horaI, String horaF, Sala sala)
			throws SesionMustBeLaterThanTodayException, SalaOcupadaException {

		dbManager.open();
		Sesion sesion = dbManager.planSes(actividad, fecha, horaI, horaF, sala);
		dbManager.close();
		return sesion;
	};

	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public List<Sesion> getSesiones(Date fecha) {
		dbManager.open();
		List<Sesion> sesiones = dbManager.getSesiones(fecha);
		dbManager.close();
		return sesiones;
	}

	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public List<Date> getThisMonthDatesWithSesiones(Date date) {
		dbManager.open();
		List<Date> dates = dbManager.getThisMonthDatesWithSesiones(date);
		dbManager.close();
		return dates;
	}

	public void close() {
		DataAccess dB4oManager = new DataAccess();

		dB4oManager.close();

	}

	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public void initializeBD() {
		dbManager.open();
		dbManager.initializeDB();
		dbManager.close();
	}

	@WebMethod
	public Socio getSocio(String mail, String contra) {
		dbManager.open();
		Socio socio = dbManager.getSocio(mail, contra);
		if (socio != null) {
			if (!socio.getContra().equals(contra)) {
				System.out.println("CONTRASEÑA INCORRECTA");
			}
		} else {
			System.out.println("EL USUARIO NO ESTA REGISTRADO");
		}
		dbManager.close();
		return socio;
	}

	@WebMethod
	public Socio registrarUser(String nom, String mail, String contra, String mPago, int paypalTarjeta)
			throws MetodoDePagoNoValidoException {
		dbManager.open();
		Socio socio = dbManager.registrarUser(nom, mail, contra, mPago, paypalTarjeta);
		dbManager.close();
		return socio;
	}

	@WebMethod
	public boolean doesSocioExist(String email) throws SocioAlreadyExistsException {
		dbManager.open();
		boolean existe = dbManager.doesSocioExist(email);
		dbManager.close();
		return existe;
	}

	@WebMethod
	public List<Sesion> consultarSesiones(Date fecha) {
		dbManager.open();
		List<Sesion> sesiones = dbManager.consultarSesiones(fecha);
		dbManager.close();
		return sesiones;

	}

	@WebMethod
	public Actividades añadirActividad(String nombre, int gExigencia, int precio)
			throws ActividadAlreadyExistsException {
		dbManager.open();
		Actividades actividad = dbManager.añadirActividad(nombre, gExigencia, precio);
		dbManager.close();
		return actividad;
	}

	@WebMethod
	public List<Actividades> getActividades() {
		dbManager.open();
		List<Actividades> actividades = dbManager.getActividades();
		dbManager.close();
		return actividades;
	};

	@WebMethod
	public List<Sala> getSalas() {
		dbManager.open();
		List<Sala> salas = dbManager.getSalas();
		dbManager.close();
		return salas;
	}

	@WebMethod
	public Actividades getActividad(String nomActividad) {
		dbManager.open();
		Actividades actividad = dbManager.getActividad(nomActividad);
		dbManager.close();
		return actividad;
	}

	@WebMethod
	public Sala getSala(String nomSala) {
		dbManager.open();
		Sala sala = dbManager.getSala(nomSala);
		dbManager.close();
		return sala;
	}

	@WebMethod
	public boolean resDisponible(Socio socio) {
		if (socio.getNumReservas() < socio.getNumMaxReserva()) {
			return true;
		}
		return false;
	}

	@WebMethod
	public boolean estaLlena(Sesion sesion) {
		dbManager.open();
		List<Reserva> reservas = dbManager.getReservasSesion(sesion);
		dbManager.close();
		if (reservas.size() < sesion.getSala().getAforoMax()) {
			return false;
		}
		return true;
	}

	@WebMethod
	public void addListaEspera(Socio socio, Sesion sesion) {
		dbManager.open();
		dbManager.addListaEspera(socio, sesion);
		dbManager.close();
	}

	@WebMethod
	public int reservarSesion(Socio socio, Sesion sesion) {
		dbManager.open();
		int id = dbManager.reservarSesion(socio, sesion);
		dbManager.close();
		return id;
	}

	@WebMethod
	public List<Reserva> visualizarReservas(Socio socio) {
		dbManager.open();
		List<Reserva> reservas = dbManager.visualizarReservas(socio);
		dbManager.close();
		return reservas;
	}

	@WebMethod
	public List<Factura> consFacturas(Socio socio) {
		dbManager.open();
		List<Factura> facturas = dbManager.consFacturas(socio);
		dbManager.close();
		return facturas;
	}

	@WebMethod
	public Reserva cancelarReserva(Reserva reserva) {
		dbManager.open();
		Reserva res = dbManager.cancelarReserva(reserva);
		dbManager.close();
		return res;
	}

	@WebMethod
	public void envFacturas() {
		dbManager.open();
		dbManager.envFacturas();
		dbManager.close();
	}

	@WebMethod
	public boolean cobrarFact(int codigo, String mPago) {
		dbManager.open();
		boolean pagado = dbManager.cobrarFact(codigo, mPago);
		dbManager.close();
		return pagado;
	}
}
