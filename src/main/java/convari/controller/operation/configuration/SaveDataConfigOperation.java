package convari.controller.operation.configuration;

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
import convari.persistence.bean.UserConfigBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.weblogic.SessionUserBean;



public class SaveDataConfigOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam endervParam = manager.createParam( "enderv" );
		endervParam.getValidators().add( manager.createRequiredValidator() );
		
		RequestValidatorParam telvParam = manager.createParam( "telv" );
		telvParam.getValidators().add( manager.createRequiredValidator() );

		RequestValidatorParam mailvParam = manager.createParam( "mailv" );
		mailvParam.getValidators().add( manager.createRequiredValidator() );
		
		vparams.add( endervParam );
		vparams.add( telvParam );
		vparams.add( mailvParam );
	}

	public void execute( OperationController controller, OperationParameters parameters )	throws OperationException {
		Persistence persistence = parameters.getPersistence();
		HttpServletRequest request = parameters.getRequest();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		
		String enderVID = request.getParameter( "enderv" );
		String telVID = request.getParameter( "telv" );		
		String mailVID = request.getParameter( "mailv" );

		try {
			String enderVisibility = controller.getVisibilityForIndex( enderVID );		
			String telVisibility = controller.getVisibilityForIndex( telVID );		
			String mailVisibility = controller.getVisibilityForIndex( mailVID );		

			UserConfigBean config = new UserConfigBean();
			config.setEnderVisibility( enderVisibility );
			config.setTelVisibility( telVisibility );
			config.setMailVisibility( mailVisibility );

			SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( request );			
			persistence.getUserManager().updateConfig( sessionUser.getUID(), config );
			
			responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.SET_CONFIG_DATAS_INFO );
		} catch( PersistenceException e ) {
			e.printStackTrace();
			throw new OperationException( e );
		}	
	}
	
	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {		
		controller.getValidationManager().validateAuthNeed( parameters );
	}

}