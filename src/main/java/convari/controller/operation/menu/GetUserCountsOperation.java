package convari.controller.operation.menu;

import italo.validate.RequestValidatorParam;

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
import convari.persistence.bean.MenuBean;
import convari.response.ResponseBuilder;
import convari.weblogic.SessionUserBean;


public class GetUserCountsOperation implements Operation {

	public void initialize(OperationController controller, List<RequestValidatorParam> vparams) throws OperationException {
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam uidP = manager.createParam( "uid" );
		uidP.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );

		RequestValidatorParam includeTCountP = manager.createParam( "includetcount" );
		includeTCountP.getValidators().add( manager.createKnownValidator( "true", "false" ) ); 
		
		vparams.add( uidP );
		vparams.add( includeTCountP );
	}

	public void execute(OperationController controller, OperationParameters parameters) throws OperationException {
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		Persistence persistence = parameters.getPersistence();
		HttpServletRequest request = parameters.getRequest();
												
		String uidP = request.getParameter( "uid" );
		String includeTCountP = request.getParameter( "includetcount" );
		
		int uid = Integer.parseInt( uidP );
		boolean includeTCount = false;
		if( includeTCountP != null )
			includeTCount = includeTCountP.equals( "true" );
		
		SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
		if( sessionUser != null ) {						
			Document doc = responseBuilder.getDoc();
			
			Element notificationsCountNode = doc.createElement( "notifications-count" );
			Element invitationsCountNode = doc.createElement( "invitations-count" );
			Element topicsCountNode = doc.createElement( "topics-count" );
			Element isOwnerNode = doc.createElement( "is-owner" );
			
			responseBuilder.getBodyNode().appendChild( notificationsCountNode );
			responseBuilder.getBodyNode().appendChild( invitationsCountNode );
			responseBuilder.getBodyNode().appendChild( topicsCountNode );
			responseBuilder.getBodyNode().appendChild( isOwnerNode );

			String notificationsCount = "-1";
			String invitationsCount = "-1";
			String topicsCount = "-1";
			String isOwner = "false";
			try {					
				MenuBean counts = persistence.getMenuManager().findCounts( uid, includeTCount );		
				if( counts != null ) {
					if( sessionUser.getUID() == uid ) {
						notificationsCount = String.valueOf( counts.getNotificationsCount() );
						invitationsCount = String.valueOf( counts.getInvitationsCount() );
						topicsCount = String.valueOf( counts.getTopicsCount() );
						isOwner = "true";
					} else {
						topicsCount = String.valueOf( counts.getTopicsCount() );	
					}					
				}
			} catch (PersistenceException e) {
				throw new OperationException( e );
			}
						
			notificationsCountNode.appendChild( responseBuilder.createTextNode( notificationsCount ) );
			invitationsCountNode.appendChild( responseBuilder.createTextNode( invitationsCount ) );
			topicsCountNode.appendChild( responseBuilder.createTextNode( topicsCount ) );
			isOwnerNode.appendChild( responseBuilder.createTextNode( isOwner ) );
		}
				
	}

	public void validate(OperationController controller, OperationParameters parameters) throws ValidationOpException {
		
	}

}
