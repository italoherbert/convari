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



public class OperationParametersImpl implements OperationParameters {

	private int uid = -1;
	private int systemUserId;
	private int anonymousUserId;
	private long loginAbsentDelay;
	private Security security;
	private Persistence persistence;
	private WebLogic webLogic;
	private Logger logger;
	private MailController mailController;
	private DateUtil dateUtil;
	private String privateKey;	
	private String rootURL;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpServlet servlet;
	private ResponseBuilder responseBuilder;
	private ImageUploader imageUploader;
	private MessageManager messageManager;
	
	public OperationParametersImpl( HttpServlet servlet, 
			HttpServletRequest request, HttpServletResponse response, 
			ResponseBuilder responseBuilder ) {
		super();
		this.servlet = servlet;
		this.request = request;
		this.response = response;
		this.responseBuilder = responseBuilder;
	}

	public int getUID() {
		return uid;
	}

	public void setUID(int uid) {
		this.uid = uid;
	}

	public int getSystemUserId() {
		return systemUserId;
	}

	public void setSystemUserId(int systemUserId) {
		this.systemUserId = systemUserId;
	}

	public int getAnonymousUserId() {
		return anonymousUserId;
	}

	public void setAnonymousUserId(int anonymousUserId) {
		this.anonymousUserId = anonymousUserId;
	}

	public long getLoginAbsentDelay() {
		return loginAbsentDelay;
	}

	public void setLoginAbsentDelay(long loginAbsentDelay) {
		this.loginAbsentDelay = loginAbsentDelay;
	}

	public Persistence getPersistence() {
		return persistence;
	}

	public void setPersistence(Persistence persistence) {
		this.persistence = persistence;
	}

	public WebLogic getWebLogic() {
		return webLogic;
	}

	public void setWebLogic(WebLogic webLogic) {
		this.webLogic = webLogic;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public MailController getMailController() {
		return mailController;
	}

	public void setMailController(MailController mailController) {
		this.mailController = mailController;
	}

	public DateUtil getDateUtil() {
		return dateUtil;
	}

	public void setDateUtil(DateUtil dateUtil) {
		this.dateUtil = dateUtil;
	}

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServlet getServlet() {
		return servlet;
	}

	public void setServlet(HttpServlet servlet) {
		this.servlet = servlet;
	}

	public ResponseBuilder getResponseBuilder() {
		return responseBuilder;
	}

	public void setResponseBuilder(ResponseBuilder responseBuilder) {
		this.responseBuilder = responseBuilder;
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getRootURL() {
		return rootURL;
	}

	public void setRootURL(String rootURL) {
		this.rootURL = rootURL;
	}

	public ImageUploader getImageUploader() {
		return imageUploader;
	}

	public void setImageUploader(ImageUploader imageUploader) {
		this.imageUploader = imageUploader;
	}

}
