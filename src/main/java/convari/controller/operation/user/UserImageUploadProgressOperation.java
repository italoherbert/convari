package convari.controller.operation.user;

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
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.upload.ImageUploadProcess;
import convari.weblogic.SessionUserBean;




public class UserImageUploadProgressOperation implements Operation {

	public void initialize(OperationController controller, List<RequestValidatorParam> vparams) throws OperationException {
		ValidationManager manager = controller.getValidationManager();		
		
		RequestValidatorParam opP = manager.createParam( "op" );
		opP.getValidators().add( manager.createRequiredValidator() );
		
		vparams.add( opP );
	}

	public void execute(OperationController controller, OperationParameters parameters) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		HttpServletRequest request = parameters.getRequest();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		
		SessionUserBean suser = parameters.getWebLogic().getSessionUser( request );
		int userId = suser.getUID();
		ImageUploadProcess uploadProcess = suser.getImageUploadProcess(); 
		
		String op = request.getParameter( "op" );
		if( op.equals( "cancel" ) )
			if( uploadProcess.getStatus() != ImageUploadProcess.Status.FINISHING )
				uploadProcess.setStatus( ImageUploadProcess.Status.CANCELING );		
		
		int progress = uploadProcess.getProgress();
		String status = "ready";
		String imagePath = null;
		if( uploadProcess.getStatus() == ImageUploadProcess.Status.PROCESSING ) {
			status = "processing";
		} else if( uploadProcess.getStatus() == ImageUploadProcess.Status.CANCELING ) {
			status = "canceling";
		} else if( uploadProcess.getStatus() == ImageUploadProcess.Status.FINISHING ) {
			status = "finishing";
		} else {
			if( uploadProcess.getStatus() == ImageUploadProcess.Status.CANCELED || 
					uploadProcess.getStatus() == ImageUploadProcess.Status.FINISHED ) {
				if( uploadProcess.getStatus() == ImageUploadProcess.Status.CANCELED ) {		
					status = "canceled";
					responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.SAVE_USER_IMAGE_CANCELED_INFO );
				} else if( uploadProcess.getStatus() == ImageUploadProcess.Status.FINISHED ) {
					status = "finished";
					try {
						imagePath = persistence.getUserManager().findImagepath( userId );
					} catch (PersistenceException e) {
						throw new OperationException( e );
					}
					responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.SAVE_USER_IMAGE_FINISHED_INFO );
				}
				uploadProcess.processMessageKeys( responseBuilder );
			} else if( uploadProcess.getStatus() == ImageUploadProcess.Status.FAILED ) {
				status = "failed";
				uploadProcess.processMessageKeys( responseBuilder );
			}
			uploadProcess.setStatus( ImageUploadProcess.Status.READY );
		}
		
		Document doc = responseBuilder.getDoc();
		Element progressNode = doc.createElement( "progress" );
		progressNode.setAttribute( "status", status );
		progressNode.setAttribute( "value", String.valueOf( progress ) );
		if( imagePath != null )
			progressNode.setAttribute( "imagepath", imagePath );
		responseBuilder.getBodyNode().appendChild( progressNode );
		
	}

	public void validate(OperationController controller, OperationParameters parameters) throws ValidationOpException {
		controller.getValidationManager().validateAuthNeed( parameters );		
	}

}
