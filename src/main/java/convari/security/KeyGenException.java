package convari.security;

public class KeyGenException extends Exception {

	private static final long serialVersionUID = 1L;

	public KeyGenException() {}

	public KeyGenException(String msg) {
		super(msg);
	}

	public KeyGenException(Throwable cause) {
		super(cause);
	}

	public KeyGenException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
