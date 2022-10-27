package convari.controller.operation.contact;

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
import convari.persistence.bean.InvitationBean;
import convari.response.ResponseBuilder;
import convari.weblogic.SessionUserBean;


public class GetInvitationStatusOperation implements Operation {
	
	public void initialize(OperationController controller, List<RequestValidatorParam> vparams) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam uidP = manager.createParam( "uid" );
		uidP.getValidators().add( manager.createNotNullValidator() );
		uidP.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );

		vparams.add( uidP );
	}

	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		HttpServletRequest request = parameters.getRequest();
		Persistence persistence = parameters.getPersistence();
		
		String uidParam = request.getParameter( "uid" );		
		int uid = Integer.parseInt( uidParam );
		
		SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( request );			
		int userId = sessionUser.getUID();
		
		int invitationStatus = 0;
		String of = null;
		try {
			InvitationBean contact = persistence.getContactManager().find( userId, uid );
			if( contact != null ) {
				if( userId == contact.getFromUserId() ) {
					of = "from";
				} else if( userId == contact.getToUserId() ) {
					of = "to";
				}
				
				String status = contact.getStatus();
				if( status.equals( InvitationBean.PENDING_INVITATION ) ) {
					invitationStatus = 1;
				} else if( status.equals( InvitationBean.ACCEPTED_INVITATION ) ) {
					invitationStatus = 2;
				} else if( status.equals( InvitationBean.REFUSED_INVITATION ) ) {
					invitationStatus = 3;
				}				
			}			
		} catch (PersistenceException e) {
			throw new OperationException( e );
		}
		
		Document doc = responseBuilder.getDoc();
		Element invitationNode = doc.createElement( "invitation" );
		Attr ofNode = doc.createAttribute( "of" );
		Attr statusNode = doc.createAttribute( "status" );		

		invitationNode.setAttributeNode( ofNode );
		invitationNode.setAttributeNode( statusNode );
		responseBuilder.getBodyNode().appendChild( invitationNode );			
					
		ofNode.appendChild( responseBuilder.createTextNode( of ) );
		statusNode.appendChild( responseBuilder.createTextNode( String.valueOf( invitationStatus ) ) );
	}
	
	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		controller.getValidationManager().validateAuthNeed( parameters );
	}
	
}