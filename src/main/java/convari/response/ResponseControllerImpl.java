package convari.response;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


public class ResponseControllerImpl implements ResponseController {

	private List<ResponseMessage> infoMessages = new ArrayList<ResponseMessage>();
	private List<ResponseMessage> errorMessages = new ArrayList<ResponseMessage>();
	private ResponseMessageListener messageListener = null;
	
	public ResponseControllerImpl() {
		this( null );
	}
	
	public ResponseControllerImpl( ResponseMessageListener messageListener ) {
		this.messageListener = messageListener;
	}
	
	public ResponseBuilder createXmlResponseBuilder( HttpServletRequest request, boolean addMsgs ) throws ResponseBuilderException {
		ResponseBuilder responseBuilder = new ResponseBuilder( request, "xml" );
		responseBuilder.setMessageListener( messageListener );
		responseBuilder.initializeXMLResponse();
		if( addMsgs )
			this.copyMessagesToResponseBuilder( responseBuilder );
		return responseBuilder;	
	}
	
	public ResponseBuilder createPlainResponseBuilder( HttpServletRequest request, boolean addMsgs ) {
		ResponseBuilder responseBuilder = new ResponseBuilder( request );
		responseBuilder.setMessageListener( messageListener );
		if( addMsgs )
			this.copyMessagesToResponseBuilder( responseBuilder );
		return responseBuilder;
	}
	
	public ResponseBuilder createHtmlResponseBuilder( HttpServletRequest request, PrintWriter out, boolean addMsgs ) {
		ResponseBuilder responseBuilder = new ResponseBuilder( request, "html", out );
		responseBuilder.setMessageListener( messageListener );
		if( addMsgs )
			this.copyMessagesToResponseBuilder( responseBuilder );
		return responseBuilder;
	}
	
	public void copyMessagesToResponseBuilder( ResponseBuilder responseBuilder ) {
		for( ResponseMessage message: errorMessages )
			responseBuilder.processErrorMSG( message.getKey(), message.getText(), message.getParams() );
		for( ResponseMessage message: infoMessages )
			responseBuilder.processInfoMSG( message.getKey(), message.getText(), message.getParams() );			
	}
		
	public void setMessageListener(ResponseMessageListener messageListener) {
		this.messageListener = messageListener;
	}

	public void addSystemInfoMessage( String key, String... params ) {
		ResponseMessage message = new ResponseMessage( ResponseMessage.INFO_TYPE, null, key, params );
		infoMessages.add( message);
	}
	
	public void addSystemErrorMessage( String key, String... params ) {
		ResponseMessage message = new ResponseMessage( ResponseMessage.ERROR_TYPE, null, key, params );
		errorMessages.add( message );
	}
	
	public void addTextInfoMessage( String text ) {
		ResponseMessage message = new ResponseMessage( ResponseMessage.INFO_TYPE, text, null );
		infoMessages.add( message ); 
	}
	
	public void addTextErrorMessage( String text ) {
		ResponseMessage message = new ResponseMessage( ResponseMessage.ERROR_TYPE, text, null );
		errorMessages.add( message );
	}
			
	public int getSystemErrorMSGCount() {
		return errorMessages.size();
	}
	
	public int getSystemInfoMSGCount() {
		return infoMessages.size();
	}
	
}
