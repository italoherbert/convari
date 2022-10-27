package convari.controller.operation.contact;

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
import convari.persistence.bean.InvitationBean;
import convari.persistence.bean.UserGeneralDataBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.util.DateUtil;
import convari.weblogic.SessionUserBean;



public class GetPendingInvitationsOperation implements Operation {

	public void initialize(OperationController controller, List<RequestValidatorParam> vparams) throws OperationException {
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam langP = manager.createParam( "lang" );
		langP.getValidators().add( manager.createLengthValidator( 5 ) );
		
		vparams.add( langP );
	}

	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		DateUtil dateUtil = parameters.getDateUtil();
		HttpServletRequest request = parameters.getRequest();
				
		SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
		int userId = sessionUser.getUID();		
		long newControlDate = 0;

		String lang = request.getParameter( "lang" );
		
		try {
			Document doc = responseBuilder.getDoc();			
			Element invitationsNode = doc.createElement( "invitations" );
			responseBuilder.getBodyNode().appendChild( invitationsNode );
			
			List<InvitationBean> contacts = persistence.getContactManager().listForUserByStatus( userId, InvitationBean.PENDING_INVITATION );
				
			invitationsNode.setAttribute( "count", String.valueOf( contacts.size() ) );							
			if( contacts.isEmpty() ) {
				responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.EMPTY_LIST_INVITE_INFO );
			} else {	
				for( InvitationBean contact:contacts ) {	
					int id = contact.getId();
					int contactUserId = -1;
					String type = "unknown";
					if( contact.getFromUserId() == userId ) {						
						contactUserId = contact.getToUserId();
						type = "from";
					} else if( contact.getToUserId() == userId ) {
						contactUserId = contact.getFromUserId();
						type = "to";
					} else {
						throw new OperationException( "Inconsist�ncia na listagem de convites.");
					}									
					UserGeneralDataBean pi = persistence.getUserManager().findGeneralData( contactUserId );
					
					Element invitationNode = doc.createElement( "invitation" );	
					Element invitationIdNode = doc.createElement( "id" );
					Element contactNode = doc.createElement( "contact" );
					
					Element contactIdNode = doc.createElement( "id" );
					Element imagePathNode = doc.createElement( "imagepath" ); 
					Element nameNode = doc.createElement( "name" );					
					Element lastnameNode = doc.createElement( "lastname" );
					Element statusNode = doc.createElement( "status" );
					Element sendDateNode = doc.createElement( "senddate" );
					Element responseDateNode = doc.createElement( "responsedate" );
					
					contactNode.appendChild( contactIdNode );
					contactNode.appendChild( imagePathNode );
					contactNode.appendChild( nameNode );
					contactNode.appendChild( lastnameNode ); 
					contactNode.appendChild( statusNode );
					contactNode.appendChild( sendDateNode );
					contactNode.appendChild( responseDateNode );
					
					invitationNode.appendChild( invitationIdNode );
					invitationNode.appendChild( contactNode );
					
					invitationsNode.appendChild( invitationNode );		
					
					invitationNode.setAttribute( "type", type );
					invitationIdNode.appendChild( responseBuilder.createTextNode( String.valueOf( id ) ) );
					
					contactIdNode.appendChild( responseBuilder.createTextNode( String.valueOf( contactUserId ) ) );
					imagePathNode.appendChild( responseBuilder.createTextNode( pi.getImagePath() ) );
					nameNode.appendChild( responseBuilder.createTextNode( pi.getName() ) );
					lastnameNode.appendChild( responseBuilder.createTextNode( pi.getLastname() ) );
					sendDateNode.appendChild( responseBuilder.createTextNode( dateUtil.formatToDateAndTime( contact.getSendDate(), lang ) ) );
					responseDateNode.appendChild( responseBuilder.createTextNode( dateUtil.formatToDateAndTime( contact.getResponseDate(), lang ) ) );					
					
					long time;
					if( contact.getStatus().equals( InvitationBean.PENDING_INVITATION ) ) {
						time = dateUtil.getTime( contact.getSendDate() );						
					} else {
						time = dateUtil.getTime( contact.getResponseDate() );
					}
					if( time > newControlDate )
						newControlDate = time;	
				}
			}
			if( newControlDate == 0 )
				newControlDate = System.currentTimeMillis();
			invitationsNode.setAttribute( "controldate", String.valueOf( newControlDate ) );

		} catch (PersistenceException e) {
			throw new OperationException( e );
		}
		
	}

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		
	}

}
