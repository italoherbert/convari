package convari.controller.operation.notification;

import italo.validate.RequestValidatorParam;

import java.util.List;

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
import convari.weblogic.SessionUserBean;



public class RemoveNotificationOperation implements Operation {

	public void initialize(OperationController controller,	List<RequestValidatorParam> vparams) throws OperationException {
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam nidP = manager.createParam( "nid" );
		nidP.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );
		
		vparams.add( nidP );
	}

	public void execute(OperationController controller,	OperationParameters parameters) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		
		SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
		int userId = sessionUser.getUID();		
		
		String nidP = parameters.getRequest().getParameter( "nid" );
		int nid = Integer.parseInt( nidP );
		try {			
			persistence.getNotificationManager().delete( userId, nid );
			responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.REMOVE_NOTIFICATION_INFO );
		} catch( PersistenceException e ) {
			throw new OperationException( e );
		}		
	}

	public void validate(OperationController controller, OperationParameters parameters) throws ValidationOpException {
		
	}

}

