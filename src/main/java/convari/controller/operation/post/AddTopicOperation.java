package convari.controller.operation.post;

import italo.validate.RequestValidatorParam;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import convari.persistence.bean.TopicBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.weblogic.SessionUserBean;



public class AddTopicOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam descriptionP = manager.createParam( "description" );
		descriptionP.getValidators().add( manager.createRequiredValidator() );
		descriptionP.getValidators().add( manager.createLengthValidator( 3, 512 ) );

		vparams.add( descriptionP );
	}
	
	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		HttpServletRequest request = parameters.getRequest();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		
		String description = request.getParameter( "description" );
		String visibilityIndex = request.getParameter( "visibility" );
		String visibility = controller.getVisibilityForIndex( visibilityIndex );
		
		SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
		int userId = sessionUser.getUID();
		
		try {
			description = URLDecoder.decode( description, "utf-8" );							
			TopicBean topic = persistence.getPostManager().createTopic();
			topic.setDescription( description );
			topic.setVisibility( visibility );
			int tid = persistence.getPostManager().addTopic( topic, userId );
			
			Document doc = responseBuilder.getDoc();
			Element topicNode = doc.createElement( "topic" );
			topicNode.setAttribute( "id", String.valueOf( tid ) );
			responseBuilder.getBodyNode().appendChild( topicNode );
					
			responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.SAVE_TOPIC_INFO );
		} catch( PersistenceException e ) {
			throw new OperationException( e );
		} catch (UnsupportedEncodingException e) {
			throw new OperationException( e );
		}	
	}

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		controller.getValidationManager().validateAuthNeed( parameters );
	}

}