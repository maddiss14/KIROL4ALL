package exceptions;

public class MetodoDePagoNoValidoException extends Exception {
	private static final long serialVersionUID = 1L;

	public MetodoDePagoNoValidoException() {
		super();
	}

	/**
	 * This exception is triggered if el método de pago no es válido
	 * 
	 * @param s String of the exception
	 */
	public MetodoDePagoNoValidoException(String s) {
		super(s);
	}

}
