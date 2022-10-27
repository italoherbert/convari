package convari.controller.operation;

public class OperationException extends Exception {

	private static final long serialVersionUID = 1L;

	public OperationException() {
		super();
	}

	public OperationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public OperationException(String msg) {
		super(msg);
	}

	public OperationException(Throwable cause) {
		super(cause);
	}

}
