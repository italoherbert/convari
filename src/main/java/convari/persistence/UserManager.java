package convari.persistence;

import italo.persistence.find.ControlDatesFindBean;
import italo.persistence.find.parameters.UserFindParameters;

import java.util.List;

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


public interface UserManager {
	
	public UserBean create();
	
	public ControlDatesFindBean findControlDates( UserFindParameters findParameters ) throws PersistenceException;
	
	public List<FindUserBean> find( UserFindParameters findParameters ) throws PersistenceException;	
			
	public void addUserRole( int id ) throws PersistenceException;

	public void addRole( int id, String roleName ) throws PersistenceException;
	
	public void setImagepath( int id, String imagePath ) throws PersistenceException;

	public void updateLogin( int id, LoginBean login ) throws PersistenceException;

	public void updateGeneralData( int id, UserGeneralDataBean generalData ) throws PersistenceException;

	public void updateContactData( int id, UserContactDataBean contactData ) throws PersistenceException;

	public void updateProfessionalInfo( int id, UserProfessionalDataBean professionalInfo ) throws PersistenceException;
	
	public void updateConfig( int id, UserConfigBean config ) throws PersistenceException;
		
	public void insert( UserBean user ) throws PersistenceException;
		
	public List<OperationBean> findOperations( int id ) throws PersistenceException;
	
	public List<InvitationBean> findContacts( int id ) throws PersistenceException;
	
	public String findImagepath( int id ) throws PersistenceException;
	
	public UserMailBean findMailForUsername( String username ) throws PersistenceException;
				
	public BasicUserDataBean findBasicUserData( int id ) throws PersistenceException;
	
	public UserBean find( int id ) throws PersistenceException;
		
	public LoginBean findLogin( int id ) throws PersistenceException;

	public UserGeneralDataBean findGeneralData( int id ) throws PersistenceException;
	
	public UserContactDataBean findContactData( int id ) throws PersistenceException;
	
	public UserProfessionalDataBean findProfessionalInfo( int id ) throws PersistenceException;

	public UserConfigBean findUserConfig( int id ) throws PersistenceException;
	
	public int findId( String username, String password ) throws PersistenceException;
	
	public boolean exists( int id ) throws PersistenceException;
	
	public int count( String username ) throws PersistenceException;
			
}
