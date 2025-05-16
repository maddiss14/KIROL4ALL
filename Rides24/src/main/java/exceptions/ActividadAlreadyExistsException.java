package exceptions;

public class ActividadAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 1L;

	public ActividadAlreadyExistsException() {
		super();
	}

	/**
	 * This exception is triggered if el método de pago no es válido
	 * 
	 * @param s String of the exception
	 */
	public ActividadAlreadyExistsException(String s) {
		super(s);
	}

}
