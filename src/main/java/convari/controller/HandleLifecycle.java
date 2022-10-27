
package convari.controller;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import convari.controller.operation.OperationController;
import convari.logger.Logger;
import convari.logger.LoggerDriver;
import convari.logger.LoggerImpl;
import convari.logger.LoggerListener;
import convari.mail.MailController;
import convari.mail.MailControllerImpl;
import convari.messages.MessageManager;
import convari.messages.MessageManagerImpl;
import convari.persistence.Persistence;
import convari.persistence.PersistenceFacade;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseController;
import convari.response.ResponseControllerImpl;
import convari.response.ResponseMessage;
import convari.response.ResponseMessageListener;
import convari.security.Security;
import convari.security.SecurityFacade;
import convari.upload.ImageUploader;
import convari.upload.ImageUploaderFacade;
import convari.util.DateUtil;
import convari.weblogic.WebLogic;
import convari.weblogic.WebLogicFacade;



public class HandleLifecycle implements LoggerDriver, LoggerListener, ResponseMessageListener, 
											ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

	private final String PRIVATE_KEY = "30820276020100300d06092a864886f70d0101010500048202603082025c02010002818100a879c8cc765bc251b7405fc268645816f05c21767ae1ddbc2a58f4fd59daa9fd29c7a926b980d651423d1c86bd6ec499672bf6985ecd32fedcb9c79d3f69e547890a1c1ba88909328aa62e0ee72b70da3071dfe34ede3ca240ccd5fdc230fef1047f109cea909084fccb38688e9181faa0960e53a917113cc655b91b514fb9bd020301000102818040ba416e648ece728cc03bca3a474b1ccd0de88157a6cd850c940ed403a0a22cb4cf93ea5fdce2fa674389612f088e9a0d6c739a88d9b79cf97a9af98c328836bb16f9c421ed15af702f55915ee83504b4b02297247314ed810ff26a1f6ed405e0ce7c899acad714d2f798bb8660d69173cd3f62f5b7b8cebd82cf1cdd94ce95024100d17976fcbaf0d344089fd4da269202cc92dbcad700343e53e821000c889c26be62df917aa8eed0c2cf1a73ca805c7f5a6afdb3624c4538ffd4ef959aaa5527e3024100cde52a9adbf8fc6e4a67001a8bf0011f66a78bf0c62ac47043a6aa7a0e837ea0b16b4e0bae8c6e01e7f070283f3ac92867cb2dde97c5baee7586bfac914909df02410091b8b4ecb5c388c4c0572804a66656aa27d3dcf4da807d87736959965e867a716168f3d467a6d2f5628b9d57971a41c20dc1f5c381197ac0864a3141ca12b94f024021aa8318e737032f9582da057d34353ac93244338e7397c3338ca0fb6c3c1c0c72763540bbbdc28f1539bf5d1559ab62abf610fe009b1841bd7dd701849008c902405301bb17c895b0c112379d60b6f7a25efafce5ea3fa875b93b471ce6bd7523ca88368b96be19ab3acde4791603152411a480e90db6369841a9e2bc36f6239418";
	private final String USER_IMAGE_DIR_PATH = "users-images/";
	private final String ROOT_URL = "http://sociedadefeliz.com.br/ConVari";
	private final String DATASOURCE_URL = "java:comp/env/jdbc/convari";			

	private final String MAIL_USERNAME = "contato@sociedadefeliz.com.br";
	private final String MAIL_PASSWORD = "6iSHdPZW";
	private final long LOGIN_ABSENT_DELAY = 300000;
	private final int ANONYMOUS_USER_ID = 0;
	private final int SYSTEM_USER_ID = 1;
	
	private Security security = new SecurityFacade();
	private Persistence persistence = new PersistenceFacade();
	private WebLogic webLogic = new WebLogicFacade();
	private MessageManager messageManager = new MessageManagerImpl();
	private DateUtil dateUtil = new DateUtil( messageManager );
	private MailController mailController = new MailControllerImpl( MAIL_USERNAME, MAIL_PASSWORD );
	private ImageUploader imageUploader = new ImageUploaderFacade( USER_IMAGE_DIR_PATH );
	
	private Logger logger = new LoggerImpl( this, this );
	private ResponseController responseController = new ResponseControllerImpl( this );
	private OperationController operationController = new OperationController();
	
    public void contextInitialized(ServletContextEvent e) {
    	ServletContext context = e.getServletContext();    	    	    
    	context.setAttribute( "security", security );
    	context.setAttribute( "persistence", persistence );
    	context.setAttribute( "web-logic", webLogic );
    	context.setAttribute( "logger", logger ); 
    	context.setAttribute( "operation-controller", operationController );
    	context.setAttribute( "response-controller", responseController );
    	context.setAttribute( "message-manager", messageManager );
    	context.setAttribute( "system-user-id", SYSTEM_USER_ID );
    	context.setAttribute( "anonymous-user-id", ANONYMOUS_USER_ID );
    	context.setAttribute( "private-key", PRIVATE_KEY );
    	context.setAttribute( "root-url", ROOT_URL );
    	context.setAttribute( "login-absent-delay", LOGIN_ABSENT_DELAY );
    	context.setAttribute( "mail-controller", mailController );
    	context.setAttribute( "date-util", dateUtil );
    	context.setAttribute( "image-uploader", imageUploader );
    	try {
    		messageManager.processConfigFile();
			persistence.setDataSourceURL( DATASOURCE_URL );
			operationController.initialize();
		} catch (Exception ex) {
			ex.printStackTrace();
			responseController.addSystemErrorMessage( KeyResponseMessageConstants.SYSTEM_ERROR );
			responseController.addSystemInfoMessage( KeyResponseMessageConstants.SYSTEM_INFO );
		}
    }

    public void contextDestroyed(ServletContextEvent e) {
    	
    }

	public void sessionCreated(HttpSessionEvent e) {
		
	}

	public void sessionDestroyed(HttpSessionEvent e) {
		
	}

	public void attributeReplaced(HttpSessionBindingEvent e) {
		
	}
	
	public void attributeAdded(HttpSessionBindingEvent e) {
		
	}		
	
	public void attributeRemoved(HttpSessionBindingEvent e) {
		
	}

	public void message(HttpServletRequest request, ResponseMessage message ) {
		logger.addEventLog( request, message );
	}

	public void loggerException(Logger logger, Exception e) {
		e.printStackTrace();
	}

	public Persistence getPersistence() {
		return persistence;
	}

	public WebLogic getWebLogic() {
		return webLogic;
	}
	
}
