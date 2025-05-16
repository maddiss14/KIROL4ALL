package exceptions;

public class SalaOcupadaException extends Exception {
	private static final long serialVersionUID = 1L;

	public SalaOcupadaException() {
		super();
	}

	/**
	 * This exception is triggered if la sala is ocupada
	 * 
	 * @param s String of the exception
	 */
	public SalaOcupadaException(String s) {
		super(s);
	}
}