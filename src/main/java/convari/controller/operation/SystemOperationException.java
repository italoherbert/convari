package convari.controller.operation;

public class SystemOperationException extends OperationException {

	private static final long serialVersionUID = 1L;

	public SystemOperationException() {
		super();
	}

	public SystemOperationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public SystemOperationException(String msg) {
		super(msg);
	}

	public SystemOperationException(Throwable cause) {
		super(cause);
	}

}
