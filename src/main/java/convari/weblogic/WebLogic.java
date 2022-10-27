package convari.weblogic;

import javax.servlet.http.HttpServletRequest;

import convari.upload.ImageUploadProcess;


public interface WebLogic {
	
	public final static String USER_ATTR_NAME = "user";
	public final static String INVALID_TRY_COUNT_PREFIX = "-invalid-try-count";
	public final static String CAPTCHA_CODE_PREFIX= "-captcha-code";
		
	public final static String LOGIN = "login";
	public final static String LOGIN_RECOVERY = "login-recovery";
	public final static String REGISTER = "register"; 
	public final static String PASSWORD_REDEFINITION = "password-redefinition"; 	
	
	public void invalidTry( HttpServletRequest request, String tryKey );
	
	public int getInvalidTryCount( HttpServletRequest request, String tryKey );
	
	public String getCaptchaCode( HttpServletRequest request, String key );
	
	public void setCaptchaCode(  HttpServletRequest request, String key, String code );
	
	public void createUserSession( HttpServletRequest request, int uid, String username );
	
	public void invalidateUserSession( HttpServletRequest request );
	
	public SessionUserBean getSessionUser( HttpServletRequest request );
		
	public boolean isUserLogged( HttpServletRequest request );
	
	public ImageUploadProcess getImageUploadProcessForSession( HttpServletRequest request );
	
}
