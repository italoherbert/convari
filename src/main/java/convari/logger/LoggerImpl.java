package convari.logger;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import convari.persistence.Persistence;
import convari.persistence.PersistenceException;
import convari.response.ResponseMessage;
import convari.weblogic.SessionUserBean;
import convari.weblogic.WebLogic;


public class LoggerImpl implements Logger {

	private LoggerDriver driver;
	private LoggerListener listener;
	private List<Exception> exceptions = new ArrayList<Exception>();	

	public LoggerImpl(LoggerDriver driver) {
		this( driver, null );
	}
	
	public LoggerImpl(LoggerDriver driver, LoggerListener listener) {
		super();
		this.driver = driver;
		this.listener = listener;
	}
	
	public void addEventLog( HttpServletRequest request, ResponseMessage message ) {
		WebLogic webLogic = driver.getWebLogic();
		Persistence persistence = driver.getPersistence();
		
		SessionUserBean sessionUser = webLogic.getSessionUser( request );
		int userId = ( sessionUser != null ? sessionUser.getUID() : 0 );
		
		try {
			if( message.getKey() != null ) {				
				persistence.getLogManager().addEventLog( userId, message.getType(), message.getKey() );
			} else {
				String text = message.getText();
				if( text.length() > 100 )
					text = text.substring( 0, 99 );
				persistence.getLogManager().addTextEventLog( userId, message.getType(), text );
			}					
		} catch (PersistenceException e) {
			this.loggerException( e );
		}
	}
	
	public void addLoginSuccessLog( SessionUserBean suser, String ip ) {
		Persistence persistence = driver.getPersistence();
		int userId = suser.getUID();
		try {
			int id = persistence.getLogManager().addLoginSuccessLog( userId, ip );
			if( id > -1 )
				suser.setLoginSuccessLogId( id );
		} catch (PersistenceException e) {
			this.loggerException( e );
		}
	}		
	
	public void setLogoutForLoginSuccessLog( SessionUserBean suser ) {
		Persistence persistence = driver.getPersistence();

		int id = suser.getLoginSuccessLogId();
		try {
			persistence.getLogManager().setLogoutForLoginSuccessLog( id );				
		} catch (PersistenceException e) {
			this.loggerException( e );
		}
	}
	
	public void addLoginFailLog( String username, String ip ) {
		Persistence persistence = driver.getPersistence();
		try {
			persistence.getLogManager().addLoginFailLog( username, ip );
		} catch (PersistenceException e) {
			this.loggerException( e );		
		}
	}		
	
	public void loggerException( Exception e ) {
		exceptions.add( e );
		if( listener != null )
			listener.loggerException( this, e );
	}

	public void setLoggerListener( LoggerListener listener ) {
		this.listener = listener;
	}

	public List<Exception> getExceptions() {
		return exceptions;
	}
	
}
