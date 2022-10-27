package italo.persistence.db;

public class DBManagerException extends Exception {

	private static final long serialVersionUID = 1L;

	public DBManagerException() {}

	public DBManagerException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DBManagerException(String msg) {
		super(msg);
	}

	public DBManagerException(Throwable cause) {
		super(cause);
	}	

}
