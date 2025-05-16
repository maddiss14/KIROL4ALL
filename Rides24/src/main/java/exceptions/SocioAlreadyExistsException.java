package exceptions;

public class SocioAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 1L;

	public SocioAlreadyExistsException() {
		super();
	}

	/**
	 * This exception is triggered if the event has already finished
	 * 
	 * @param s String of the exception
	 */
	public SocioAlreadyExistsException(String s) {
		super(s);
	}
}