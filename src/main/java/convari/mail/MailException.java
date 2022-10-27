package convari.mail;

public class MailException extends Exception {

	private static final long serialVersionUID = 1L;

	public MailException() {}

	public MailException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public MailException(String msg) {
		super(msg);
	}

	public MailException(Throwable cause) {
		super(cause);
	}	

}