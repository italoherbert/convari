package convari.persistence;

public class PersistenceException extends Exception {

	private static final long serialVersionUID = 1L;

	public PersistenceException() {}

	public PersistenceException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public PersistenceException(String msg) {
		super(msg);
	}

	public PersistenceException(Throwable cause) {
		super(cause);
	}	

}
