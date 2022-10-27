package convari.mail;

public class MailSenderException extends Exception {

	private static final long serialVersionUID = 1L;

	public MailSenderException() {}

	public MailSenderException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public MailSenderException(String msg) {
		super(msg);
	}

	public MailSenderException(Throwable cause) {
		super(cause);
	}	

}