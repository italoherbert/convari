package convari.controller.operation.status;

import italo.validate.RequestValidatorParam;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import convari.controller.operation.Operation;
import convari.controller.operation.OperationController;
import convari.controller.operation.OperationException;
import convari.controller.operation.OperationParameters;
import convari.controller.operation.ValidationManager;
import convari.controller.operation.ValidationOpException;
import convari.persistence.Persistence;
import convari.persistence.PersistenceException;
import convari.persistence.bean.LoginBean;
import convari.response.ResponseBuilder;
import convari.weblogic.SessionUserBean;


public class GetStatusOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam uidP = manager.createParam( "uid" );
		uidP.getValidators().add( manager.createNotNullValidator() );
		uidP.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );

		RequestValidatorParam lastControlDateP = manager.createParam( "lastcontroldate" );
		lastControlDateP.getValidators().add( manager.createNotNullValidator() );
		lastControlDateP.getValidators().add( manager.createLongValidator( 0, Long.MAX_VALUE ) );
				
		vparams.add( uidP );
		vparams.add( lastControlDateP );
	}
	
	public void execute( OperationController controller, OperationParameters parameters )	throws OperationException {
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		HttpServletRequest request = parameters.getRequest();
		Persistence persistence = parameters.getPersistence();
		long loginAbsentDelay = parameters.getLoginAbsentDelay();
		
		String uidP = request.getParameter( "uid" );		
		int uid = Integer.parseInt( uidP );
		
		String lastControlDateP = request.getParameter( "lastcontroldate" );
		long delay = System.currentTimeMillis() - Long.parseLong( lastControlDateP );
		
		SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
		int userId = ( sessionUser != null ? sessionUser.getUID() : -1 );
		LoginBean login = null;
		try {					
			login = persistence.getLoginManager().find( uid );
		} catch (PersistenceException e) {
			throw new OperationException( e );
		}							
		
		Document doc = responseBuilder.getDoc();
		Element userNode = doc.createElement( "user" );
		Attr foundAttr = doc.createAttribute( "found" );
		userNode.setAttributeNode( foundAttr );					
		if( login != null ) {
			Element loginNode = doc.createElement( "login" );	
			Element loginStatusNode = doc.createElement( "status" );
			
			loginNode.appendChild( loginStatusNode );
			userNode.appendChild( loginNode );

			foundAttr.setNodeValue( "true" );
			
			int statusIndex = -1;
						
			if( userId == uid ) {
				if( delay > loginAbsentDelay ) {
					if( !sessionUser.isAbsent() ) {
						sessionUser.setAbsent( true );
						try {
							persistence.getLoginManager().setAbsent( userId, true );
						} catch (PersistenceException e) {
							throw new OperationException( e );
						}
					}
					statusIndex = 3;
				} else {
					if( sessionUser.isAbsent() ) {
						sessionUser.setAbsent( false );
						try {
							persistence.getLoginManager().setAbsent( userId, false );
						} catch (PersistenceException e) {
							throw new OperationException( e );
						}
					}
					String status = login.getStatus();
					if( status.equals( LoginBean.UNOCCUPIED_STATUS ) ) {
						statusIndex = 0;
					} else if( status.equals( LoginBean.OCCUPIED_STATUS ) ) {
						statusIndex = 1;
					} else if( status.equals( LoginBean.INVISIBLE_STATUS ) ) {
						statusIndex = 2;
					}
				}				
			} else {
				if( login.isConnected() ) {
					if( login.isAbsent() ) {
						statusIndex = 3;
					} else {
						String status = login.getStatus();
						if( status.equals( LoginBean.UNOCCUPIED_STATUS ) ) {
							statusIndex = 0;
						} else if( status.equals( LoginBean.OCCUPIED_STATUS ) ) {
							statusIndex = 1;
						} else if( status.equals( LoginBean.INVISIBLE_STATUS ) ) {
							statusIndex = 4;
						}
					}
				} else {
					statusIndex = 4;
				}
			}					
			loginStatusNode.appendChild( responseBuilder.createTextNode( String.valueOf( statusIndex ) ) );			

		} else {
			foundAttr.setNodeValue( "false" );
		}
		responseBuilder.getBodyNode().appendChild( userNode );
										
	}
	
	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		
	}

}
