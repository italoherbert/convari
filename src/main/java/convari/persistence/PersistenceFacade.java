package convari.persistence;

import italo.persistence.db.DBManager;
import italo.persistence.db.DBManagerImpl;
import convari.persistence.bo.BOManager;


public class PersistenceFacade implements Persistence {
				
	private DBManager db = new DBManagerImpl();	
	private BOManager manager = new BOManager( db );
	
	public PersistenceFacade() {
		db.getConfig().setDataSourceConnection( true );
	}
				
	public String getDataSourceURL() {
		return db.getConfig().getDataSourceURL();
	}

	public void setDataSourceURL(String dataSourceURL) {
		db.getConfig().setDataSourceURL( dataSourceURL );
	}

	public UserManager getUserManager() {
		return manager.getUserManager();
	}
	
	public LoginManager getLoginManager() {
		return manager.getLoginManager();
	}

	public PostManager getPostManager() {
		return manager.getPostManager();
	}

	public OperationManager getOperationManager() {
		return manager.getOperationManager();
	}

	public InvitationManager getContactManager() {
		return manager.getInvitationManager();
	}
	
	public NotificationManager getNotificationManager() {
		return manager.getNotificationManager();
	}

	public CommentManager getCommentManager() {
		return manager.getCommentManager();
	}

	public LoginRecoveryManager getLoginRecoveryManager() {
		return manager.getPWRecoveryManager();
	}

	public MenuManager getMenuManager() {
		return manager.getMenuManager();
	}

	public LogManager getLogManager() {
		return manager.getLogManager();
	}

	public DBManager getDBManager() {
		return db;
	}
	
}
