package convari.controller.operation.comment;

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
import convari.persistence.bean.CommentBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.weblogic.SessionUserBean;



public class AddCommentOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam messageP = manager.createParam( "message" );
		messageP.getValidators().add( manager.createRequiredValidator() );
		messageP.getValidators().add( manager.createLengthValidator( 3, 512 ) );
		
		vparams.add( messageP );
	}
	
	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		HttpServletRequest request = parameters.getRequest();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		
		String message = request.getParameter( "message" );
				
		try {
			if( message != null )
				message = URLDecoder.decode( message, "utf-8" );
			
			SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
			int userId = sessionUser.getUID();
			
			CommentBean comment = persistence.getCommentManager().create();
			comment.setMessage( message );
			comment.setUserId( userId );
			persistence.getCommentManager().insert( comment );
			
			responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.SAVE_COMMENT_INFO );
		} catch( PersistenceException e ) {
			throw new OperationException( e );
		} catch (UnsupportedEncodingException e) {
			throw new OperationException( e );
		}	
	}

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		
	}

}