 package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import convari.persistence.bean.PostBean;


public class PostDAO {

	public void addPost( Connection c, PostBean post, int userId, int topicId ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"insert into post (user_id, topic_id, message) values (?, ?, ?)" 
			);
			ps.setInt( 1, userId );
			ps.setInt( 2, topicId );
			ps.setString( 3, post.getMessage() );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}

	public List<PostBean> getPostsByTopic( Connection c, int topicId, Timestamp controlDate ) throws DAOException {
		List<PostBean> list = new ArrayList<PostBean>();
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select p.id, p.message, p.date, u.id, u.name, u.lastname, u.imagepath " +
				"from user u inner join post p on u.id=p.user_id " +
				"where p.topic_id=? " +
				(controlDate != null ? "and p.date>? " : "") +				
				"order by p.date"
			);
			ps.setInt( 1, topicId );
			if( controlDate != null )
				ps.setTimestamp( 2, controlDate );
			ResultSet rs = ps.executeQuery();
			while( rs.next() ) {
				PostBean post = new PostBean();				
				post.setId( rs.getInt( 1 ) );
				post.setMessage( rs.getString( 2 ) );
				post.setDate( rs.getTimestamp( 3 ) );
				post.setUserId( rs.getInt( 4 ) );
				post.setUserFirstname( rs.getString( 5 ) );
				post.setUserLastname( rs.getString( 6 ) );
				post.setUserImage( rs.getString( 7 ) );
				post.setTopicId( topicId ); 				
				list.add( post );
			}
			return list;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
			
	public PostBean find( Connection c, int id ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select p.message, p.date, u.id, u.name, u.lastname, u.imagepath " +
				"from user u inner join post p on u.id=p.user_id " +
				"where p.id=?"
			);
			ps.setInt( 1, id );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				PostBean post = new PostBean();				
				post.setId( id );
				post.setMessage( rs.getString( 1 ) );				
				post.setDate( rs.getTimestamp( 2 ) );
				post.setUserId( rs.getInt( 3 ) );
				post.setUserFirstname( rs.getString( 4 ) );
				post.setUserLastname( rs.getString( 5 ) );
				post.setUserImage( rs.getString( 6 ) );
				return post;
			}
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
}

