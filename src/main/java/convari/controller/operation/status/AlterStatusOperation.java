package convari.controller.operation.status;

import italo.validate.RequestValidatorParam;

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
import convari.persistence.bean.LoginBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.weblogic.SessionUserBean;



public class AlterStatusOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam newStatusP = manager.createParam( "newstatus" );
		newStatusP.getValidators().add( manager.createNotNullValidator() );
		newStatusP.getValidators().add( manager.createIntValidator( 0, 2 ) );
		
		vparams.add( newStatusP );
	}
	
	public void execute( OperationController controller, OperationParameters parameters )  throws OperationException {
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		HttpServletRequest request = parameters.getRequest();
		Persistence persistence = parameters.getPersistence();
		
		String statusIndexParam = request.getParameter( "newstatus" );
		int statusIndex = Integer.parseInt( statusIndexParam );
		
		SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
		int userId = sessionUser.getUID();		
		
		String status = LoginBean.UNOCCUPIED_STATUS;
		switch( statusIndex ) {
			case 0: status = LoginBean.UNOCCUPIED_STATUS; break;
			case 1: status = LoginBean.OCCUPIED_STATUS;   break;
			case 2: status = LoginBean.INVISIBLE_STATUS;  break;
		}
		
		try {
			persistence.getLoginManager().setStatus( userId, status );
		} catch (PersistenceException e) {
			throw new OperationException( e );
		}
		
		responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.SET_STATUS_INFO );
										
	}
	
	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		
	}

}
