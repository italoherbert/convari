package convari.controller.operation.notification;

import italo.validate.RequestValidatorParam;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import convari.persistence.bean.NotificationBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.util.DateUtil;
import convari.weblogic.SessionUserBean;



public class GetNotificationsOperation implements Operation {

	public void initialize(OperationController controller,	List<RequestValidatorParam> vparams) throws OperationException {
		ValidationManager manager = controller.getValidationManager();
				
		RequestValidatorParam controlDateP = manager.createParam( "controldate" );
		controlDateP.getValidators().add( manager.createLongValidator( 0, Long.MAX_VALUE ) );	
		
		RequestValidatorParam langP = manager.createParam( "lang" );
		langP.getValidators().add( manager.createLengthValidator( 5 ) );

		vparams.add( controlDateP );
		vparams.add( langP );
	}

	public void execute(OperationController controller,	OperationParameters parameters) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		HttpServletRequest request = parameters.getRequest();
		DateUtil dateUtil = parameters.getDateUtil();
				
		SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
		int userId = sessionUser.getUID();
		
		String controlDateP = request.getParameter( "controldate" );
		String lang = request.getParameter( "lang" );
		
		long newControlDate = 0;

		Timestamp controlDate = null;
		if( controlDateP != null )
			controlDate = new Timestamp( Long.parseLong( controlDateP ) );
		
		try {			
			List<NotificationBean> notifications = persistence.getNotificationManager().listForUser( userId, controlDate );			
			
			Document doc = responseBuilder.getDoc();
			Element notificationsNode = doc.createElement( "notifications" );
			notificationsNode.setAttribute( "count", String.valueOf( notifications.size() ) );
			
			responseBuilder.getBodyNode().appendChild( notificationsNode );
			
			if( notifications.isEmpty() ) {
				responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.EMPTY_LIST_NOTIFICATION_INFO );
			} else {				
				for( NotificationBean notification:notifications ) {
					Element notificationNode = doc.createElement( "notification" );
					Element notificationIdNode = doc.createElement( "id" );
					
					Element notificationTextNode = doc.createElement( "text" );
					Element notificationDateNode = doc.createElement( "date" );
					
					notificationNode.appendChild( notificationIdNode );
					notificationNode.appendChild( notificationTextNode );
					notificationNode.appendChild( notificationDateNode );
					
					notificationsNode.appendChild( notificationNode );
										
					notificationIdNode.appendChild( responseBuilder.createTextNode( String.valueOf( notification.getId() ) ) ); 
					notificationTextNode.appendChild( responseBuilder.createTextNode( notification.getMessage() ) );
					notificationDateNode.appendChild( responseBuilder.createTextNode( dateUtil.formatToDateAndTime( notification.getDate(), lang ) ) );
					long time = dateUtil.getTime( notification.getDate() );
					if( time > newControlDate ) {
						newControlDate = time;
					}
				}
			}
			if( newControlDate == 0 )
				newControlDate = System.currentTimeMillis();
			notificationsNode.setAttribute( "controldate", String.valueOf( newControlDate ) ); 
		} catch( PersistenceException e ) {
			throw new OperationException( e );
		}
		
	}

	public void validate(OperationController controller, OperationParameters parameters) throws ValidationOpException {
		
	}

}
