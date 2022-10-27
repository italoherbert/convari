package convari.controller.operation.user;

import italo.validate.RequestValidatorParam;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import convari.persistence.bean.UserBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.security.SecurityException;
import convari.weblogic.WebLogic;



public class RegisterOperation implements Operation {

	public void initialize(OperationController controller, List<RequestValidatorParam> vparams) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
						
		RequestValidatorParam usernameP = manager.createParam( "username" );
		usernameP.getValidators().add( manager.createRequiredValidator() );
		usernameP.getValidators().add( manager.createLengthValidator( 3, 100 ) );

		RequestValidatorParam passwordP = manager.createParam( "password" );
		passwordP.getValidators().add( manager.createRequiredValidator() );
		
		RequestValidatorParam nameP = manager.createParam( "name" );
		nameP.getValidators().add( manager.createRequiredValidator() );
		nameP.getValidators().add( manager.createLengthValidator( 100 ) );
		
		RequestValidatorParam lastnameP = manager.createParam( "lastname" );
		lastnameP.getValidators().add( manager.createRequiredValidator() );
		lastnameP.getValidators().add( manager.createLengthValidator( 100 ) );

		RequestValidatorParam sexP = manager.createParam( "sex" );
		sexP.getValidators().add( manager.createRequiredValidator() );
		sexP.getValidators().add( manager.createLengthValidator( 1, 1 ) );
				
		RequestValidatorParam webSiteP = manager.createParam( "website" );
		webSiteP.getValidators().add( manager.createLengthValidator( 100 ) );		
		
		RequestValidatorParam mailP = manager.createParam( "mail" );
		mailP.getValidators().add( manager.createRequiredValidator() );
		mailP.getValidators().add( manager.createLengthValidator( 100 ) );
		mailP.getValidators().add( manager.createMailValidator() );
		
		RequestValidatorParam mail2P = manager.createParam( "mail2" );
		mail2P.getValidators().add( manager.createLengthValidator( 100 ) );
		mail2P.getValidators().add( manager.createMailValidator() );		
		
		RequestValidatorParam captchaP = manager.createParam( "captcha" );
		captchaP.getValidators().add( manager.createRequiredValidator() );
		captchaP.getValidators().add( manager.createLengthValidator( 4, 4 ) );
		
		vparams.add( usernameP );
		vparams.add( passwordP );
		
		vparams.add( nameP );
		vparams.add( lastnameP );
		vparams.add( sexP );
		vparams.add( webSiteP );
		
		vparams.add( mailP );
		vparams.add( mail2P );
		
		vparams.add( captchaP );
	}

	public void execute( OperationController controller, OperationParameters parameters )	throws OperationException {
		Persistence persistence = parameters.getPersistence();
		WebLogic webLogic = parameters.getWebLogic();
		HttpServletRequest request = parameters.getRequest();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		Document doc = responseBuilder.getDoc();
		
		String imagePath = parameters.getImageUploader().getDefaultImagePath();
		
		String username = request.getParameter( "username" );
		String password = request.getParameter( "password" );	
				
		String name = request.getParameter( "name" );
		String lastname = request.getParameter( "lastname" );
		String sex = request.getParameter( "sex" );
		String website = request.getParameter( "website" );
		
		String mail = request.getParameter( "mail" );
		String mail2 = request.getParameter( "mail2" );
		
		String captcha = request.getParameter( "captcha" );
		
		try {							
			Element loadCaptchaNode = doc.createElement( "load-captcha" );
			responseBuilder.getBodyNode().appendChild( loadCaptchaNode );			
			
			boolean captchaValid = false;			
			String captchaCode = webLogic.getCaptchaCode( request, WebLogic.REGISTER );
			if( captchaCode != null)
				captchaValid = captcha.equals( captchaCode );							
			
			if( captchaValid ) {								
				if( password.length() < 6 ) {
					responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.PASSWORD_REQUEST_PARAM_ERROR );
				} else {
					name = URLDecoder.decode( name, "utf-8" );
					lastname = URLDecoder.decode( lastname, "utf-8" );
												
					UserBean user = persistence.getUserManager().create();		
					user.getLogin().setUsername( username );
					user.getLogin().setPassword( password );
					user.getGeneralData().setImagePath( imagePath );
					user.getGeneralData().setName( name );
					user.getGeneralData().setLastname( lastname );
					user.getGeneralData().setSex( sex );
					user.getGeneralData().setWebsite( website );
					user.getContactData().setMail( mail );
					user.getContactData().setMail2( mail2 );
					persistence.getUserManager().insert( user );
					persistence.getUserManager().addUserRole( user.getId() );
					
					responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.SAVE_USER_INFO );
				}
			} else {
				responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.INVALID_VERIFICATION_CODE_ERROR );
				responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.INVALID_VERIFICATION_CODE_INFO );
			}
			loadCaptchaNode.appendChild( responseBuilder.createTextNode( String.valueOf( !captchaValid ) ) );
		} catch( PersistenceException e ) {
			throw new OperationException( e );
		} catch (UnsupportedEncodingException e) {
			throw new OperationException( e );
		}	
	}
	
	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		Persistence persistence = parameters.getPersistence();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		HttpServletRequest request = parameters.getRequest();
				
		String username = request.getParameter( "username" );
		try {
			int count = persistence.getUserManager().count( username );
			if( count > 0 )
				responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.SAVE_INDISPONIBLE_USERNAME );
		} catch (PersistenceException e) {
			throw new ValidationOpException( e );
		}		
	}

}