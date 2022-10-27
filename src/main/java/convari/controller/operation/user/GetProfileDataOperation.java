package convari.controller.operation.user;

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
import convari.persistence.bean.UserBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;



public class GetProfileDataOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam uidP = manager.createParam( "uid" );
		uidP.getValidators().add( manager.createNotNullValidator() );
		uidP.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );
		
		RequestValidatorParam langP = manager.createParam( "lang" );
		langP.getValidators().add( manager.createRequiredValidator() );
		
		vparams.add( uidP );
	}
	
	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		
		String paramId = parameters.getRequest().getParameter( "uid" );
		int uid = Integer.parseInt( paramId );
		
		try {
			UserBean user = persistence.getUserManager().find( uid );			
			if( user == null ) {
				responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.USER_NOT_FOUND_ERROR );
			} else {												
				boolean enderVisible = controller.authorize( parameters, uid, user.getConfig().getEnderVisibility() );
				boolean telVisible = controller.authorize( parameters, uid, user.getConfig().getTelVisibility() );
				boolean mailVisible = controller.authorize( parameters, uid, user.getConfig().getMailVisibility() );
				
				Document doc = responseBuilder.getDoc();
				Element userNode = doc.createElement( "user" );
				Element generalDataNode = doc.createElement( "general-data" );
				Element contactDataNode = doc.createElement( "contact-data" );
				Element professionalDataNode = doc.createElement( "professional-data" );
				
				Element imagePathNode = doc.createElement( "image" );
				Element nameNode = doc.createElement( "name" );
				Element lastNameNode = doc.createElement( "lastname" );
				Element sexNode = doc.createElement( "sex" );
				Element websiteNode = doc.createElement( "website" );
				
				Element enderNode = doc.createElement( "ender" );
				Element cityNode = doc.createElement( "city" );
				Element stateNode = doc.createElement( "state" );
				Element countryNode = doc.createElement( "country" );
				
				Element telNode = doc.createElement( "tel" );
				Element tel2Node = doc.createElement( "tel2" );
				Element mailNode = doc.createElement( "mail" );
				Element mail2Node = doc.createElement( "mail2" );				
				
				Element occupationNode = doc.createElement( "occupation" );
				Element academicNode = doc.createElement( "academic" );
				
				Attr enderVisibleAttr = doc.createAttribute( "visible" );
				Attr telVisibleAttr = doc.createAttribute( "visible" );
				Attr mailVisibleAttr = doc.createAttribute( "visible" );
				
				generalDataNode.appendChild( imagePathNode );
				generalDataNode.appendChild( nameNode );
				generalDataNode.appendChild( lastNameNode );
				generalDataNode.appendChild( sexNode );
				generalDataNode.appendChild( websiteNode );
				
				contactDataNode.appendChild( enderNode );
				contactDataNode.appendChild( cityNode );
				contactDataNode.appendChild( stateNode );
				contactDataNode.appendChild( countryNode );				
				
				contactDataNode.appendChild( telNode );
				contactDataNode.appendChild( tel2Node );
				contactDataNode.appendChild( mailNode );
				contactDataNode.appendChild( mail2Node );
				
				professionalDataNode.appendChild( occupationNode );
				professionalDataNode.appendChild( academicNode );
				
				enderNode.setAttributeNode( enderVisibleAttr );
				telNode.setAttributeNode( telVisibleAttr );
				mailNode.setAttributeNode( mailVisibleAttr );
				
				userNode.appendChild( generalDataNode );
				userNode.appendChild( contactDataNode );
				userNode.appendChild( professionalDataNode );
			
				responseBuilder.getBodyNode().appendChild( userNode );

				imagePathNode.appendChild( responseBuilder.createTextNode( user.getGeneralData().getImagePath() ) );
				nameNode.appendChild( responseBuilder.createTextNode( user.getGeneralData().getName() ) );
				lastNameNode.appendChild( responseBuilder.createTextNode( user.getGeneralData().getLastname() ) );
				sexNode.appendChild( responseBuilder.createTextNode( user.getGeneralData().getSex() ) );
				websiteNode.appendChild( responseBuilder.createTextNode( user.getGeneralData().getWebsite() ) );

				if( enderVisible ) {
					enderNode.appendChild( responseBuilder.createTextNode( user.getContactData().getEnder() ) );
					cityNode.appendChild( responseBuilder.createTextNode( user.getContactData().getCity() ) );
					stateNode.appendChild( responseBuilder.createTextNode( user.getContactData().getState() ) );
					countryNode.appendChild( responseBuilder.createTextNode( user.getContactData().getCountry() ) );					
				}
				
				if( telVisible ) {
					telNode.appendChild( responseBuilder.createTextNode( user.getContactData().getTel() ) );
					tel2Node.appendChild( responseBuilder.createTextNode( user.getContactData().getTel2() ) );					
				}
				
				if( mailVisible ) {
					mailNode.appendChild( responseBuilder.createTextNode( user.getContactData().getMail() ) );
					mail2Node.appendChild( responseBuilder.createTextNode( user.getContactData().getMail2() ) );
				}				
				
				occupationNode.appendChild( responseBuilder.createTextNode( user.getProfessionalData().getOccupation() ) );
				academicNode.appendChild( responseBuilder.createTextNode( user.getProfessionalData().getAcademic() ) );
												
				enderVisibleAttr.setNodeValue( String.valueOf( enderVisible ) );
				telVisibleAttr.setNodeValue( String.valueOf( telVisible ) );
				mailVisibleAttr.setNodeValue( String.valueOf( mailVisible ) );
			}
		} catch (PersistenceException e) {
			throw new OperationException( e );
		}
				
	}

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		
	}

}
