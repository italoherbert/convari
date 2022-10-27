package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import convari.persistence.bean.InvitationBean;


public class InvitationDAO {
	
	public void insert( Connection c, int uid1, int uid2, String status ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"insert into invitation ( from_user_id, to_user_id, status_id ) " +
				"values (?, ?, " +
					"(select id from invitation_status where status=?)" +
				")" 
			);
			ps.setInt( 1, uid1 );
			ps.setInt( 2, uid2 );
			ps.setString( 3, status );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
			
	public void delete( Connection c, int uid1, int uid2 ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"delete from invitation " +
				"where (from_user_id=? and to_user_id=?) or (from_user_id=? and to_user_id=?)" 
			);
			ps.setInt( 1, uid1 );
			ps.setInt( 2, uid2 );
			ps.setInt( 3, uid2 );
			ps.setInt( 4, uid1 );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}		
	
	public void updateStatus( Connection c, int uid1, int uid2, String status ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement(
				"update invitation set " +
				( status.equals( InvitationBean.PENDING_INVITATION ) 
						? "send_date=current_timestamp, response_date=timestamp(0), "  
						: "response_date=current_timestamp, " ) +
				"status_id=(select id from invitation_status where status=?) " +				
				"where (from_user_id=? and to_user_id=?) or (from_user_id=? and to_user_id=?)"		
			);
			ps.setString( 1, status );
			ps.setInt( 2, uid1 );
			ps.setInt( 3, uid2 );
			ps.setInt( 4, uid2 );
			ps.setInt( 5, uid1 );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
						
	public boolean added( Connection c, int uid1, int uid2 ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select id from invitation where (from_user_id=? and to_user_id=?) or (from_user_id=? and to_user_id=?)" 
			);
			ps.setInt( 1, uid1 );
			ps.setInt( 2, uid2 );
			ps.setInt( 3, uid2 );
			ps.setInt( 4, uid1 );
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public String status( Connection c, int uid1, int uid2 ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select ist.status " +
				"from invitation_status ist inner join invitation i on ist.id=i.status_id " +
				"where (i.from_user_id=? and i.to_user_id=?) or " +
						"(i.from_user_id=? and i.to_user_id=?) " +
				"limit 1" 
			);
			ps.setInt( 1, uid1 );
			ps.setInt( 2, uid2 );
			ps.setInt( 3, uid2 );
			ps.setInt( 4, uid1 );			
			ResultSet rs = ps.executeQuery();
			if( rs.next() )
				return rs.getString( 1 );
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public List<InvitationBean> listAcceptedOrRemoved( Connection c, int fromUserId, Timestamp date ) throws DAOException {
		try {
			List<InvitationBean> invitations = new ArrayList<InvitationBean>();
			PreparedStatement ps = c.prepareStatement( 
				"select i.id, i.from_user_id, i.to_user_id, i.send_date, i.response_date, ist.status " +
				"from invitation_status ist inner join invitation i on ist.id=i.status_id " +
				"where (i.from_user_id=? or i.to_user_id=?) and (ist.status=? or ist.status=?) and i.response_date>? " +
				"order by i.response_date desc" 
			);
			ps.setInt( 1, fromUserId );
			ps.setInt( 2, fromUserId );
			ps.setString( 3, InvitationBean.ACCEPTED_INVITATION );
			ps.setString( 4, InvitationBean.REMOVED_INVITATION );
			ps.setTimestamp( 5, date );
			ResultSet rs = ps.executeQuery();
			while( rs.next() ) {
				InvitationBean invitation = new InvitationBean();
				invitation.setId( rs.getInt( 1 ) );				
				invitation.setFromUserId( rs.getInt( 2 ) );
				invitation.setToUserId( rs.getInt( 3 ) ); 
				invitation.setSendDate( rs.getTimestamp( 4 ) );
				invitation.setResponseDate( rs.getTimestamp( 5 ) );
				invitation.setStatus( rs.getString( 6 ) ); 
				invitations.add( invitation );
			}
			return invitations;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
		
	public InvitationBean find( Connection c, int uid1, int uid2 ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select i.id, i.from_user_id, i.to_user_id, ist.status, i.send_date, i.response_date " +
				"from invitation_status ist inner join invitation i on ist.id=i.status_id " +
				"where (from_user_id=? and to_user_id=?) or (from_user_id=? and to_user_id=?) " +
				"limit 1"		
			);
			ps.setInt( 1, uid1 );
			ps.setInt( 2, uid2 );
			ps.setInt( 3, uid2 );
			ps.setInt( 4, uid1 );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {				
				InvitationBean invitation = new InvitationBean();
				invitation.setId( rs.getInt( 1 ) );
				invitation.setFromUserId( rs.getInt( 2 ) ); 
				invitation.setToUserId( rs.getInt( 3 ) );
				invitation.setStatus( rs.getString( 4 ) );
				invitation.setSendDate( rs.getTimestamp( 5 ) );
				invitation.setResponseDate( rs.getTimestamp( 6 ) );
				return invitation;
			}
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public int countForUserByStatus( Connection c, int uid, String status ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select count(*) " +
				"from invitation_status ist inner join invitation i on ist.id=i.status_id " +
				"where (i.from_user_id=? or i.to_user_id=?) and ist.status=? "
			);
			ps.setInt( 1, uid );
			ps.setInt( 2, uid );
			ps.setString( 3, status );
			ResultSet rs = ps.executeQuery();
			if( rs.next() )
				return rs.getInt( 1 );			
			return 0;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public List<InvitationBean> listForUser( Connection c, int uid ) throws DAOException {
		try {
			List<InvitationBean> invitations = new ArrayList<InvitationBean>();
			PreparedStatement ps = c.prepareStatement( 
				"select i.id, i.from_user_id, i.to_user_id, i.send_date, i.response_date, ist.status " +
				"from invitation_status ist inner join invitation i on ist.id=i.status_id " +
				"where i.from_user_id=? or i.to_user_id=? " +
				"order by i.response_date desc, send_date desc " 
			);
			ps.setInt( 1, uid );
			ps.setInt( 2, uid );
			ResultSet rs = ps.executeQuery();
			while( rs.next() ) {
				InvitationBean invitation = new InvitationBean();
				invitation.setId( rs.getInt( 1 ) );				
				invitation.setFromUserId( rs.getInt( 2 ) );
				invitation.setToUserId( rs.getInt( 3 ) ); 
				invitation.setSendDate( rs.getTimestamp( 4 ) );
				invitation.setResponseDate( rs.getTimestamp( 5 ) );
				invitation.setStatus( rs.getString( 6 ) ); 
				invitations.add( invitation );
			}
			return invitations;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public List<InvitationBean> listForUser( Connection c, int uid, Timestamp cControlDate, Timestamp iControlDate, boolean contactsOnly ) throws DAOException {
		try {
			List<InvitationBean> invitations = new ArrayList<InvitationBean>();
			PreparedStatement ps = c.prepareStatement( 
				"select i.id, i.from_user_id, i.to_user_id, i.send_date, i.response_date, ist.status " +
				"from invitation_status ist inner join invitation i on ist.id=i.status_id " +
				"where (i.from_user_id=? or i.to_user_id=?) " +
							(contactsOnly ? "and (ist.status=? or ist.status=?) " : "" ) +
							"and if( ist.status=?, i.send_date, i.response_date) > if( ist.status in (?, ?, ?), ?, ? ) " +
				"order by ist.status=? desc, if( ist.status=?, i.send_date, i.response_date) desc" 
			);
			int index = 0;
			ps.setInt( ++index, uid );
			ps.setInt( ++index, uid );

			if( contactsOnly ) {
				ps.setString( ++index, InvitationBean.ACCEPTED_INVITATION );
				ps.setString( ++index, InvitationBean.REFUSED_INVITATION );
			}
			
			ps.setString( ++index, InvitationBean.PENDING_INVITATION );
			
			ps.setString( ++index, InvitationBean.PENDING_INVITATION );
			ps.setString( ++index, InvitationBean.CANCELED_INVITATION );
			ps.setString( ++index, InvitationBean.REFUSED_INVITATION );
			
			ps.setTimestamp( ++index, ( iControlDate != null  ? iControlDate : new Timestamp(0) ) ); 			
			ps.setTimestamp( ++index, ( cControlDate != null ? cControlDate : new Timestamp(0) ) ); 			
			
			ps.setString( ++index, InvitationBean.PENDING_INVITATION );
			ps.setString( ++index, InvitationBean.PENDING_INVITATION );
			
			ResultSet rs = ps.executeQuery();
			while( rs.next() ) {
				InvitationBean invitation = new InvitationBean();
				invitation.setId( rs.getInt( 1 ) );				
				invitation.setFromUserId( rs.getInt( 2 ) );
				invitation.setToUserId( rs.getInt( 3 ) ); 
				invitation.setSendDate( rs.getTimestamp( 4 ) );
				invitation.setResponseDate( rs.getTimestamp( 5 ) );
				invitation.setStatus( rs.getString( 6 ) ); 
				invitations.add( invitation );
			}
			return invitations;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public List<InvitationBean> listForUserByStatus( Connection c, int uid, String status ) throws DAOException {
		try {
			List<InvitationBean> invitations = new ArrayList<InvitationBean>();
			PreparedStatement ps = c.prepareStatement( 
				"select i.id, i.from_user_id, i.to_user_id, i.send_date, i.response_date " +
				"from invitation_status ist inner join invitation i on ist.id=i.status_id " +
				"where (i.from_user_id=? or i.to_user_id=?) and ist.status=? " +
				"order by i.response_date desc, send_date desc " 
			);
			ps.setInt( 1, uid );
			ps.setInt( 2, uid );
			ps.setString( 3, status );
			ResultSet rs = ps.executeQuery();
			while( rs.next() ) {
				InvitationBean invitation = new InvitationBean();
				invitation.setId( rs.getInt( 1 ) );				
				invitation.setFromUserId( rs.getInt( 2 ) );
				invitation.setToUserId( rs.getInt( 3 ) ); 
				invitation.setStatus( status );
				invitation.setSendDate( rs.getTimestamp( 4 ) );
				invitation.setResponseDate( rs.getTimestamp( 5 ) );
				invitations.add( invitation );
			}
			return invitations;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public List<InvitationBean> listForUserByPendingStatus( Connection c, int uid, Timestamp date ) throws DAOException {
		try {
			List<InvitationBean> invitations = new ArrayList<InvitationBean>();
			PreparedStatement ps = c.prepareStatement( 
				"select i.id, i.from_user_id, i.to_user_id, i.send_date, i.response_date " +
				"from invitation_status ist inner join invitation i on ist.id=i.status_id " +
				"where (i.from_user_id=? or i.to_user_id=?) and ist.status=? and i.send_date>? " +
				"order by i.send_date desc " 
			);
			ps.setInt( 1, uid );
			ps.setInt( 2, uid );
			ps.setString( 3, InvitationBean.PENDING_INVITATION );
			ps.setTimestamp( 4, date );
			ResultSet rs = ps.executeQuery();
			while( rs.next() ) {
				InvitationBean invitation = new InvitationBean();
				invitation.setId( rs.getInt( 1 ) );				
				invitation.setFromUserId( rs.getInt( 2 ) );
				invitation.setToUserId( rs.getInt( 3 ) ); 
				invitation.setStatus( InvitationBean.PENDING_INVITATION );
				invitation.setSendDate( rs.getTimestamp( 4 ) );
				invitation.setResponseDate( rs.getTimestamp( 5 ) );
				invitations.add( invitation );
			}
			return invitations;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public List<InvitationBean> listForFromUserByStatus( Connection c, int fromUserId, String status ) throws DAOException {
		try {
			List<InvitationBean> invitations = new ArrayList<InvitationBean>();
			PreparedStatement ps = c.prepareStatement( 
				"select i.id, i.to_user_id, i.send_date, i.response_date " +
				"from invitation_status ist inner join invitation i on ist.id=i.status_id " +
				"where i.from_user_id=? and ist.status=? " +
				"order by i.response_date desc" 
			);
			ps.setInt( 1, fromUserId );
			ps.setString( 2, status );
			ResultSet rs = ps.executeQuery();
			while( rs.next() ) {
				InvitationBean invitation = new InvitationBean();
				invitation.setId( rs.getInt( 1 ) );				
				invitation.setFromUserId( fromUserId );
				invitation.setToUserId( rs.getInt( 2 ) ); 
				invitation.setStatus( status );
				invitation.setSendDate( rs.getTimestamp( 3 ) );
				invitation.setResponseDate( rs.getTimestamp( 4 ) );
				invitations.add( invitation );
			}
			return invitations;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public List<InvitationBean> listForToUserByStatus( Connection c, int toUserId, String status ) throws DAOException {
		try {
			List<InvitationBean> invitations = new ArrayList<InvitationBean>();
			PreparedStatement ps = c.prepareStatement( 
				"select i.id, i.from_user_id, i.send_date, i.response_date " +
				"from invitation_status ist inner join invitation i on ist.id=i.status_id " +
				"where i.to_user_id=? and ist.status=?" 
			);
			ps.setInt( 1, toUserId );
			ps.setString( 2, status );
			ResultSet rs = ps.executeQuery();
			while( rs.next() ) {
				InvitationBean invitation = new InvitationBean();
				invitation.setId( rs.getInt( 1 ) );				
				invitation.setFromUserId( rs.getInt( 2 ) );
				invitation.setToUserId( toUserId ); 
				invitation.setStatus( status );
				invitation.setSendDate( rs.getTimestamp( 3 ) );
				invitation.setResponseDate( rs.getTimestamp( 4 ) );
				invitations.add( invitation );
			}
			return invitations;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
					
}
