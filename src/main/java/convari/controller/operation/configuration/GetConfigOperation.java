package convari.controller.operation.configuration;

import italo.validate.RequestValidatorParam;

import java.util.List;

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
import convari.persistence.bean.UserConfigBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;



public class GetConfigOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam param = manager.createParam( "uid" );
		param.getValidators().add( manager.createNotNullValidator() );
		param.getValidators().add( manager.createLongValidator( 0, Long.MAX_VALUE ) );

		vparams.add( param );
	}

	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		
		String paramId = parameters.getRequest().getParameter( "uid" );
		int id = Integer.parseInt( paramId );
		
		try {
			UserConfigBean config = persistence.getUserManager().findUserConfig( id );			
			if( config == null ) {
				responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.USER_NOT_FOUND_BY_ID_ERROR, "uid" ); 
			} else {																
				String enderVID = controller.getIndexForVisibility( config.getEnderVisibility() );
				String telVID = controller.getIndexForVisibility( config.getTelVisibility() );
				String mailVID = controller.getIndexForVisibility( config.getMailVisibility() );

				Document doc = responseBuilder.getDoc();
				Element userNode = doc.createElement( "user" );
				Element userConfigNode = doc.createElement( "config" );
				
				Element enderVisibilityNode = doc.createElement( "ender-visibility" );
				Element telVisibilityNode = doc.createElement( "tel-visibility" );
				Element mailVisibilityNode = doc.createElement( "mail-visibility" );
												
				userConfigNode.appendChild( enderVisibilityNode );
				userConfigNode.appendChild( telVisibilityNode );
				userConfigNode.appendChild( mailVisibilityNode );
											
				userNode.appendChild( userConfigNode );
				
				responseBuilder.getBodyNode().appendChild( userNode );

				enderVisibilityNode.appendChild( responseBuilder.createTextNode( enderVID ) );
				telVisibilityNode.appendChild( responseBuilder.createTextNode( telVID ) );
				mailVisibilityNode.appendChild( responseBuilder.createTextNode( mailVID ) );				
			}
		} catch (PersistenceException e) {
			throw new OperationException( e );
		}
				
	}

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		controller.getValidationManager().validateAuthNeed( parameters );
	}		

}
