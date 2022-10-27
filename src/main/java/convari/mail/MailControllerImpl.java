package convari.mail;


public class MailControllerImpl implements MailController {
	
	private MailUtil mailUtil = new MailUtil();
	private MailSender sender;
	private String mailUsername;
	private String mailPassword;
	
	public MailControllerImpl( String mailUsername, String mailPassword ) {
		this.mailUsername = mailUsername;
		this.mailPassword = mailPassword;
		this.sender = new MailSender( 
				mailUtil.buildGMailProperties(), 
				mailUtil.buildAuthenticator( mailUsername, mailPassword ) );
	}		
	
	public void sendMail( String to, String subject, String message ) throws MailException {
		try {
			sender.send( new Mail( mailUsername, to, subject, message ) );
		} catch ( MailSenderException e ) {
			throw new MailException( e );
		}
	}

	public String getMailUsername() {
		return mailUsername;
	}

	public void setMailUsername(String mailUsername) {
		this.mailUsername = mailUsername;
	}

	public String getMailPassword() {
		return mailPassword;
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}

	public MailUtil getMailUtil() {
		return mailUtil;
	}

	public MailSender getSender() {
		return sender;
	}
				
}
