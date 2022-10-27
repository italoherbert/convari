package convari.controller.operation.login;

import italo.validate.RequestValidatorParam;

import java.sql.Timestamp;
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
import convari.logger.Logger;
import convari.persistence.Persistence;
import convari.persistence.PersistenceException;
import convari.persistence.bean.OperationBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.security.Security;
import convari.weblogic.SessionUserBean;
import convari.weblogic.WebLogic;




public class LoginOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam usernameP = manager.createParam( "username" );
		usernameP.getValidators().add( manager.createRequiredValidator() );
		usernameP.getValidators().add( manager.createLengthValidator( 100 ) );
		
		RequestValidatorParam passwordP = manager.createParam( "password" );
		passwordP.getValidators().add( manager.createRequiredValidator() );
		
		vparams.add( usernameP );
		vparams.add( passwordP );
	}
	
	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		WebLogic webLogic = parameters.getWebLogic();
		//Security security = parameters.getSecurity();
		HttpServletRequest request = parameters.getRequest();
		//String privateKey = parameters.getPrivateKey();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		Logger logger = parameters.getLogger();
		Document doc = responseBuilder.getDoc();
		
		try {									
			String username = request.getParameter( "username" );
			String password = request.getParameter( "password" );

			Element loadCaptchaNode = doc.createElement( "load-captcha" );
			responseBuilder.getBodyNode().appendChild( loadCaptchaNode );			
			
			boolean loadCaptcha = false;
			boolean captchaValid = true;			
			int tryCount = webLogic.getInvalidTryCount( request, WebLogic.LOGIN );
			if( tryCount >= 3 ) {
				captchaValid = false;
				String captcha = request.getParameter( "captcha" );
				if( captcha != null ) {
					String captchaCode = webLogic.getCaptchaCode( request, WebLogic.LOGIN ); 
					captchaValid = captcha.equals( captchaCode );
				}
			}
			
			if( captchaValid ) {
                                //password = security.decrypt( password, privateKey );
				
				int userId = persistence.getUserManager().findId( username, password );
				if( userId > -1 ) {
					List<OperationBean> operations = persistence.getUserManager().findOperations( userId );
					
					webLogic.createUserSession( request, userId, username );
					SessionUserBean sessionUser = webLogic.getSessionUser( request );
					sessionUser.setOperations( operations ); 
															
					persistence.getLoginManager().setConnected( userId, true );
					persistence.getLoginManager().setLastAccess( userId, new Timestamp( System.currentTimeMillis() ) );
					
					Element idNode = doc.createElement( "userid" );
					idNode.appendChild( doc.createTextNode( String.valueOf( userId ) ) );
					
					responseBuilder.getBodyNode().appendChild( idNode );
					
					logger.addLoginSuccessLog( sessionUser, request.getRemoteAddr() );
				} else {
					webLogic.invalidTry( request, WebLogic.LOGIN ); 
					loadCaptcha =  webLogic.getInvalidTryCount( request, WebLogic.LOGIN ) >= 3;
						
					responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.INVALID_LOGIN_ERROR );
					logger.addLoginFailLog( username, request.getRemoteAddr() );
				}					
			} else {
				loadCaptcha = true;
				responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.INVALID_VERIFICATION_CODE_ERROR );
				responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.INVALID_VERIFICATION_CODE_INFO );
			}
			loadCaptchaNode.appendChild( responseBuilder.createTextNode( String.valueOf( loadCaptcha ) ) );
		/*} catch (SecurityException e) {
			throw new OperationException( e );*/
		} catch (PersistenceException e) {
			throw new OperationException( e );
		}
	}
	
	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
					
	}

}
