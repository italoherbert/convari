package convari.controller.operation.navigation;

import italo.validate.RequestValidatorParam;

import java.util.List;

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
import convari.persistence.bean.UserGeneralDataBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.weblogic.SessionUserBean;



public class GetNavigationUserInfoOperation implements Operation {

	public void initialize(OperationController controller, List<RequestValidatorParam> vparams) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam param = manager.createParam( "uid" );
		param.getValidators().add( manager.createNotNullValidator() );
		param.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );

		vparams.add( param );		
	}
	
	public void execute( OperationController controller, OperationParameters parameters )	throws OperationException {
		Persistence persistence = parameters.getPersistence();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
					
		String uidP = parameters.getRequest().getParameter( "uid" );
		int uid = Integer.parseInt( uidP );
		
		Document doc = responseBuilder.getDoc();
		Element userNode = doc.createElement( "user" );

		Element uIdNode = doc.createElement( "id" );
		Element uNameNode = doc.createElement( "name" );
		
		Element contactNode = doc.createElement( "contact" );		
		Attr isOwnerAttr = doc.createAttribute( "isOwner" ); 
						
		userNode.appendChild( uIdNode );
		userNode.appendChild( uNameNode );
		
		contactNode.setAttributeNode( isOwnerAttr );
		
		responseBuilder.getBodyNode().appendChild( userNode );
		responseBuilder.getBodyNode().appendChild( contactNode );
		
		SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
		int userId = sessionUser.getUID();
		try {
			UserGeneralDataBean personalInfo = persistence.getUserManager().findGeneralData( userId );
			String firstname = personalInfo.getName();
			String lastname = personalInfo.getLastname(); 
			String name = firstname+" "+lastname;
					
			uIdNode.appendChild( responseBuilder.createTextNode( String.valueOf( userId ) ) );
			uNameNode.appendChild( responseBuilder.createTextNode( name ) );
			
			if( uid == userId ) {
				isOwnerAttr.setNodeValue( "true" );
			} else {						
				UserGeneralDataBean contact =  parameters.getPersistence().getUserManager().findGeneralData( uid );
				if( contact == null ) {
					responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.USER_NOT_FOUND_BY_ID_ERROR, String.valueOf( uid ) );
				} else {
					String contactName = contact.getName()+" "+contact.getLastname();
					
					Element cIdNode = doc.createElement( "id" );
					Element cNameNode = doc.createElement( "name" );
	
					contactNode.appendChild( cIdNode );
					contactNode.appendChild( cNameNode );
					
					cIdNode.appendChild( responseBuilder.createTextNode( uidP ) );
					cNameNode.appendChild( responseBuilder.createTextNode( contactName ) );
				}								
				isOwnerAttr.setNodeValue( "false" );
			}			
		} catch (PersistenceException e) {
			throw new OperationException( e );
		}
		
	}
	
	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		controller.getValidationManager().validateAuthNeed( parameters );
	}

}
