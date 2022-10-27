package convari.security;

public class EncryptException extends Exception {

	private static final long serialVersionUID = 1L;

	public EncryptException() {}

	public EncryptException(String msg) {
		super(msg);
	}

	public EncryptException(Throwable cause) {
		super(cause);
	}

	public EncryptException(String msg, Throwable cause) {
		super(msg, cause);
	}

}