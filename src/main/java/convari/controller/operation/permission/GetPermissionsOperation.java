package convari.controller.operation.permission;

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
import convari.persistence.bean.OperationBean;
import convari.persistence.bean.UserGeneralDataBean;
import convari.persistence.bean.VisibilityBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.weblogic.SessionUserBean;



public class GetPermissionsOperation implements Operation {

	public void initialize(OperationController controller, List<RequestValidatorParam> vparams) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam uidP = manager.createParam( "uid" );
		uidP.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );

		vparams.add( uidP );
	}
	
	public void execute( OperationController controller, OperationParameters parameters )	throws OperationException {
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		Persistence persistence = parameters.getPersistence();
		
		SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
								
		String uidP = parameters.getRequest().getParameter( "uid" );
		
		boolean redirect = false;
		int uid = -1;
		if( uidP != null ) {
			uid = Integer.parseInt( uidP );
		} else if( sessionUser != null ) {
			uid = sessionUser.getUID();
			redirect = true;			
		}
		
		Document doc = responseBuilder.getDoc();
		Element permissionsNode = doc.createElement( "permissions" );		
		Element visibilityNode = doc.createElement( "visibility" );
		Element userNode = doc.createElement( "user" );
		userNode.setAttribute( "redirect", String.valueOf( redirect ) );		
		
		Element userIdNode = doc.createElement( "id" );
		Element userNameNode = doc.createElement( "name" );
		
		userNode.appendChild( userIdNode );
		userNode.appendChild( userNameNode );
		
		responseBuilder.getBodyNode().appendChild( visibilityNode );
		responseBuilder.getBodyNode().appendChild( permissionsNode );		
		responseBuilder.getBodyNode().appendChild( userNode );
		
		if( redirect ) {
			userIdNode.appendChild( responseBuilder.createTextNode( String.valueOf( uid ) ) );
		} else {
			try {
				String visibility = VisibilityBean.PUBLIC_VISIBILITY;
				if( uid > -1 ) {
					UserGeneralDataBean personalInfo = persistence.getUserManager().findGeneralData( uid );
					if( personalInfo == null ) {
						responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.USER_NOT_FOUND_BY_ID_ERROR, String.valueOf( uid ) ); 				
					} else {
						userIdNode.appendChild( responseBuilder.createTextNode( String.valueOf( uid ) ) );
						userNameNode.appendChild( responseBuilder.createTextNode( personalInfo.getName() + " " + personalInfo.getLastname() ) );
						visibility = controller.getVisibility( parameters, uid );
					}
				}
				
				String vindex = controller.getIndexForVisibility( visibility );
				List<OperationBean> operations = persistence.getOperationManager().listByVisibility( VisibilityBean.PUBLIC_VISIBILITY );

				if( sessionUser != null ) {
					operations.addAll( sessionUser.getOperations() );
					userNode.setAttribute( "logged", String.valueOf( true ) );
				} else {
					userNode.setAttribute( "logged", String.valueOf( false ) );
				}
				
				for( OperationBean resource: operations ) {
					Element resourceNode = doc.createElement( "operation" );
					Attr nameAttr = doc.createAttribute( "name" );
					resourceNode.setAttributeNode( nameAttr );
					nameAttr.setNodeValue( resource.getName() );
					permissionsNode.appendChild( resourceNode );	
				}					
				visibilityNode.appendChild( responseBuilder.createTextNode( vindex ) );	
				
			} catch (PersistenceException e) {
				throw new OperationException( e );
			}	
		}								
						
	}
	
	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		
	}

}
