package convari.controller.operation.user;

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
import convari.persistence.bean.UserProfessionalDataBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.weblogic.SessionUserBean;



public class SaveProfessionalDataOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam occupationP = manager.createParam( "occupation" );
		occupationP.getValidators().add( manager.createLengthValidator( 100 ) );
		
		RequestValidatorParam academicP = manager.createParam( "academic" );
		academicP.getValidators().add( manager.createLengthValidator( 100 ) );
		
		vparams.add( occupationP );
		vparams.add( academicP );
	}
	
	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		HttpServletRequest request = parameters.getRequest();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		
		String occupation = request.getParameter( "occupation" );
		String academic = request.getParameter( "academic" );		
		
		try {
			if( occupation != null )
				occupation = URLDecoder.decode( occupation, "utf-8" );
			if( academic != null)
				academic = URLDecoder.decode( academic, "utf-8" );
			
			SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
			int userId = sessionUser.getUID();
			
			UserProfessionalDataBean professionalData = new UserProfessionalDataBean();
			professionalData.setOccupation( occupation ); 
			professionalData.setAcademic( academic ); 
			persistence.getUserManager().updateProfessionalInfo( userId, professionalData );
					
			responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.SET_PROFESSIONAL_DATA_INFO );
		} catch( PersistenceException e ) {
			throw new OperationException( e );
		} catch (UnsupportedEncodingException e) {
			throw new OperationException( e );
		}	
	}

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		
	}

}