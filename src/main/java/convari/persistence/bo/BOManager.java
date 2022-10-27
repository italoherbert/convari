package convari.persistence.bo;

import convari.persistence.CommentManager;
import convari.persistence.InvitationManager;
import convari.persistence.LogManager;
import convari.persistence.LoginManager;
import convari.persistence.LoginRecoveryManager;
import convari.persistence.MenuManager;
import convari.persistence.NotificationManager;
import convari.persistence.OperationManager;
import convari.persistence.PostManager;
import convari.persistence.UserManager;
import convari.persistence.dao.CommentDAO;
import convari.persistence.dao.InvitationDAO;
import convari.persistence.dao.LogDAO;
import convari.persistence.dao.LoginDAO;
import convari.persistence.dao.LoginRecoveryDAO;
import convari.persistence.dao.MenuDAO;
import convari.persistence.dao.NotificationDAO;
import convari.persistence.dao.OperationConstraintDAO;
import convari.persistence.dao.OperationDAO;
import convari.persistence.dao.UserContactDataDAO;
import convari.persistence.dao.UserGeneralDataDAO;
import convari.persistence.dao.PostDAO;
import convari.persistence.dao.UserProfessionalDataDAO;
import convari.persistence.dao.RoleDAO;
import convari.persistence.dao.SQLFunctionsDAO;
import convari.persistence.dao.TopicDAO;
import convari.persistence.dao.UserConfigDAO;
import convari.persistence.dao.UserDAO;
import convari.persistence.dao.VisibilityDAO;
import italo.persistence.db.ConnectionDBManager;

public class BOManager {
	
	private UserManager userBO;
	private LoginManager loginBO;
	private OperationManager opeartionBO;
	private PostManager postBO;
	private InvitationManager invitationBO;
	private NotificationManager notificationBO;
	private CommentManager commentBO;
	private LoginRecoveryManager pwRecoveryBO;	
	private LogManager logBO;		
	private MenuManager menuBO;
	
	public BOManager(ConnectionDBManager manager) {
		SQLFunctionsDAO sqlFuncsDAO = new SQLFunctionsDAO(); 
		LoginDAO loginDAO = new LoginDAO();
		UserGeneralDataDAO generalDataDAO = new UserGeneralDataDAO();
		UserContactDataDAO contactDataDAO = new UserContactDataDAO();
		UserProfessionalDataDAO professionalDataDAO = new UserProfessionalDataDAO();
		UserConfigDAO userConfigDAO = new UserConfigDAO();
		OperationDAO resourceDAO = new OperationDAO();
		OperationConstraintDAO operationConstraintDAO = new OperationConstraintDAO();
		VisibilityDAO visibilityDAO = new VisibilityDAO();
		RoleDAO roleDAO = new RoleDAO();
		TopicDAO topicDAO = new TopicDAO();
		PostDAO postDAO = new PostDAO();
		UserDAO userDAO = new UserDAO();
		InvitationDAO invitationDAO = new InvitationDAO();
		NotificationDAO notificationDAO = new NotificationDAO();
		CommentDAO commentDAO = new CommentDAO();
		LoginRecoveryDAO pwRecoveryDAO = new LoginRecoveryDAO();
		MenuDAO menuDAO = new MenuDAO();
		LogDAO logDAO = new LogDAO();
		
		this.userBO = new UserBO( manager, 
				userDAO, roleDAO, resourceDAO, 
				loginDAO, generalDataDAO, contactDataDAO, professionalDataDAO, invitationDAO,
				userConfigDAO, sqlFuncsDAO );
		this.loginBO = new LoginBO( manager, loginDAO );
		this.postBO = new PostBO( manager, topicDAO, postDAO, sqlFuncsDAO );		
		this.opeartionBO = new OperationBO( manager, resourceDAO, operationConstraintDAO, visibilityDAO );
		this.invitationBO = new InvitationBO( manager, invitationDAO );
		this.notificationBO = new NotificationBO( manager, notificationDAO );
		this.commentBO = new CommentBO( manager, commentDAO );
		this.pwRecoveryBO = new LoginRecoveryBO( manager, pwRecoveryDAO );
		this.logBO = new LogBO( manager, logDAO, sqlFuncsDAO );
		this.menuBO = new MenuBO( manager, menuDAO );
	}
	
	public UserManager getUserManager() {
		return userBO;
	}
		
	public LoginManager getLoginManager() {
		return loginBO;
	}
	
	public OperationManager getOperationManager() {
		return opeartionBO;
	}
	
	public PostManager getPostManager() {
		return postBO;
	}
	
	public InvitationManager getInvitationManager() {
		return invitationBO;
	}
	
	public NotificationManager getNotificationManager() {
		return notificationBO;
	}
	
	public CommentManager getCommentManager() {
		return commentBO;
	}
	
	public LoginRecoveryManager getPWRecoveryManager() {
		return pwRecoveryBO;
	}
	
	public MenuManager getMenuManager() {
		return menuBO;
	}

	public LogManager getLogManager() {
		return logBO;
	}
	
}
