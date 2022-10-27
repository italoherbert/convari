package convari.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
	
	private Properties properties = new Properties();
	private Authenticator authenticator;			
	
	public MailSender(Properties properties, Authenticator authenticator) {
		super();
		this.properties = properties;
		this.authenticator = authenticator;
	}

	public void send( Mail mail ) throws MailSenderException {
		if( mail.getFrom() == null)
			throw new MailSenderException( "O destinatario e obrigatorio!" );		
		Session session = Session.getInstance( properties, authenticator );
		MimeMessage message = new MimeMessage( session );
		try {			
			message.setFrom( new InternetAddress( mail.getFrom() ) );
			if( mail.getRecipients() != null ) {
				InternetAddress[] tos = new InternetAddress[ mail.getRecipients().length ];
				for( int i = 0; i < mail.getRecipients().length; i++ )
					tos[ i ] = new InternetAddress( mail.getRecipients()[i] );
				message.addRecipients(Message.RecipientType.TO, tos );
			}
			message.setSubject( mail.getSubject(), "utf-8" );
			message.setContent( mail.getMessage(), "text/html; charset=UTF-8" );
			message.setSentDate( new Date() );
			Transport.send( message );
		} catch (MessagingException e) {
			throw new MailSenderException( e );
		}
				
	}
	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Authenticator getAuthenticator() {
		return authenticator;
	}

	public void setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
	}		
	
}
