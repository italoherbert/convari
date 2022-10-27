package convari.controller.operation.captcha;

import italo.captcha.CaptchaGenerator;
import italo.validate.RequestValidatorParam;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import convari.controller.operation.Operation;
import convari.controller.operation.OperationController;
import convari.controller.operation.OperationException;
import convari.controller.operation.OperationParameters;
import convari.controller.operation.ValidationOpException;
import convari.weblogic.WebLogic;


public class GenCaptchaOperation implements Operation {

	private CaptchaGenerator captchaGenerator = new CaptchaGenerator();
	private String randomCharacters = "0123456789abcdefghijklmnopqrstuvwxyz";

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {
		
	}
	
	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		HttpServletRequest request = parameters.getRequest();
		HttpServletResponse response = parameters.getResponse();
		WebLogic webLogic = parameters.getWebLogic();
		
		String cmd = request.getParameter( "cmd" );
		
		String key = null;
		if( cmd.equals( "login" ) ) {
			key = WebLogic.LOGIN;
		} else if( cmd.equals( "login-recovery" ) ) {
			key = WebLogic.LOGIN_RECOVERY;
		} else if( cmd.equals( "register" ) ) {
			key = WebLogic.REGISTER;
		} else if( cmd.equals( "password-redefinition" ) ) {
			key = WebLogic.PASSWORD_REDEFINITION;
		}
				
		if( key != null ) {
			String code = captchaGenerator.genRandomCaptcha( randomCharacters );
			BufferedImage image = captchaGenerator.genCaptchaImage( code );			
			webLogic.setCaptchaCode( request, key, code );			
						
			response.setContentType( "image/jpeg" );
			try {
				OutputStream out = response.getOutputStream();
				ImageIO.write( image, "jpg", out );
				
				out.close();
			} catch (IOException e) {
				throw new OperationException( e );
			}										
		}
	}

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		
	}

}
