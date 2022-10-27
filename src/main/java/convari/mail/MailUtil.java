package convari.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailUtil {

	public Properties buildItaloHerbertProperties() {
		Properties properties = new Properties();
		properties.put( "mail.smtp.host", "mail.italoherbert.com.br" );	
		properties.put( "mail.smtp.auth", "true" );
		return properties;		
	}
	
	public Properties buildGMailProperties() {
		Properties properties = new Properties();
		properties.put( "mail.smtp.host", "smtp.gmail.com" );
		properties.put( "mail.smtp.port", "587" );		
		properties.put( "mail.smtp.auth", "true" );
		properties.put( "mail.smtp.socketFactory.port", "587" );
		properties.put( "mail.smtp.starttls.enable", "true" );
		return properties;				
	}
	
	public Authenticator buildAuthenticator( final String username, final String password ) {
		return new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication( username, password );
			}			
		};
	}
	
}
