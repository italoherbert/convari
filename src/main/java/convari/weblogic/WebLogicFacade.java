package convari.weblogic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import convari.upload.ImageUploadProcess;


public class WebLogicFacade implements WebLogic {
	
	public void createUserSession( HttpServletRequest request, int uid, String username ) {		
		HttpSession session = request.getSession( false );
		if( session == null )
			session = request.getSession( true );	
		SessionUserBean sessionUser = new SessionUserBean( uid, username );
		session.setAttribute( USER_ATTR_NAME, sessionUser );
	}
	
	public String getCaptchaCode( HttpServletRequest request, String key ) {
		String attr = key + CAPTCHA_CODE_PREFIX;
		HttpSession session = request.getSession( false );
		if( session == null )
			return null;
		if( session.getAttribute( attr ) == null )
			return null;
		return String.valueOf( session.getAttribute( attr ) );	
	}

	public void setCaptchaCode( HttpServletRequest request, String key, String code ) {
		String attr = key + CAPTCHA_CODE_PREFIX;
		HttpSession session = request.getSession( false );
		if( session == null )
			session = request.getSession( true );
		session.setAttribute( attr, code );		
	}

	public void invalidTry( HttpServletRequest request, String key ) {
		String attr = key + INVALID_TRY_COUNT_PREFIX;
		HttpSession session = request.getSession( false );
		if( session == null )
			session = request.getSession( true );
		if( session.getAttribute( attr ) == null ) 
			session.setAttribute( attr, 1 );
		else {
			int invalidtrycount = Integer.parseInt( String.valueOf( session.getAttribute( attr ) ) );
			if( invalidtrycount < 3)
				session.setAttribute( attr, invalidtrycount+1 );					
		}
	}
	
	public int getInvalidTryCount( HttpServletRequest request, String key ) {
		String attr = key + INVALID_TRY_COUNT_PREFIX;
		HttpSession session = request.getSession( false );
		if( session == null )
			return 0;
		if( session.getAttribute( attr ) == null )
			return 0;
		return Integer.parseInt( String.valueOf( session.getAttribute( attr ) ) );
	}
	
	public void invalidateUserSession( HttpServletRequest request ) {
		HttpSession session = request.getSession( false );
		if( session != null ) {
			session.setAttribute( USER_ATTR_NAME, null );
			session.invalidate();
		}
	}
	
	public SessionUserBean getSessionUser( HttpServletRequest request ) {
		HttpSession session = request.getSession( false );
		if( session != null ) {
			Object sessionUser = session.getAttribute( USER_ATTR_NAME );
			if( sessionUser != null )
				return (SessionUserBean)sessionUser;
		}
		return null;
	}
	
	public boolean isUserLogged(HttpServletRequest request) {
		return this.getSessionUser( request ) != null;
	}

	public ImageUploadProcess getImageUploadProcessForSession(HttpServletRequest request) {
		SessionUserBean suser = this.getSessionUser( request );
		if( suser != null )
			return suser.getImageUploadProcess();
		return null;
	}
			
}

