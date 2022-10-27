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



public class GetContactsOperation implements Operation {

	public void initialize(OperationController controller, List<RequestValidatorParam> vparams) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam uidP = manager.createParam( "uid" );
		uidP.getValidators().add( manager.createNotNullValidator() );
		uidP.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );
		
		vparams.add( uidP );
	}

	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		Persistence persistence = parameters.getPersistence();
		HttpServletRequest request = parameters.getRequest();
		DateUtil dateUtil = parameters.getDateUtil();
		
		String uidP = request.getParameter( "uid" );		
		int uid = Integer.parseInt( uidP );
		long newControlDate = 0;
		
		Document doc = responseBuilder.getDoc();
		Element contactsNode = doc.createElement( "contacts" );
		
		responseBuilder.getBodyNode().appendChild( contactsNode );			

		try {
			List<InvitationBean> contacts = persistence.getContactManager().listForUserByStatus( uid, InvitationBean.ACCEPTED_INVITATION );
						
			if( contacts.isEmpty() ) {
				responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.EMPTY_LIST_CONTACT_INFO );
			} else {
				for( InvitationBean contact:contacts ) {
					int contactId = -1;
					if( uid == contact.getFromUserId() ) {
						contactId = contact.getToUserId();
					} else if( uid == contact.getToUserId() ) {
						contactId = contact.getFromUserId();
					} else {
						throw new OperationException( "Inconsist�ncia na listagem de contatos." );
					}
					
					UserGeneralDataBean pi = persistence.getUserManager().findGeneralData( contactId );					
					String status = controller.getIndexForInvitationStatus( contact.getStatus() );
					
					Element contactNode = doc.createElement( "contact" );
					contactNode.setAttribute( "status", status );
					
					Element idNode = doc.createElement( "id" );
					Element firstnameNode = doc.createElement( "name" );
					Element lastnameNode = doc.createElement( "lastname" );
					Element imagepathNode = doc.createElement( "imagepath" );
					
					contactNode.appendChild( idNode );
					contactNode.appendChild( firstnameNode );
					contactNode.appendChild( lastnameNode );
					contactNode.appendChild( imagepathNode );
					contactsNode.appendChild( contactNode );

					idNode.appendChild( responseBuilder.createTextNode( String.valueOf( contactId ) ) );
					firstnameNode.appendChild( responseBuilder.createTextNode( pi.getName() ) );
					lastnameNode.appendChild( responseBuilder.createTextNode( pi.getLastname() ) );
					imagepathNode.appendChild( responseBuilder.createTextNode( pi.getImagePath() ) );
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
			contactsNode.setAttribute( "controldate", String.valueOf( newControlDate ) );
		} catch (PersistenceException e) {
			throw new OperationException( e );
		}		
	}
	
	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		controller.getValidationManager().validateAuthNeed( parameters );
	}
	
}