package convari.controller.operation.contact;

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
import convari.persistence.bean.InvitationBean;
import convari.persistence.bean.UserGeneralDataBean;
import convari.response.ResponseBuilder;
import convari.util.DateUtil;


public class GetInvitationNewsOperation implements Operation {

	public void initialize(OperationController controller, List<RequestValidatorParam> vparams) throws OperationException {
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam uidP = manager.createParam( "uid" );
		uidP.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );
		
		RequestValidatorParam contactControlDateP = manager.createParam( "ccontroldate" );
		contactControlDateP.getValidators().add( manager.createLongValidator( 0, Long.MAX_VALUE ) );	

		RequestValidatorParam invitationControlDateP = manager.createParam( "icontroldate" );
		invitationControlDateP.getValidators().add( manager.createLongValidator( 0, Long.MAX_VALUE ) );			
		
		RequestValidatorParam contactsOnlyP = manager.createParam( "contactsonly" );
		contactsOnlyP.getValidators().add( manager.createKnownValidator( "true", "false" ) ); 	
		
		RequestValidatorParam langP = manager.createParam( "lang" );
		langP.getValidators().add( manager.createLengthValidator( 5 ) );		
				
		vparams.add( uidP );
		vparams.add( contactControlDateP );
		vparams.add( invitationControlDateP );
		vparams.add( contactsOnlyP );
		vparams.add( langP );
	}

	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		HttpServletRequest request = parameters.getRequest();
		DateUtil dateUtil = parameters.getDateUtil();
				
		String uidP = request.getParameter( "uid" );
		String contactsOnlyP = request.getParameter( "contactsonly" );
		String contactControlDateP = request.getParameter( "ccontroldate" );
		String invitationControlDateP = request.getParameter( "icontroldate" );
		
		String lang = request.getParameter( "lang" );
		
		int uid = Integer.parseInt( uidP );
		long newContactControlDate = 0;		
		long newInvitationControlDate = 0;		

		boolean contactsOnly = false;
		if( contactsOnlyP != null )
			contactsOnly = contactsOnlyP.equals( "true" );

		Timestamp contactControlDate = null;
		Timestamp invitationControlDate = null;
		if( contactControlDateP != null )
			contactControlDate = new Timestamp( Long.parseLong( contactControlDateP ) );
		if( invitationControlDateP != null )
			invitationControlDate = new Timestamp( Long.parseLong( invitationControlDateP ) );
				
		try {
			Document doc = responseBuilder.getDoc();			
			Element invitationsNode = doc.createElement( "invitations" );
			responseBuilder.getBodyNode().appendChild( invitationsNode );
			
			List<InvitationBean> contacts = persistence.getContactManager().listForUser( uid, contactControlDate, invitationControlDate, contactsOnly );
			
			invitationsNode.setAttribute( "count", String.valueOf( contacts.size() ) );
			if( !contacts.isEmpty() ) {
				for( InvitationBean contact:contacts ) {	
					int id = contact.getId();
					int contactUserId = -1;
					String type = "unknown";
					if( contact.getFromUserId() == uid ) {						
						contactUserId = contact.getToUserId();
						type = "from";
					} else if( contact.getToUserId() == uid ) {
						contactUserId = contact.getFromUserId();
						type = "to";
					} else {
						throw new OperationException( "Inconsistência na listagem de convites.");
					}									
					String status = controller.getIndexForInvitationStatus( contact.getStatus() );
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
					statusNode.appendChild( responseBuilder.createTextNode( String.valueOf( status ) ) );
					long time;
					if( contact.getStatus().equals( InvitationBean.PENDING_INVITATION ) ) {
						time = dateUtil.getTime( contact.getSendDate() );						
					} else {
						time = dateUtil.getTime( contact.getResponseDate() );
					}
					
					if( contact.getStatus().equals( InvitationBean.ACCEPTED_INVITATION ) || contact.getStatus().equals( InvitationBean.REMOVED_INVITATION ) )
						if( time > newContactControlDate )
							newContactControlDate = time;
					if( !contact.getStatus().equals( InvitationBean.REMOVED_INVITATION ) ) 
						if( time > newInvitationControlDate )
							newInvitationControlDate = time;
				}
			}
			if( newContactControlDate == 0 )
				newContactControlDate = System.currentTimeMillis();
			if( newInvitationControlDate == 0 )
				newInvitationControlDate = System.currentTimeMillis();
			invitationsNode.setAttribute( "ccontroldate", String.valueOf( newContactControlDate ) );
			invitationsNode.setAttribute( "icontroldate", String.valueOf( newInvitationControlDate ) );
		} catch (PersistenceException e) {
			throw new OperationException( e );
		}
		
	}

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		
	}

}
