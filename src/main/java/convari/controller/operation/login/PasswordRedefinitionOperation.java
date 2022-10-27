package convari.controller.operation.login;

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
import convari.persistence.bean.LoginRecoveryBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.security.Security;
import convari.security.SecurityException;
import convari.weblogic.WebLogic;



public class PasswordRedefinitionOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam passwordP = manager.createParam( "password" );
		passwordP.getValidators().add( manager.createRequiredValidator() );		
		
		RequestValidatorParam codeP = manager.createParam( "code" );
		codeP.getValidators().add( manager.createRequiredValidator() );
		codeP.getValidators().add( manager.createLengthValidator( 32 ) );

		RequestValidatorParam captchaP = manager.createParam( "captcha" );
		captchaP.getValidators().add( manager.createRequiredValidator() );
		captchaP.getValidators().add( manager.createLengthValidator( 4 ) );
		
		vparams.add( passwordP );
		vparams.add( codeP );
		vparams.add( captchaP );
	}
	
	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		Security security = parameters.getSecurity();
		WebLogic webLogic = parameters.getWebLogic();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		Document doc = responseBuilder.getDoc();
		HttpServletRequest request = parameters.getRequest();		
		
		
		String password = request.getParameter( "password" );
		String code = request.getParameter( "code" );
		String captcha = request.getParameter( "captcha" );

		try {
			Element loadCaptchaNode = doc.createElement( "load-captcha" );
			responseBuilder.getBodyNode().appendChild( loadCaptchaNode );			
			
			boolean captchaValid = false;			
			String captchaCode = webLogic.getCaptchaCode( request, WebLogic.PASSWORD_REDEFINITION );
			if( captchaCode != null)
				captchaValid = captcha.equals( captchaCode );							
			
			if( captchaValid ) {
				password = security.decrypt( password, parameters.getPrivateKey() );
				if( password.length() < 6 ) {
					responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.PASSWORD_REQUEST_PARAM_ERROR );
				} else {									
					LoginRecoveryBean recovery = persistence.getLoginRecoveryManager().findForTimeoutAndCode( code );				
					if( recovery == null ) {
						responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.PASSWORD_REDEFINITION_EXPIRED_ERROR );
						responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.PASSWORD_REDEFINITION_EXPIRED_INFO );
					} else {
						if( code.equals( recovery.getCode() ) ) {
							persistence.getLoginManager().setPassword( recovery.getUserId(), password );
							persistence.getLoginRecoveryManager().removeForLogin( recovery.getUserId() );
							responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.PASSWORD_REDEFINITION_SUCCESS_INFO );
						} else {
							responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.PASSWORD_REDEFINITION_INVALID_CODE_ERROR );
						}
					}
				}
			} else {
				responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.INVALID_VERIFICATION_CODE_ERROR );
				responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.INVALID_VERIFICATION_CODE_INFO );
			}
			loadCaptchaNode.appendChild( responseBuilder.createTextNode( String.valueOf( !captchaValid ) ) );
		} catch( PersistenceException e ) {
			throw new OperationException( e );
		} catch (SecurityException e) {
			throw new OperationException( e );
		}	
	}

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		
	}

}
