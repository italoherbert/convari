package convari.logger;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import convari.response.ResponseMessage;
import convari.weblogic.SessionUserBean;


public interface Logger {
	
	public void setLoggerListener( LoggerListener listener );

	public List<Exception> getExceptions();

	public void addEventLog( HttpServletRequest request, ResponseMessage message );
	
	public void addLoginSuccessLog( SessionUserBean suser, String ip );
	
	public void setLogoutForLoginSuccessLog( SessionUserBean suser );
	
	public void addLoginFailLog( String username, String ip );
	
}
