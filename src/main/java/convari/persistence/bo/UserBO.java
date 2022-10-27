package convari.persistence.bo;

import italo.persistence.db.ConnectionDBManager;
import italo.persistence.db.DBManagerException;
import italo.persistence.find.ControlDatesFindBean;
import italo.persistence.find.parameters.UserFindParameters;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import convari.persistence.PersistenceException;
import convari.persistence.UserManager;
import convari.persistence.bean.BasicUserDataBean;
import convari.persistence.bean.FindUserBean;
import convari.persistence.bean.InvitationBean;
import convari.persistence.bean.LoginBean;
import convari.persistence.bean.OperationBean;
import convari.persistence.bean.UserBean;
import convari.persistence.bean.UserConfigBean;
import convari.persistence.bean.UserContactDataBean;
import convari.persistence.bean.UserGeneralDataBean;
import convari.persistence.bean.UserMailBean;
import convari.persistence.bean.UserProfessionalDataBean;
import convari.persistence.bean.VisibilityBean;
import convari.persistence.dao.DAOException;
import convari.persistence.dao.InvitationDAO;
import convari.persistence.dao.LoginDAO;
import convari.persistence.dao.OperationDAO;
import convari.persistence.dao.RoleDAO;
import convari.persistence.dao.SQLFunctionsDAO;
import convari.persistence.dao.UserConfigDAO;
import convari.persistence.dao.UserContactDataDAO;
import convari.persistence.dao.UserDAO;
import convari.persistence.dao.UserGeneralDataDAO;
import convari.persistence.dao.UserProfessionalDataDAO;


public class UserBO implements UserManager {

	private ConnectionDBManager manager;
	private UserDAO userDAO;
	private RoleDAO roleDAO;
	private OperationDAO operationDAO;
	private LoginDAO loginDAO;
	private UserGeneralDataDAO personalInfoDAO;
	private UserContactDataDAO contactDataDAO;
	private UserProfessionalDataDAO professionalInfoDAO;
	private InvitationDAO invitationDAO;
	private UserConfigDAO userConfigDAO;
	private SQLFunctionsDAO sqlFuncsDAO;
	
	public UserBO(ConnectionDBManager manager, UserDAO userDAO,
			RoleDAO roleDAO, OperationDAO resourceDAO, LoginDAO loginDAO,
			UserGeneralDataDAO personalInfoDAO, UserContactDataDAO contactDataDAO,
			UserProfessionalDataDAO professionalInfoDAO, InvitationDAO invitationDAO, 
			UserConfigDAO userConfigDAO, 
			SQLFunctionsDAO sqlFuncsDAO) {
		super();
		this.manager = manager;
		this.userDAO = userDAO;
		this.roleDAO = roleDAO;
		this.operationDAO = resourceDAO;
		this.loginDAO = loginDAO;
		this.personalInfoDAO = personalInfoDAO;
		this.contactDataDAO = contactDataDAO;
		this.professionalInfoDAO = professionalInfoDAO;
		this.invitationDAO = invitationDAO;
		this.userConfigDAO = userConfigDAO;
		this.sqlFuncsDAO = sqlFuncsDAO;
	}

	public UserBean create() {
		UserBean user = new UserBean();
		user.setLogin( new LoginBean() );
		user.setGeneralData( new UserGeneralDataBean() );
		user.setContactData( new UserContactDataBean() );
		user.setProfessionalData( new UserProfessionalDataBean() );		
		user.setConfig( new UserConfigBean() );
		user.setOperations( new ArrayList<OperationBean>() );
		
		user.getLogin().setStatus( LoginBean.UNOCCUPIED_STATUS );
		user.getConfig().setEnderVisibility( VisibilityBean.OWNER_ONLY_VISIBILITY );
		user.getConfig().setTelVisibility( VisibilityBean.CONTACTS_ONLY_VISIBILITY );
		user.getConfig().setMailVisibility( VisibilityBean.CONTACTS_ONLY_VISIBILITY );
		return user;
	}
				
