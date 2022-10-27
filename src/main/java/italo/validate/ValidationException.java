package italo.validate;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	public ValidationException() {
		super();
	}

	public ValidationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ValidationException(String msg) {
		super(msg);
	}

	public ValidationException(Throwable cause) {
		super(cause);
	}

}