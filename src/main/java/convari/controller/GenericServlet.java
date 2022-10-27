package convari.controller;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import convari.controller.operation.OperationController;
import convari.controller.operation.OperationParameters;
import convari.controller.operation.OperationParametersImpl;
import convari.logger.Logger;
import convari.mail.MailController;
import convari.messages.MessageManager;
import convari.persistence.Persistence;
import convari.response.ResponseBuilder;
import convari.response.ResponseBuilderException;
import convari.response.ResponseController;
import convari.security.Security;
import convari.upload.ImageUploader;
import convari.util.DateUtil;
import convari.weblogic.WebLogic;



public class GenericServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected int anonymousUserId;
	protected int systemUserId;
	protected Security security;
	protected Persistence persistence;
	protected WebLogic webLogic;
	protected Logger logger;
	protected MailController mailController;
	protected DateUtil dateUtil;
	protected String privateKey;
	protected String rootURL;
	protected long loginAbsentDelay;
	protected MessageManager messageManager;
	protected ResponseController responseController;
	protected ImageUploader imageUploader;
	protected OperationController operationController;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);		
		this.systemUserId = Integer.parseInt( String.valueOf( config.getServletContext().getAttribute( "system-user-id" ) ) );
		this.anonymousUserId = Integer.parseInt( String.valueOf( config.getServletContext().getAttribute( "anonymous-user-id" ) ) );
		this.security = (Security)config.getServletContext().getAttribute( "security" );
		this.persistence = (Persistence)config.getServletContext().getAttribute( "persistence" );
		this.webLogic = (WebLogic)config.getServletContext().getAttribute( "web-logic" );
		this.logger = (Logger)config.getServletContext().getAttribute( "logger" );
		this.mailController = (MailController)config.getServletContext().getAttribute( "mail-controller" );
		this.dateUtil = (DateUtil)config.getServletContext().getAttribute( "date-util" );
		this.operationController = (OperationController)config.getServletContext().getAttribute( "operation-controller" );
		this.responseController = (ResponseController)config.getServletContext().getAttribute( "response-controller" );
		this.messageManager = (MessageManager)config.getServletContext().getAttribute( "message-manager" );
		this.loginAbsentDelay = Long.parseLong( String.valueOf( config.getServletContext().getAttribute( "login-absent-delay" ) ) );
		this.privateKey = String.valueOf( config.getServletContext().getAttribute( "private-key" ) );
		this.rootURL = String.valueOf( config.getServletContext().getAttribute( "root-url" ) );
		this.imageUploader =(ImageUploader) config.getServletContext().getAttribute( "image-uploader" );
	}

	protected ResponseBuilder createPlainResponseBuilder( HttpServletRequest request ) throws IOException {
		return responseController.createPlainResponseBuilder( request, true );
	}

	protected ResponseBuilder createHtmlResponseBuilder( HttpServletRequest request, HttpServletResponse response ) throws IOException { 
		return responseController.createHtmlResponseBuilder( request, response.getWriter(), true );
	}
	
	protected ResponseBuilder createXmlResponseBuilder( HttpServletRequest request ) throws ResponseBuilderException {		
		return responseController.createXmlResponseBuilder( request, true );
	}
	
	protected OperationParameters buildOperationParameters( HttpServletRequest request, HttpServletResponse response, ResponseBuilder responseBuilder ) {
		OperationParameters params = new OperationParametersImpl( this, request, response, responseBuilder );
		
		params.setSystemUserId( systemUserId );
		params.setAnonymousUserId( anonymousUserId );
		params.setSecurity( security );
		params.setPersistence( persistence );
		params.setWebLogic( webLogic );
		params.setLogger( logger );		
		params.setMailController( mailController );
		params.setDateUtil( dateUtil );
		params.setPrivateKey( privateKey );
		params.setRootURL( rootURL );
		params.setLoginAbsentDelay( loginAbsentDelay );
		params.setMessageManager( messageManager );
		params.setImageUploader( imageUploader );
					
		String uidParam = request.getParameter( "uid" );
		if( uidParam != null ) {
			try {
				params.setUID( Integer.parseInt( uidParam ) );
			} catch( NumberFormatException e ) {
				
			}
		}
		
		return params;
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}
			
}
