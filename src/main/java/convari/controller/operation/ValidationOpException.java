package convari.controller.operation;

public class ValidationOpException extends SystemOperationException {

	private static final long serialVersionUID = 1L;

	public ValidationOpException() {
		super();
	}

	public ValidationOpException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ValidationOpException(String msg) {
		super(msg);
	}

	public ValidationOpException(Throwable cause) {
		super(cause);
	}

}