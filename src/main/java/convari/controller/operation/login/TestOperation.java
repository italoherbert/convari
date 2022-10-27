package convari.controller.operation.login;

import italo.validate.RequestValidatorParam;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import convari.controller.operation.Operation;
import convari.controller.operation.OperationController;
import convari.controller.operation.OperationException;
import convari.controller.operation.OperationParameters;
import convari.controller.operation.ValidationManager;
import convari.controller.operation.ValidationOpException;
import convari.weblogic.WebLogic;

public class TestOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam cmdP = manager.createParam( "cmd" );
		cmdP.getValidators().add( manager.createRequiredValidator() );
		cmdP.getValidators().add( manager.createKnownValidator( "connection", "session" ) ); 
		
		vparams.add( cmdP );
	}
	
	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		HttpServletRequest request = parameters.getRequest();
		HttpServletResponse response = parameters.getResponse();
		WebLogic webLogic = parameters.getWebLogic();
		
		String cmd = request.getParameter( "cmd" );
		try {
			if( cmd.equals( "connection" ) ) {
				response.getWriter().println( "true" );			
			} else if( cmd.equals( "session" ) ) {
				response.getWriter().println( webLogic.isUserLogged( request ) );			
			}				
		} catch (IOException e) {
			throw new OperationException( e );
		}
	}

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		
	}

}
