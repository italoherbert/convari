package convari.persistence.dao;

public class DAOException extends Exception {

	private static final long serialVersionUID = 1L;

	public DAOException() {}

	public DAOException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DAOException(String msg) {
		super(msg);
	}

	public DAOException(Throwable cause) {
		super(cause);
	}	

}
