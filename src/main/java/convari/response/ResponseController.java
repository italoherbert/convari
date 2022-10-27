package convari.response;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;


public interface ResponseController {

	public ResponseBuilder createPlainResponseBuilder( HttpServletRequest request, boolean addMsgs );

	public ResponseBuilder createXmlResponseBuilder( HttpServletRequest request, boolean addMsgs ) throws ResponseBuilderException;
	
	public ResponseBuilder createHtmlResponseBuilder( HttpServletRequest request, PrintWriter out, boolean addMsgs  );
	
	public void copyMessagesToResponseBuilder( ResponseBuilder responseBuilder );
		
	public void addSystemInfoMessage( String message, String... params );
	
	public void addSystemErrorMessage( String message, String... params );
		
	public void setMessageListener( ResponseMessageListener listener );
	
	public int getSystemErrorMSGCount();
	
	public int getSystemInfoMSGCount();
}
