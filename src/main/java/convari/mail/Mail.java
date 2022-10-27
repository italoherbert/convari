package convari.mail;

public class Mail {

	private String from;
	private String subject;
	private String message;		
	private String[] recipients = {};
	
	private String smtpServer;
	
	public Mail() {}

	public Mail(String from, String recipient, String subject, String message) {
		this( from, new String[]{ recipient }, subject, message, null );
	}

	public Mail( String from, String[] recipients, String subject, String message, String smtpServer ) {
		this.smtpServer = smtpServer;
		this.from = from;
		this.subject = subject;
		this.message = message;
		this.recipients = recipients;
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getRecipients() {
		return recipients;
	}

	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}
		
}
