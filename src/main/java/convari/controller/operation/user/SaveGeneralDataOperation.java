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
import convari.persistence.bean.UserGeneralDataBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.weblogic.SessionUserBean;



public class SaveGeneralDataOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
						
		RequestValidatorParam nameP = manager.createParam( "name" );
		nameP.getValidators().add( manager.createRequiredValidator() );
		nameP.getValidators().add( manager.createLengthValidator( 100 ) );
		
		RequestValidatorParam lastnameP = manager.createParam( "lastname" );
		lastnameP.getValidators().add( manager.createRequiredValidator() );
		lastnameP.getValidators().add( manager.createLengthValidator( 100 ) );	

		RequestValidatorParam sexP = manager.createParam( "sex" );
		sexP.getValidators().add( manager.createRequiredValidator() );
		sexP.getValidators().add( manager.createLengthValidator( 1, 1 ) );	

		RequestValidatorParam websiteP = manager.createParam( "website" );
		websiteP.getValidators().add( manager.createLengthValidator( 100 ) );	
		
		vparams.add( nameP );
		vparams.add( lastnameP );
		vparams.add( sexP );
		vparams.add( websiteP );
	}
	
	public void execute( OperationController controller, OperationParameters parameters )	throws OperationException {
		Persistence persistence = parameters.getPersistence();
		HttpServletRequest request = parameters.getRequest();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		
		String firstname = request.getParameter( "name" );
		String lastname = request.getParameter( "lastname" );		
		String sex = request.getParameter( "sex" );
		String website = request.getParameter( "website" );

		try {
			if( firstname != null )
				firstname = URLDecoder.decode( firstname, "utf-8" );
			if( lastname != null)
				lastname = URLDecoder.decode( lastname, "utf-8" );
			if( website != null )
				website = URLDecoder.decode( website, "utf-8" );
			
			SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
			int userId = sessionUser.getUID();
			
			UserGeneralDataBean generalDate = new UserGeneralDataBean();
			generalDate.setName( firstname ); 
			generalDate.setLastname( lastname );
			generalDate.setSex( sex );
			generalDate.setWebsite( website );
			persistence.getUserManager().updateGeneralData( userId, generalDate );			
			
			responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.SET_PERSONAL_DATA_INFO );
		} catch( PersistenceException e ) {
			throw new OperationException( e );
		} catch (UnsupportedEncodingException e) {
			throw new OperationException( e );
		}	
	}
	
	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
			
	}

}