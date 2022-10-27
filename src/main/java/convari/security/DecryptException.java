package convari.security;

public class DecryptException extends Exception {

	private static final long serialVersionUID = 1L;

	public DecryptException() {}

	public DecryptException(String msg) {
		super(msg);
	}

	public DecryptException(Throwable cause) {
		super(cause);
	}

	public DecryptException(String msg, Throwable cause) {
		super(msg, cause);
	}

}