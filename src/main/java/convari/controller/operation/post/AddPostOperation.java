package convari.controller.operation.post;

import italo.validate.RequestValidatorParam;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import convari.controller.operation.Operation;
import convari.controller.operation.OperationController;
import convari.controller.operation.OperationException;
import convari.controller.operation.OperationParameters;
import convari.controller.operation.ValidationManager;
import convari.controller.operation.ValidationOpException;
import convari.persistence.Persistence;
import convari.persistence.PersistenceException;
import convari.persistence.bean.PostBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.weblogic.SessionUserBean;



public class AddPostOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam tidP = manager.createParam( "tid" );
		tidP.getValidators().add( manager.createLongValidator( 0, Long.MAX_VALUE ) );
		
		RequestValidatorParam messageP = manager.createParam( "message" );
		messageP.getValidators().add( manager.createRequiredValidator() );
		messageP.getValidators().add( manager.createLengthValidator( 3, 512 ) );
		
		vparams.add( tidP );
		vparams.add( messageP );
	}
	
	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		HttpServletRequest request = parameters.getRequest();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		
		String message = request.getParameter( "message" );
		String tidP = request.getParameter( "tid" );
		
		try {
			if( message != null )
				message = URLDecoder.decode( message, "utf-8" );
			int tid = Integer.parseInt( tidP );
			
			SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
			int userId = sessionUser.getUID();
			
			PostBean post = persistence.getPostManager().createPost();
			post.setMessage( message );
			persistence.getPostManager().addPost( post, userId, tid );
					
			responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.SAVE_POST_INFO );
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