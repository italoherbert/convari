package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import convari.persistence.bean.InvitationBean;
import convari.persistence.bean.MenuBean;


public class MenuDAO {

	public MenuBean findCounts( Connection c, int userId, boolean includeTopicsCount ) throws DAOException {
		try {						
			PreparedStatement ps = c.prepareStatement( 
				"select " +
						"(select count(*) from notification where user_id=?), " +
						"(select count(*) from invitation where (from_user_id=? or to_user_id=?) and " +
							"status_id=(select id from invitation_status where status=?)) " +
						(includeTopicsCount ? ", (select count(*) from topic where user_id=?)" : "")
			);
			ps.setInt( 1, userId );
			ps.setInt( 2, userId );
			ps.setInt( 3, userId );
			ps.setString( 4, InvitationBean.PENDING_INVITATION );
			if( includeTopicsCount )
				ps.setInt( 5, userId );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				MenuBean counts = new MenuBean();
				counts.setNotificationsCount( rs.getInt( 1 ) );
				counts.setInvitationsCount( rs.getInt( 2 ) );
				if( includeTopicsCount )
					counts.setTopicsCount( rs.getInt( 3 ) );
				return counts;
			}
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}		
	}
	
}
