package convari.controller.operation;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import convari.logger.Logger;
import convari.mail.MailController;
import convari.messages.MessageManager;
import convari.persistence.Persistence;
import convari.response.ResponseBuilder;
import convari.security.Security;
import convari.upload.ImageUploader;
import convari.util.DateUtil;
import convari.weblogic.WebLogic;



public interface OperationParameters {
	
	public int getUID();
	
	public void setUID( int uid );
	
	public int getSystemUserId();

	public void setSystemUserId(int systemUserId);

	public int getAnonymousUserId();

	public void setAnonymousUserId(int anonymousUserId);
	
	public long getLoginAbsentDelay();

	public void setLoginAbsentDelay(long loginAbsentDelay);
	
	public Security getSecurity();

	public void setSecurity(Security security);

	public Persistence getPersistence();

	public void setPersistence(Persistence persistence);	
	
	public WebLogic getWebLogic();
	
	public void setWebLogic(WebLogic webLogic);
	
	public Logger getLogger();

	public void setLogger(Logger logger);
	
	public MailController getMailController();
	
	public void setMailController(MailController sender);
	
	public DateUtil getDateUtil();
	
	public void setDateUtil( DateUtil util );
	
	public HttpServletRequest getRequest();

	public void setRequest(HttpServletRequest request);

	public HttpServletResponse getResponse();

	public void setResponse(HttpServletResponse response);

	public HttpServlet getServlet();
	
	public void setServlet( HttpServlet servlet );
	
	public ResponseBuilder getResponseBuilder();

	public void setResponseBuilder(ResponseBuilder responseBuilder);
	
	public MessageManager getMessageManager();

	public void setMessageManager(MessageManager messageManager);
	
	public String getPrivateKey();

	public void setPrivateKey(String privateKey);
	
	public String getRootURL();

	public void setRootURL(String rootURL);
	
	public ImageUploader getImageUploader();

	public void setImageUploader(ImageUploader uploader);
	
}
