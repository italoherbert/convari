package convari.persistence.dao;

import italo.persistence.find.ControlDatesFindBean;
import italo.persistence.find.FindDAO;
import italo.persistence.find.parameters.UserFindParameters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import convari.persistence.bean.BasicUserDataBean;
import convari.persistence.bean.FindUserBean;
import convari.persistence.bean.UserBean;
import convari.persistence.bean.UserConfigBean;
import convari.persistence.bean.UserContactDataBean;
import convari.persistence.bean.UserGeneralDataBean;
import convari.persistence.bean.UserMailBean;
import convari.persistence.bean.UserProfessionalDataBean;


public class UserDAO {
	
	private final static int ANONYMOUS_ID = 0;
				
	private FindDAO findDAO = new FindDAO();
	
	public UserDAO() {
		findDAO.setControlDateField( "u.last_access" );
	}
	
	public ControlDatesFindBean findControlDates( Connection c, UserFindParameters findParameters ) throws DAOException {						
		String[][] fields = new String[][] {
			{ "concat(u.name,' ',u.lastname)", findParameters.getNameQuery() }
		};
		
		String sql = 
			"select u.last_access " +
			"from user u " +
			"where u.id<>"+ANONYMOUS_ID+" and :conditions " +
			"order by u.last_access desc " +
			":limit";
		
		return findDAO.findControlDates( c, findParameters, fields, sql );		
	}
		
