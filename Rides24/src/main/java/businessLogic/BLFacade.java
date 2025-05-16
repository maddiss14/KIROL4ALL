package businessLogic;

import java.time.LocalTime;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

//import domain.Booking;
import domain.Sesion;
import domain.Socio;
import domain.Actividades;
import domain.Factura;
import domain.Reserva;
import domain.Sala;
import exceptions.SesionMustBeLaterThanTodayException;
import exceptions.SocioAlreadyExistsException;
import exceptions.ActividadAlreadyExistsException;
import exceptions.MetodoDePagoNoValidoException;
import exceptions.SalaOcupadaException;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Interface that specifies the business logic.
 */
@WebService
public interface BLFacade {
	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from    the origin location of a ride
	 * @param to      the destination location of a ride
	 * @param date    the date of the ride
	 * @param nPlaces available seats
	 * @param driver  to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws SesionMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException           if the same ride already exists
	 *                                             for the driver
	 */
	@WebMethod
	public Sesion planSes(Actividades actividad, Date fecha, String horaI, String horaF, Sala sala)
			throws SesionMustBeLaterThanTodayException, SalaOcupadaException;

	/**
	 * This method retrieves the rides from two locations on a given date
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	@WebMethod
	public List<Sesion> getSesiones(Date fecha);

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	@WebMethod
	public List<Date> getThisMonthDatesWithSesiones(Date date);

	/**
	 * This method calls the data access to initialize the database with some events
	 * and questions. It is invoked only when the option "initialize" is declared in
	 * the tag dataBaseOpenMode of resources/config.xml file
	 */
	@WebMethod
	public void initializeBD();

	@WebMethod
	public Socio getSocio(String nom, String contra);

	@WebMethod
	public Socio registrarUser(String nom, String mail, String contra, String mPago, int paypalTarjeta)
			throws MetodoDePagoNoValidoException;

	@WebMethod
	public boolean doesSocioExist(String mail) throws SocioAlreadyExistsException;

	@WebMethod
	public List<Sesion> consultarSesiones(Date fecha);

	@WebMethod
	public Actividades añadirActividad(String nombre, int gExigencia, int precio)
			throws ActividadAlreadyExistsException;

	@WebMethod
	public List<Actividades> getActividades();

	@WebMethod
	public List<Sala> getSalas();

	@WebMethod
	public Actividades getActividad(String nomActividad);

	@WebMethod
	public Sala getSala(String nomSala);

	@WebMethod
	public boolean resDisponible(Socio socio);

	@WebMethod
	public boolean estaLlena(Sesion sesion);

	@WebMethod
	public void addListaEspera(Socio socio, Sesion sesion);

	@WebMethod
	public int reservarSesion(Socio socio, Sesion sesion);

	@WebMethod
	public List<Reserva> visualizarReservas(Socio socio);

	@WebMethod
	public List<Factura> consFacturas(Socio socio);

	@WebMethod
	public Reserva cancelarReserva(Reserva res);

	@WebMethod
	public void envFacturas();

	@WebMethod
	public boolean cobrarFact(int codigo, String mPago);
	/**
	 * @WebMethod public Sesion solicitarReserva(Sesion rideToAdd, String
	 *            userEmail);
	 * 
	 * @WebMethod public ArrayList<String> visualizarSolicitudes(String
	 *            emailDriver);
	 * 
	 * @WebMethod public Reserva getDriver(String email, String password);
	 **/
}
