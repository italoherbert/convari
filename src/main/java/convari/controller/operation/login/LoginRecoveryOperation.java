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
import convari.mail.MailController;
import convari.mail.MailException;
import convari.messages.KeyMessageConstants;
import convari.messages.MessageManager;
import convari.messages.MessageManagerException;
import convari.persistence.Persistence;
import convari.persistence.PersistenceException;
import convari.persistence.bean.UserMailBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.security.Security;
import convari.security.SecurityException;
import convari.util.DateUtil;
import convari.weblogic.WebLogic;



public class LoginRecoveryOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam usernameP = manager.createParam( "username" );
		usernameP.getValidators().add( manager.createRequiredValidator() );
		usernameP.getValidators().add( manager.createLengthValidator( 100 ) );
		
		RequestValidatorParam captchaP = manager.createParam( "captcha" );
		captchaP.getValidators().add( manager.createRequiredValidator() );
		captchaP.getValidators().add( manager.createLengthValidator( 4 ) );
		
		RequestValidatorParam langP = manager.createParam( "lang" );
		langP.getValidators().add( manager.createLengthValidator( 5 ) );

		vparams.add( usernameP );
		vparams.add( captchaP );
		vparams.add( langP );
	}
	
	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		Security security = parameters.getSecurity();
		MailController mailController = parameters.getMailController();
		DateUtil dateUtil = parameters.getDateUtil();
		WebLogic webLogic = parameters.getWebLogic();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		Document doc = responseBuilder.getDoc();
		HttpServletRequest request = parameters.getRequest();
		MessageManager messageManager = parameters.getMessageManager();		
		String rootURL = parameters.getRootURL();
		
		String username = request.getParameter( "username" );
		String captcha = request.getParameter( "captcha" );
		
		String lang = request.getParameter( "lang" );
		try {
			Element loadCaptchaNode = doc.createElement( "load-captcha" );
			responseBuilder.getBodyNode().appendChild( loadCaptchaNode );			
			
			boolean captchaValid = false;			
			String captchaCode = webLogic.getCaptchaCode( request, WebLogic.LOGIN_RECOVERY );
			if( captchaCode != null)
				captchaValid = captcha.equals( captchaCode );							
			
			if( captchaValid ) {
				UserMailBean userMail = persistence.getUserManager().findMailForUsername( username ); 
				if( userMail != null ) {						
					String code = security.genMAC( username );
					Timestamp date = persistence.getLoginRecoveryManager().setLoginRecovery( userMail.getUserId(), code );
					
					String to = userMail.getMail();
					String subject = messageManager.getMessage( lang, KeyMessageConstants.LOGIN_RECOVERY_MAIL_SUBJECT );
					String message = messageManager.getHTMLText( lang, KeyMessageConstants.HTML_LOGIN_RECOVERY_MAIL, 
							rootURL, code, rootURL, code, dateUtil.formatToLongDate( date, lang ) );  
					
					try {
						mailController.sendMail( to, subject, message );
						responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.PASSWORD_REDEFINITION_SEND_LINK_INFO );
						responseBuilder.processTextInfoMSG( userMail.getMail() );
					} catch (MailException e) {
						responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.PASSWORD_REDEFINITION_SEND_LINK_ERROR );
					}										
				} else {
					responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.USER_NOT_FOUND_BY_USERNAME_ERROR );
				}
			} else {
				responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.INVALID_VERIFICATION_CODE_ERROR );
				responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.INVALID_VERIFICATION_CODE_INFO );
			}
			loadCaptchaNode.appendChild( responseBuilder.createTextNode( String.valueOf( !captchaValid ) ) );
		} catch ( PersistenceException e ) {
			throw new OperationException( e );
		} catch (SecurityException e) {
			throw new OperationException( e );
		} catch (MessageManagerException e) {
			throw new OperationException( e );
		}	
	}

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		
	}

}
