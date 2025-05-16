package dataAccess;

import java.io.File;

import java.net.NoRouteToHostException;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import bancoExterno.Banco;
import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Actividades;
import domain.Factura;
import domain.Reserva;
import domain.Sala;
import domain.Sesion;
import domain.Socio;
import exceptions.ActividadAlreadyExistsException;
import exceptions.MetodoDePagoNoValidoException;
import exceptions.SalaOcupadaException;
import exceptions.SesionMustBeLaterThanTodayException;
import exceptions.SocioAlreadyExistsException;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess {
	private EntityManager db;
	private EntityManagerFactory emf;

	ConfigXML c = ConfigXML.getInstance();

	public DataAccess() {
		if (c.isDatabaseInitialized()) {
			String fileName = c.getDbFilename();

			File fileToDelete = new File(fileName);
			if (fileToDelete.delete()) {
				File fileToDeleteTemp = new File(fileName + "$");
				fileToDeleteTemp.delete();

				System.out.println("File deleted");
			} else {
				System.out.println("Operation failed");
			}
		}
		open();
		if (c.isDatabaseInitialized())
			initializeDB();

		System.out.println("DataAccess created => isDatabaseLocal: " + c.isDatabaseLocal() + " isDatabaseInitialized: "
				+ c.isDatabaseInitialized());

		close();

	}

	public DataAccess(EntityManager db) {
		this.db = db;
	}

	/**
	 * This is the data access method that initializes the database with some events
	 * and questions. This method is invoked by the business logic (constructor of
	 * BLFacadeImplementation) when the option "initialize" is declared in the tag
	 * dataBaseOpenMode of resources/config.xml file
	 */
	public void initializeDB() {

		db.getTransaction().begin();

		try {

			Calendar today = Calendar.getInstance();

			int day = today.get(Calendar.DAY_OF_MONTH);
			int month = today.get(Calendar.MONTH);
			int year = today.get(Calendar.YEAR);
			if (month == 12) {
				month = 1;
				year += 1;
			}
			// Create drivers
			Socio socio = new Socio("Aitor Fernandez", "socio1@gmail.com", "Aitor11", "1234567812345678");

			Sala sala = new Sala("Sala1", 10);
			Actividades actividad = new Actividades("Zumba", 5, 2);
			Sesion sesion = new Sesion(actividad, UtilDate.newDate(year, month, 15), "10:00", "11:30", sala);
			Sesion sesion1 = new Sesion(actividad, UtilDate.newDate(year, month, 22), "10:00", "11:30", sala);
			Sesion sesion2 = new Sesion(actividad, UtilDate.newDate(year, month, 22), "11:00", "12:30", sala);

			Reserva reserva = new Reserva(20, UtilDate.newDate(year, month, 15), socio, sesion);
			Reserva reserva1 = new Reserva(0, UtilDate.newDate(year, month, day), socio, sesion1);
			Reserva reserva2 = new Reserva(50, UtilDate.newDate(year, month, day), socio, sesion2);

			List<Reserva> reservas = new Vector<Reserva>();
			reservas.add(reserva2);
			reservas.add(reserva1);
			reservas.add(reserva);
			Factura factura = new Factura(50, UtilDate.newDate(year, month, day), reservas, socio);
			socio.getFacturas().add(factura);
			socio.getReservas().add(reserva);
			socio.getReservas().add(reserva2);
			socio.getReservas().add(reserva1);

			sala.getSesion().add(sesion2);
			sala.getSesion().add(sesion1);
			sala.getSesion().add(sesion);

			db.persist(sala);
			db.persist(actividad);
			db.persist(socio);

			db.getTransaction().commit();

			System.out.println("Db initialized");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from        the origin location of a ride
	 * @param to          the destination location of a ride
	 * @param date        the date of the ride
	 * @param nPlaces     available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws SesionMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException           if the same ride already exists
	 *                                             for the driver
	 */
	public Sesion planSes(Actividades actividad, Date fecha, String horaI, String horaF, Sala sala)
			throws SesionMustBeLaterThanTodayException, SalaOcupadaException {
		System.out.println(">> DataAccess: planSes=> actividad= " + actividad + " fecha= " + fecha + " horaI=" + horaI
				+ " horaF " + horaF + "sala=> " + sala);
		try {
			if (new Date().compareTo(fecha) > 0) {
				throw new SesionMustBeLaterThanTodayException(
						ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
			}
			db.getTransaction().begin();

			if (sala.isSalaOcupada(fecha, horaI, horaF)) {
				db.getTransaction().commit();
				throw new SalaOcupadaException(
						ResourceBundle.getBundle("Etiquetas").getString("DataAccess.SalaOcupadaException"));
			}

			Sesion sesion = new Sesion(actividad, fecha, horaI, horaF, sala);
			sala.getSesion().add(sesion);
			db.persist(sesion);
			db.getTransaction().commit();

			return sesion;
		} catch (NullPointerException e) {
			db.getTransaction().commit();
			return null;
		}

	}

	public Actividades añadirActividad(String nombre, int gExigencia, int precio)
			throws ActividadAlreadyExistsException {
		TypedQuery<Actividades> query = db.createQuery("SELECT a FROM Actividades a WHERE a.nombre =?1",
				Actividades.class);
		query.setParameter("1", nombre);
		List<Actividades> existentes = query.getResultList();
		if (!existentes.isEmpty()) {
			throw new ActividadAlreadyExistsException();
		}
		db.getTransaction().begin();
		Actividades actividad = new Actividades(nombre, gExigencia, precio);
		db.persist(actividad);
		db.getTransaction().commit();
		return actividad;
	}

	/**
	 * This method retrieves the rides from two locations on a given date
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	public List<Sesion> getSesiones(Date fecha) {
		System.out.println(">> DataAccess: fecha " + fecha);

		List<Sesion> ses = new ArrayList<>();
		TypedQuery<Sesion> query = db.createQuery("SELECT s FROM Sesion s WHERE s.fecha=?1", Sesion.class);
		query.setParameter(1, fecha);
		List<Sesion> sesiones = query.getResultList();
		for (Sesion sesion : sesiones) {
			ses.add(sesion);
		}
		return ses;
	}

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithSesiones(Date date) {
		System.out.println(">> DataAccess: getEventsMonth");
		List<Date> res = new ArrayList<>();

		Date firstDayMonthDate = UtilDate.firstDayMonth(date);
		Date lastDayMonthDate = UtilDate.lastDayMonth(date);

		TypedQuery<Date> query = db.createQuery("SELECT DISTINCT r.fecha FROM Sesion r WHERE r.fecha BETWEEN ?3 and ?4",
				Date.class);

		query.setParameter(3, firstDayMonthDate);
		query.setParameter(4, lastDayMonthDate);
		List<Date> dates = query.getResultList();
		for (Date d : dates) {
			res.add(d);
		}
		return res;
	}

	public void open() {

		String fileName = c.getDbFilename();
		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<>();
			properties.put("javax.persistence.jdbc.user", c.getUser());
			properties.put("javax.persistence.jdbc.password", c.getPassword());

			emf = Persistence.createEntityManagerFactory(
					"objectdb://" + c.getDatabaseNode() + ":" + c.getDatabasePort() + "/" + fileName, properties);
			db = emf.createEntityManager();
		}
		System.out.println("DataAccess opened => isDatabaseLocal: " + c.isDatabaseLocal());

	}

	public void close() {
		db.close();
		System.out.println("DataAcess closed");
	}

	public Socio getSocio(String mail, String password) {
		Socio socio;
		TypedQuery<Socio> query = db.createQuery("SELECT s FROM Socio s WHERE s.mail =? 1", Socio.class);
		query.setParameter(1, mail);
		if (!query.getResultList().isEmpty()) {
			socio = query.getSingleResult();
			if (socio.getContra().equals(password)) {
				return socio;
			}
		}
		return null;
	}

	public Socio registrarUser(String nom, String mail, String contra, String mPago, int paypalTarjeta)
			throws MetodoDePagoNoValidoException {
		Socio socio = new Socio(nom, mail, contra, mPago);

		db.getTransaction().begin();
		Banco.validarMetodoPago(mPago, paypalTarjeta);
		db.persist(socio);
		db.getTransaction().commit();
		return socio;
	}

	public boolean doesSocioExist(String mail) throws SocioAlreadyExistsException {
		boolean existe = true;

		db.getTransaction().begin();
		TypedQuery<Socio> query = db.createQuery("SELECT s FROM Socio s WHERE s.mail =? 1", Socio.class);
		query.setParameter(1, mail);

		if (query.getResultList().isEmpty())
			existe = false;

		else {
			throw new SocioAlreadyExistsException(
					ResourceBundle.getBundle("Etiquetas").getString("DataAccess.SocioAlreadyExists"));
		}

		return existe;
	}

	public List<Sesion> consultarSesiones(Date fecha) {
		db.getTransaction().begin();
		TypedQuery<Sesion> query = db.createQuery("SELECT s FROM Sesion s WHERE s.fecha =? 1", Sesion.class);
		query.setParameter(1, fecha);
		return query.getResultList();
	}

	public List<Sala> getSalas() {
		db.getTransaction().begin();
		TypedQuery<Sala> query = db.createQuery("SELECT DISTINCT s FROM Sala s", Sala.class);
		List<Sala> salas = query.getResultList();
		return salas;
	}

	public List<Actividades> getActividades() {
		TypedQuery<Actividades> query = db.createQuery("SELECT DISTINCT a FROM Actividades a ORDER BY a.nombre",
				Actividades.class);
		List<Actividades> actividades = query.getResultList();
		return actividades;

	}

	public Actividades getActividad(String nomActividad) {
		TypedQuery<Actividades> query = db.createQuery("SELECT DISTINCT a FROM Actividades a WHERE a.nombre =? 1",
				Actividades.class);
		query.setParameter(1, nomActividad);
		Actividades actividad = query.getSingleResult();
		return actividad;
	}

	public Sala getSala(String nomSala) {
		TypedQuery<Sala> query = db.createQuery("SELECT DISTINCT s FROM Sala s WHERE s.nombre =? 1", Sala.class);
		query.setParameter(1, nomSala);
		Sala sala = query.getSingleResult();
		return sala;
	}

	public List<Reserva> getReservasSesion(Sesion sesion) {
		TypedQuery<Reserva> query = db.createQuery("SELECT DISTINCT r FROM Reserva r WHERE r.sesion =? 1",
				Reserva.class);
		query.setParameter(1, sesion);
		List<Reserva> reservas = query.getResultList();
		return reservas;
	}

	public List<Reserva> visualizarReservas(Socio socio) {
		TypedQuery<Reserva> query = db.createQuery("SELECT DISTINCT r FROM Reserva r WHERE r.socio =? 1",
				Reserva.class);
		query.setParameter(1, socio);
		List<Reserva> reservas = query.getResultList();
		return reservas;

	}

	public void addListaEspera(Socio socio, Sesion sesion) {
		TypedQuery<Socio> querySocio = db.createQuery("SELECT DISTINCT s FROM Socio s WHERE s =? 1", Socio.class);
		querySocio.setParameter(1, socio);
		TypedQuery<Sesion> querySesion = db.createQuery("SELECT DISTINCT s FROM Sesion s WHERE s =? 1", Sesion.class);
		querySesion.setParameter(1, sesion);
		Socio soc = querySocio.getSingleResult();
		Sesion ses = querySesion.getSingleResult();

		db.getTransaction().begin();
		Reserva reserva = new Reserva(0, sesion.getFecha(), soc, ses);
		if (soc.getNumReservas() > 5) {
			reserva.setCoste(ses.getActividad().getPrecio());
		}
		ses.addListaEspera(reserva);
		db.getTransaction().commit();
	}

	public int reservarSesion(Socio socio, Sesion sesion) {
		TypedQuery<Socio> querySocio = db.createQuery("SELECT DISTINCT s FROM Socio s WHERE s =? 1", Socio.class);
		querySocio.setParameter(1, socio);
		TypedQuery<Sesion> querySesion = db.createQuery("SELECT DISTINCT s FROM Sesion s WHERE s =? 1", Sesion.class);
		querySesion.setParameter(1, sesion);
		Socio soc = querySocio.getSingleResult();
		Sesion ses = querySesion.getSingleResult();

		db.getTransaction().begin();
		Reserva reserva = new Reserva(0, sesion.getFecha(), soc, ses);
		if (soc.getNumReservas() > 5) {
			reserva.setCoste(ses.getActividad().getPrecio());
		}
		soc.setNumReservas(soc.getNumReservas() + 1);
		db.persist(reserva);
		db.getTransaction().commit();
		return reserva.getIdReserva();
	}

	public List<Factura> consFacturas(Socio socio) {
		TypedQuery<Factura> query = db.createQuery("SELECT DISTINCT f FROM Factura f WHERE f.socio =? 1",
				Factura.class);
		query.setParameter(1, socio);
		return query.getResultList();
	}

	public Reserva cancelarReserva(Reserva res) {
		TypedQuery<Reserva> query = db.createQuery("SELECT DISTINCT r FROM Reserva r WHERE r =? 1", Reserva.class);
		query.setParameter(1, res);
		Reserva reserva = query.getSingleResult();
		db.getTransaction().begin();
		db.remove(reserva);
		Sesion se = reserva.getSesion();
		if (!se.getListaEspera().isEmpty()) {
			Reserva nReserva = se.getListaEspera().getFirst();
			se.getListaEspera().removeFirst();
			db.persist(nReserva);
		}
		db.getTransaction().commit();
		return reserva;
	}

	public void envFacturas() {

		db.getTransaction().begin();
		TypedQuery<Socio> query = db.createQuery("SELECT DISTINCT s FROM Socio s", Socio.class);
		List<Socio> socios = query.getResultList();

		for (Socio socio : socios) {
			List<Reserva> reservas = socio.getReservas();

			int costeTotal = 0;
			for (Reserva reserva : reservas) {
				costeTotal += reserva.getCoste();
			}

			if (costeTotal > 0) {
				Date fechaFactura = new Date();
				Factura factura = new Factura(costeTotal, fechaFactura, reservas, socio);
				db.persist(factura);
			}
		}

		db.getTransaction().commit();
	}

	public boolean cobrarFact(int codigo, String mPago) {
		TypedQuery<Factura> query = db.createQuery("SELECT DISTINCT f FROM Factura f WHERE f.codigo = ?1",
				Factura.class);
		query.setParameter(1, codigo);
		Factura factura = query.getSingleResult();

		int tipoPago = detectarTipoPago(mPago); // 0 = PayPal, 1 = Tarjeta
		boolean pagado = false;

		db.getTransaction().begin();
		pagado = Banco.pagoAceptado(factura.getCoste(), mPago, tipoPago);
		if (pagado) {
			db.remove(factura);
		}
		db.getTransaction().commit();

		return pagado;
	}

	/**
	 * Este método devuelve el tipo de metodo de pago
	 * 
	 * @param mPago
	 * @return 0 si es paypal 1 si es tarjeta
	 */
	private int detectarTipoPago(String mPago) {
		if (mPago.matches("[0-9]{16}")) {
			return 1; // Tarjeta
		} else
			return 0; // PayPal
	}
}
