package convari.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class Test {
	
	public static void main(String[] args) {	
		Properties properties = new Properties();
		properties.put( "mail.smtp.host", "mail.italoherbert.com.br" );	
		properties.put( "mail.smtp.auth", "true" );
		Authenticator authenticator = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication( "suporte@italoherbert.com.br", "17.ito_40" );
			}			
		};	
		MailSender mailSender = new MailSender( properties, authenticator );

		
		String from = "suporte@italoherbert.com.br";
		String to = "italoherbert@gmail.com";
		String subject = "Mensagem adicionada sobre (italoherbert.com.br)";
		String mailBody = "<b>Nome: </b><i>Italo Herbert</i><br />";
		mailBody += "<b>E-Mail: </b> <i><a href='mailto:italoherbert@gmail.com'>italoherbert.com.br</a></i><br />";
		mailBody += "<br /><b>Mensagem: </b><i>Teste!</i><br /><br />";
		try {
			mailSender.send( new Mail( from, to, subject, mailBody ) );
			System.out.println("MENSAGEM ENVIADA!");
		} catch (MailSenderException e) {
			e.printStackTrace();
		}
	}
	
}
