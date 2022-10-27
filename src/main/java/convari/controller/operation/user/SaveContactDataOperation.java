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
import convari.persistence.bean.UserContactDataBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.weblogic.SessionUserBean;



public class SaveContactDataOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();						
		
		RequestValidatorParam enderP = manager.createParam( "ender" );
		enderP.getValidators().add( manager.createLengthValidator( 100 ) );
		
		RequestValidatorParam cityP = manager.createParam( "city" );
		cityP.getValidators().add( manager.createLengthValidator( 100 ) );
		
		RequestValidatorParam stateP = manager.createParam( "state" );
		stateP.getValidators().add( manager.createLengthValidator( 100 ) );
		
		RequestValidatorParam countryP = manager.createParam( "country" );
		countryP.getValidators().add( manager.createLengthValidator( 100 ) );
		
		
		RequestValidatorParam telP = manager.createParam( "tel" );
		telP.getValidators().add( manager.createLengthValidator( 100 ) );
		
		RequestValidatorParam tel2P = manager.createParam( "tel2" );
		tel2P.getValidators().add( manager.createLengthValidator( 100 ) );
		
		RequestValidatorParam mailP = manager.createParam( "mail" );
		mailP.getValidators().add( manager.createRequiredValidator() );
		mailP.getValidators().add( manager.createLengthValidator( 100 ) );
		mailP.getValidators().add( manager.createMailValidator() );
		
		RequestValidatorParam mail2P = manager.createParam( "mail2" );
		mail2P.getValidators().add( manager.createLengthValidator( 100 ) );
		mail2P.getValidators().add( manager.createMailValidator() );
		
		vparams.add( enderP );
		vparams.add( cityP );
		vparams.add( stateP );
		vparams.add( countryP );
		vparams.add( telP );
		vparams.add( tel2P );
		vparams.add( mailP );
		vparams.add( mail2P );
	}
	
	public void execute( OperationController controller, OperationParameters parameters )	throws OperationException {
		Persistence persistence = parameters.getPersistence();
		HttpServletRequest request = parameters.getRequest();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		
		String ender = request.getParameter( "ender" );
		String city = request.getParameter( "city" );
		String state = request.getParameter( "state" );
		String country = request.getParameter( "country" );
		
		String tel = request.getParameter( "tel" );
		String tel2 = request.getParameter( "tel2" );
		String mail = request.getParameter( "mail" );
		String mail2 = request.getParameter( "mail2" );

		try {
			if( ender != null )
				ender = URLDecoder.decode( ender, "utf-8" );			
			
			SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
			int userId = sessionUser.getUID();			
			
			UserContactDataBean contactData = new UserContactDataBean();
			contactData.setEnder( ender );
			contactData.setCity( city );
			contactData.setState( state );
			contactData.setCountry( country );
			
			contactData.setTel( tel );
			contactData.setTel2( tel2 );
			
			contactData.setMail( mail );
			contactData.setMail2( mail2 );		
			persistence.getUserManager().updateContactData( userId, contactData );
			
			responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.SET_PERSONAL_DATA_INFO );
		} catch( PersistenceException e ) {
			e.printStackTrace();
			throw new OperationException( e );
		} catch (UnsupportedEncodingException e) {
			throw new OperationException( e );
		}	
	}
	
	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
			
	}

}