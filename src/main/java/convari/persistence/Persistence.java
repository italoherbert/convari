package convari.persistence;


public interface Persistence {
					
	public String getDataSourceURL();

	public void setDataSourceURL(String dataSourceURL);
	
	public UserManager getUserManager();
	
	public LoginManager getLoginManager();
	
	public PostManager getPostManager();
	
	public OperationManager getOperationManager();
	
	public InvitationManager getContactManager();
	
	public NotificationManager getNotificationManager();
	
	public CommentManager getCommentManager();
		
	public LoginRecoveryManager getLoginRecoveryManager();
	
	public MenuManager getMenuManager();
	
	public LogManager getLogManager();
	
}
