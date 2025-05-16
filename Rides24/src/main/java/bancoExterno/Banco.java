package bancoExterno;

import exceptions.MetodoDePagoNoValidoException;

import java.util.HashMap;
import java.util.Map;

public class Banco {

	// Simulación de base de datos de saldos
	private static Map<String, Double> saldosTarjetas = new HashMap<>();
	private static Map<String, Double> saldosPaypal = new HashMap<>();

	// Inicializamos algunos saldos de ejemplo
	static {
		saldosTarjetas.put("1234567812345678", 100.0);
		saldosTarjetas.put("1111222233334444", 250.0);

		saldosPaypal.put("usuario@example.com", 50.0);
		saldosPaypal.put("cliente@correo.com", 300.0);
	}

	public static void validarMetodoPago(String idPago, int paypalTarjeta) throws MetodoDePagoNoValidoException {
		if (paypalTarjeta == 1) {
			if (!esTarjetaValida(idPago))
				throw new MetodoDePagoNoValidoException("Método de pago no válido: tarjeta inválida");
		} else if (paypalTarjeta == 0) {
			if (!esEmailValido(idPago))
				throw new MetodoDePagoNoValidoException("Método de pago no válido: email inválido");
		}
	}

	public static boolean pagoAceptado(double costoFactura, String idPago, int paypalTarjeta) {

		double saldoDisponible;

		if (paypalTarjeta == 1) {
			saldoDisponible = obtenerSaldoDeTarjeta(idPago);
		} else {
			saldoDisponible = obtenerSaldoDePaypal(idPago);
		}

		return costoFactura <= saldoDisponible;
	}

	private static boolean esEmailValido(String email) {
		return email.matches("^[^@]+@[^@]+\\.[a-zA-Z]{2,}$");
	}

	private static boolean esTarjetaValida(String tarjeta) {
		return tarjeta.matches("[0-9]{16}");
	}

	private static double obtenerSaldoDeTarjeta(String numeroTarjeta) {
		return saldosTarjetas.getOrDefault(numeroTarjeta, 0.0);
	}

	private static double obtenerSaldoDePaypal(String email) {
		return saldosPaypal.getOrDefault(email, 0.0);
	}
}