	public void addUserRole( int id ) throws PersistenceException {
		this.addRole( id, "user" );
	}
	
	public ControlDatesFindBean findControlDates( UserFindParameters findParameters ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return userDAO.findControlDates( c, findParameters );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public List<FindUserBean> find( UserFindParameters findParameters ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return userDAO.find( c, findParameters );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}

	public void addRole( int id, String roleName ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				int roleId = roleDAO.idForName( c, roleName );
				roleDAO.addRoleToUser( c, id, roleId );
				manager.commit( c );
			} catch (DAOException e) {
				manager.rollback( c );
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}	
	}		
	
	public List<OperationBean> findOperations( int userId ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return operationDAO.getOperationsByUser( c, userId );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public List<InvitationBean> findContacts( int id ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return invitationDAO.listForUserByStatus( c, id, InvitationBean.ACCEPTED_INVITATION );  
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public int count( String username ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return loginDAO.count( c, username );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}		
	}
	
	public boolean exists( int id ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return userDAO.exists( c, id ); 
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public int findId( String username, String password ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return userDAO.findId( c, username, password );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public String findImagepath( int id ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return personalInfoDAO.findImagePath( c, id );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}

	
	public UserMailBean findMailForUsername( String username ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return userDAO.findMailForUsername( c, username );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
			
	public BasicUserDataBean findBasicUserData( int id ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return userDAO.findBasicUserData( c, id );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}

	public UserBean find(int id) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return userDAO.find( c, id );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public LoginBean findLogin(int id) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return loginDAO.find( c, id );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}

	public UserGeneralDataBean findGeneralData(int id) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return personalInfoDAO.find( c, id );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}

	public UserContactDataBean findContactData( int id ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return contactDataDAO.find( c, id );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}

	public UserProfessionalDataBean findProfessionalInfo(int id) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return professionalInfoDAO.find( c, id );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}

	public UserConfigBean findUserConfig(int id) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return userConfigDAO.find( c, id );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public void setImagepath( int id, String imagePath ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				personalInfoDAO.updateImagepath( c, id, imagePath );
				manager.commit( c );
			} catch (DAOException e) {
				manager.rollback( c );
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}

	public void updateLogin( int id, LoginBean login ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				loginDAO.update( c, id, login );
				manager.commit( c );
			} catch (DAOException e) {
				manager.rollback( c );
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}			
	}
	
	public void updateGeneralData( int id, UserGeneralDataBean personalInfo ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				personalInfoDAO.update( c, id, personalInfo );
				manager.commit( c );
			} catch (DAOException e) {
				manager.rollback( c );
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}		
	}
	
	public void updateContactData( int id, UserContactDataBean contactData ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				contactDataDAO.update( c, id, contactData );
				manager.commit( c );
			} catch (DAOException e) {
				manager.rollback( c );
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}		
	}
	
	public void updateProfessionalInfo( int id, UserProfessionalDataBean professionalInfo ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				professionalInfoDAO.update( c, id, professionalInfo );
				manager.commit( c );
			} catch (DAOException e) {
				manager.rollback( c );
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}	
	}
	
	public void updateConfig( int id, UserConfigBean config ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				userConfigDAO.update( c, id, config );
				manager.commit( c );
			} catch (DAOException e) {
				manager.rollback( c );
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}	
	}
		
	public void insert(UserBean user) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				userDAO.insert( c, user );
				int lastId = sqlFuncsDAO.lastInsertId( c );	
				user.setId( lastId );
				
				manager.commit( c );
			} catch (DAOException e) {
				manager.rollback( c );
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}	
	}		

	/*public void update(UserBean user) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				userDAO.update( c, user );
				manager.commit( c );
			} catch (DAOException e) {
				manager.rollback( c );
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}*/
	
}
