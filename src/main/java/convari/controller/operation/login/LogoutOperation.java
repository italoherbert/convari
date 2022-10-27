package convari.controller.operation.login;

import italo.validate.RequestValidatorParam;

import java.util.List;

import convari.controller.operation.Operation;
import convari.controller.operation.OperationController;
import convari.controller.operation.OperationException;
import convari.controller.operation.OperationParameters;
import convari.controller.operation.ValidationOpException;
import convari.logger.Logger;
import convari.persistence.PersistenceException;
import convari.weblogic.SessionUserBean;



public class LogoutOperation implements Operation {

	public void initialize(OperationController controller,	List<RequestValidatorParam> vparams) throws OperationException {
		
	}

	public void execute( OperationController controller, OperationParameters parameters )	throws OperationException {
		Logger logger = parameters.getLogger();
		SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );		
		if( sessionUser != null ) {
			try {
				int userId = sessionUser.getUID();
				parameters.getPersistence().getLoginManager().setConnected( userId, false );
				logger.setLogoutForLoginSuccessLog( sessionUser );
			} catch (PersistenceException e) {
				throw new OperationException( e );
			}
		}
		parameters.getWebLogic().invalidateUserSession( parameters.getRequest() );
	}

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		
	}
	
}