	public List<FindUserBean> find( Connection c, UserFindParameters findParameters ) throws DAOException {
		List<FindUserBean> list = new ArrayList<FindUserBean>();
		try {			
			PreparedStatement ps = c.prepareStatement( findDAO.buildSQL( 					 
				findParameters, 
				new String[][] {
					{ "concat(u.name,' ',u.lastname)", findParameters.getNameQuery() }
				},
				"select u.id, u.name, u.lastname, u.imagepath " +
				"from user u " +
				"where u.id<>"+ANONYMOUS_ID+" and :conditions " +
				"order by u.last_access desc " +
				":limit"					
			) );
						                        
			ResultSet rs = ps.executeQuery();			
			while( rs.next() ) {
				FindUserBean user = new FindUserBean();
				user.setId( rs.getInt( 1 ) );
				user.setName( rs.getString( 2 ) );
				user.setLastname( rs.getString( 3 ) );
				user.setImagePath( rs.getString( 4 ) );
				
				list.add( user );
			}			
			return list;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public boolean exists( Connection c, int id ) throws DAOException {
		try {			
			PreparedStatement ps = c.prepareStatement( 
				"select id from user where id=?" 
			);
			ps.setInt( 1, id );
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public int findId( Connection c, String username, String password ) throws DAOException {
		try {			
			PreparedStatement ps = c.prepareStatement( 
				"select id from user where username=? and password=sha1(?)" 
			);
			ps.setString( 1, username );
			ps.setString( 2, password );
			ResultSet rs = ps.executeQuery();
			if( rs.next() )
				return rs.getInt( 1 );
			return -1;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
				
	public UserMailBean findMailForUsername( Connection c, String username ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
					"select id, mail " +
					"from user " +
					"where username=?" 
			);
			ps.setString( 1, username );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				UserMailBean userMail = new UserMailBean();
				userMail.setUserId( rs.getInt( 1 ) );
				userMail.setMail( rs.getString( 2 ) );
				return userMail;
			}
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
	
	public void insert(Connection c, UserBean user) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"insert into user (username, password, " +
						"imagepath, name, lastname, sex, website, mail, mail2, " +
						"status_id, ender_visibility_id, tel_visibility_id, mail_visibility_id) " +
				"values (?, sha1(?), ?, ?, ?, ?, ?, ?, ?, " +
							"(select id from user_status where status=?)," +
							"(select id from visibility where visibility=?)," +
							"(select id from visibility where visibility=?)," +
							"(select id from visibility where visibility=?) )" 
			);			
			ps.setString( 1, user.getLogin().getUsername() );
			ps.setString( 2, user.getLogin().getPassword() );
			ps.setString( 3, user.getGeneralData().getImagepath() );
			ps.setString( 4, user.getGeneralData().getName() );
			ps.setString( 5, user.getGeneralData().getLastname() );
			ps.setString( 6, user.getGeneralData().getSex() );
			ps.setString( 7, user.getGeneralData().getWebsite() );
			ps.setString( 8, user.getContactData().getMail() );
			ps.setString( 9, user.getContactData().getMail2() );
			ps.setString( 10, user.getLogin().getStatus() );
			ps.setString( 11, user.getConfig().getEnderVisibility() );
			ps.setString( 12, user.getConfig().getTelVisibility() );
			ps.setString( 13, user.getConfig().getMailVisibility() );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
				
	public UserBean find( Connection c, int id ) throws DAOException {
		try {			
			PreparedStatement ps = c.prepareStatement( 
				"select u.imagepath, u.name, u.lastname, u.sex, u.website, " +
							"u.ender, u.city, u.state, u.country, u.tel, u.tel2, u.mail, u.mail2, " +
							"u.occupation, u.academic, " +
						"(select visibility from visibility where id=ender_visibility_id), " +
						"(select visibility from visibility where id=tel_visibility_id), " +
						"(select visibility from visibility where id=mail_visibility_id) " +
				"from user u " +
				"where u.id=? " +
				"limit 1" 
			);
			ps.setInt( 1, id );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				UserBean user = new UserBean();
				user.setGeneralData( new UserGeneralDataBean() );
				user.setContactData( new UserContactDataBean() );
				user.setProfessionalData( new UserProfessionalDataBean() );				
				user.setConfig( new UserConfigBean() );
				
				user.setId( id );		
				user.getGeneralData().setImagepath( rs.getString( 1 ) );
				user.getGeneralData().setName( rs.getString( 2 ) );
				user.getGeneralData().setLastname( rs.getString( 3 ) );
				user.getGeneralData().setSex( rs.getString( 4 ) );
				user.getGeneralData().setWebsite( rs.getString( 5 ) );
				
				user.getContactData().setEnder( rs.getString( 6 ) );
				user.getContactData().setCity( rs.getString( 7 ) );
				user.getContactData().setState( rs.getString( 8 ) );
				user.getContactData().setCountry( rs.getString( 9 ) );
				user.getContactData().setTel( rs.getString( 10 ) );
				user.getContactData().setTel2( rs.getString( 11 ) );
				user.getContactData().setMail( rs.getString( 12 ) );
				user.getContactData().setMail2( rs.getString( 13 ) );
				
				user.getProfessionalData().setOccupation( rs.getString( 14 ) );
				user.getProfessionalData().setAcademic( rs.getString( 15 ) );
				
				user.getConfig().setEnderVisibility( rs.getString( 16 ) );
				user.getConfig().setTelVisibility( rs.getString( 17 ) );
				user.getConfig().setMailVisibility( rs.getString( 18 ) );
				return user;
			}
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}	
	
	public BasicUserDataBean findBasicUserData( Connection c, int id ) throws DAOException {
		try {			
			PreparedStatement ps = c.prepareStatement( 
				"select imagepath, name, lastname, sex, occupation, academic " +
				"from user " +
				"where id=? " +
				"limit 1" 
			);
			ps.setInt( 1, id );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				BasicUserDataBean user = new BasicUserDataBean();
				user.setId( id );
				user.setImagepath( rs.getString( 1 ) );
				user.setName( rs.getString( 2 ) );
				user.setLastname( rs.getString( 3 ) );
				user.setSex( rs.getString( 4 ) );
				user.setOccupation( rs.getString( 5 ) );
				user.setAcademic( rs.getString( 6 ) );
				return user;
			}
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
}
