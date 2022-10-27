package convari.mail;

public interface MailController {

	public void sendMail( String to, String subject, String message ) throws MailException;

	public String getMailUsername();

	public void setMailUsername(String mailUsername);

	public String getMailPassword();

	public void setMailPassword(String mailPassword);

	public MailUtil getMailUtil();

	public MailSender getSender();
	
}
