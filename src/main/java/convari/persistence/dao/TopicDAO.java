package convari.persistence.dao;

import italo.persistence.find.ControlDatesFindBean;
import italo.persistence.find.FindDAO;
import italo.persistence.find.parameters.FindParameters;
import italo.persistence.find.parameters.TopicFindParameters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import convari.persistence.bean.TopicBean;
import convari.persistence.bean.TopicOfUserBean;


public class TopicDAO {

	private FindDAO findDAO = new FindDAO();
	
	public TopicDAO() {
		findDAO.setControlDateField( "t.date" );
	}
	
	public void addTopic( Connection c, TopicBean topic, int userId ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"insert into topic (user_id, description, visibility_id) " +
				"values (?, ?, " +
				"(select id from visibility where visibility=?))" 
			);
			ps.setInt( 1, userId );
			ps.setString( 2, topic.getDescription() );
			ps.setString( 3, topic.getVisibility() ); 
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
	
	public String getVisibilityForTopic( Connection c, int topicId ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select v.visibility " +
				"from visibility v inner join topic t on v.id=t.visibility_id " +
				"where t.id=?"
			);
			ps.setInt( 1, topicId );
			ResultSet rs = ps.executeQuery();
			if( rs.next() )
				return rs.getString( 1 );			
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}

	public List<TopicBean> getTopicsByUser( Connection c, int userId, Timestamp controlDate ) throws DAOException {
		List<TopicBean> list = new ArrayList<TopicBean>();
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select t.id, t.description, t.date, v.visibility, u.name, u.lastname, u.imagepath, u.id " +
				"from visibility v inner join topic t on v.id=t.visibility_id " +
						"inner join user u on u.id=t.user_id " +
				"where t.user_id=? " +
				(controlDate != null ? "and t.date>? " : "") + 
				"order by t.date"
			);
			ps.setInt( 1, userId );
			if( controlDate != null )
				ps.setTimestamp( 2, controlDate );
			ResultSet rs = ps.executeQuery();
			while( rs.next() ) {
				TopicBean topic = new TopicBean();
				topic.setId( rs.getInt( 1 ) );
				topic.setDescription( rs.getString( 2 ) );
				topic.setDate( rs.getTimestamp( 3 ) );
				topic.setVisibility( rs.getString( 4 ) );
				topic.setUserFirstname( rs.getString( 5 ) );
				topic.setUserLastname( rs.getString( 6 ) );
				topic.setUserImage( rs.getString( 7 ) );
				topic.setUserId( rs.getInt( 8 ) );
				list.add( topic );
			}
			return list;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public List<TopicOfUserBean> getPostsCountByTopics( Connection c, int userId ) throws DAOException {
		List<TopicOfUserBean> list = new ArrayList<TopicOfUserBean>();
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select t.id, t.description, count(*) " +
				"from topic t inner join post p on p.topic_id=t.id " +
				"where p.user_id=? " +				
				"group by t.id " +
				"order by t.date"
			);
			ps.setInt( 1, userId );
			ResultSet rs = ps.executeQuery();
			while( rs.next() ) {
				TopicOfUserBean topic = new TopicOfUserBean();				
				topic.setId( rs.getInt( 1 ) );
				topic.setDescription( rs.getString( 2 ) );		
				topic.setPostsCount( rs.getInt( 3 ) );
				list.add( topic );
			}
			return list;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public List<TopicBean> list( Connection c ) throws DAOException {
		List<TopicBean> list = new ArrayList<TopicBean>(); 
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select t.id, t.description, t.date, v.visibility, u.name, u.lastname, u.imagepath, u.id " +
				"from visibility v inner join topic t on v.id=t.visibility_id " +
						"inner join user u on u.id=t.user_id " +
				"order by t.date"
			);
			ResultSet rs = ps.executeQuery();
			while( rs.next() ) {
				TopicBean topic = new TopicBean();				
				topic.setId( rs.getInt( 1 ) );
				topic.setDescription( rs.getString( 2 ) );
				topic.setDate( rs.getTimestamp( 3 ) );
				topic.setVisibility( rs.getString( 4 ) );
				topic.setUserFirstname( rs.getString( 5 ) );
				topic.setUserLastname( rs.getString( 6 ) );
				topic.setUserImage( rs.getString( 7 ) );
				topic.setUserId( rs.getInt( 8 ) );
				list.add( topic );
			}
			return list;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public TopicBean find( Connection c, int id ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
					"select t.description, t.date, v.visibility, u.name, u.lastname, u.imagepath, u.id " +
					"from visibility v inner join topic t on v.id=t.visibility_id " +
						"inner join user u on u.id=t.user_id " +
					"where t.id=? " +
					"limit 1"
			);
			ps.setInt( 1, id );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				TopicBean topic = new TopicBean();		
				topic.setId( id );
				topic.setDescription( rs.getString( 1 ) );
				topic.setDate( rs.getTimestamp( 2 ) );
				topic.setVisibility( rs.getString( 3 ) );
				topic.setUserFirstname( rs.getString( 4 ) );
				topic.setUserLastname( rs.getString( 5 ) );
				topic.setUserImage( rs.getString( 6 ) );
				topic.setUserId( rs.getInt( 7 ) );
				return topic;
			}
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public ControlDatesFindBean findControlDates( Connection c, TopicFindParameters findParameters ) throws DAOException {
		int userId = findParameters.getUserId();
		int vWeight = findParameters.getVisibilityWeight();
		
		String[][] fields = new String[][] {
			{ "t.description", findParameters.getTQuery() },
			{ "concat(u.name,' ',u.lastname)", findParameters.getUQuery() }
		};

		String userIdSql = ( userId == FindParameters.INT_NULL ? "true" : "t.user_id="+userId );
		String vWeightSql = ( vWeight == FindParameters.INT_NULL ? "true" : "v.weight<="+vWeight );
		
		String sql = 
			"select t.date " +
				"from visibility v inner join topic t on v.id=t.visibility_id " +
				"inner join user u on u.id=t.user_id " +
			"where " + userIdSql + " and " + vWeightSql + " and :conditions " +
			"order by t.date desc " +
			":limit";
		
		return findDAO.findControlDates( c, findParameters, fields, sql );		
	}
	
	public List<TopicBean> find( Connection c, TopicFindParameters findParameters ) throws DAOException {
		int userId = findParameters.getUserId();
		int vWeight = findParameters.getVisibilityWeight();
		try {
			List<TopicBean> list = new ArrayList<TopicBean>();
			
			String userIdSql = ( userId == FindParameters.INT_NULL ? "true" : "t.user_id="+userId );
			String vWeightSql = ( vWeight == FindParameters.INT_NULL ? "true" : "v.weight<="+vWeight );
						
			PreparedStatement ps = c.prepareStatement( findDAO.buildSQL( 					 
				findParameters, 
				new String[][] {
					{ "t.description", findParameters.getTQuery() },
					{ "concat(u.name,' ',u.lastname)", findParameters.getUQuery() }
				},
				"select t.id, t.description, t.date, v.visibility, u.name, u.lastname, u.imagepath, u.id " +
					"from visibility v inner join topic t on v.id=t.visibility_id " +
					"inner join user u on u.id=t.user_id " +
				"where " + userIdSql + " and " + vWeightSql + " and :conditions " +
				"order by t.date desc " +
				":limit"					
			) );
							
			ResultSet rs = ps.executeQuery();			
			while( rs.next() ) {
				TopicBean topic = new TopicBean();		
				topic.setId( rs.getInt( 1 ) );
				topic.setDescription( rs.getString( 2 ) );
				topic.setDate( rs.getTimestamp( 3 ) );
				topic.setVisibility( rs.getString( 4 ) );
				topic.setUserFirstname( rs.getString( 5 ) );
				topic.setUserLastname( rs.getString( 6 ) );
				topic.setUserImage( rs.getString( 7 ) );
				topic.setUserId( rs.getInt( 8 ) );
				list.add( topic );
			}			
			return list;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
}
