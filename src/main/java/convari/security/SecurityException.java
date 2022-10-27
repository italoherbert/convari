package convari.security;

public class SecurityException extends Exception {

	private static final long serialVersionUID = 1L;

	public SecurityException() {}

	public SecurityException(String msg) {
		super(msg);
	}

	public SecurityException(Throwable cause) {
		super(cause);
	}

	public SecurityException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
