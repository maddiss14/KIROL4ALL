package exceptions;

public class SesionMustBeLaterThanTodayException extends Exception {
	private static final long serialVersionUID = 1L;

	public SesionMustBeLaterThanTodayException() {
		super();
	}

	/**
	 * This exception is triggered if the event has already finished
	 * 
	 * @param s String of the exception
	 */
	public SesionMustBeLaterThanTodayException(String s) {
		super(s);
	}
}