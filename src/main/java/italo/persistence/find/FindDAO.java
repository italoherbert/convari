package italo.persistence.find;

import italo.persistence.find.parameters.FindParameters;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import convari.persistence.dao.DAOException;


public class FindDAO {
		
	private String controlDateField; 
		
	public ControlDatesFindBean findControlDates( Connection c,	FindParameters findParameters, String[][] fields, String sql ) throws DAOException {
		int pagesCount = findParameters.getPagesCount();
		int limit = findParameters.getLimit();
		int limit2 = this.limitForFindControlDates( findParameters );
		try {		
			String newSql = this.buildSQL( findParameters, fields, sql, limit2 );			
			
			PreparedStatement ps = c.prepareStatement( newSql );									
			ResultSet rs = ps.executeQuery();

			ControlDatesFindBean findBean = new ControlDatesFindBean();
			int index = 1;
			while( rs.next() ) {
				if( (pagesCount != FindParameters.INT_NULL && (index % limit) == 0 ) || rs.isLast() ) {
					if( rs.isLast() ) {
						if( index == limit2 ) {		
							findBean.setHasMore( true );
						} else {
							findBean.getControlDates().add( rs.getTimestamp( 1 ) );
							findBean.setHasMore( false );
						}
					} else findBean.getControlDates().add( rs.getTimestamp( 1 ) );
				}
				index++;
			}		
			return findBean;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
		
	}
	
	public int limitForFindControlDates( FindParameters findParameters ) {
		int limit = findParameters.getLimit();
		int pagesCount = findParameters.getPagesCount();
		int limit2 = FindParameters.INT_NULL;
		if( limit != FindParameters.INT_NULL )
			limit2 = limit;
		if( pagesCount != FindParameters.INT_NULL ) {
			if( limit2 != FindParameters.INT_NULL )
				limit2 *= pagesCount;
			else limit2 = pagesCount;
		}
		if( limit2 != FindParameters.INT_NULL )
			limit2 += 1;
		return limit2;
	}
	
	public String buildSQL( FindParameters findParameters, String[][] fields, String sql ) {
		return this.buildSQL( findParameters, fields, sql, findParameters.getLimit() );
	}
	
	public String buildSQL( FindParameters findParameters, String[][] fields, String sql, int limit ) {
		String newSql = sql;
		String whereSql = this.buildWhere( findParameters, fields );			
			if( newSql.indexOf( ":where" ) > -1 ) {
				if( whereSql.length() > 0 )
					newSql = newSql.replace( ":where", "where "+whereSql );
				else newSql = newSql.replace( ":where", "" );
			} else {
				newSql = newSql.replace( ":conditions", (whereSql.length() > 0 ? whereSql : "true" ) );		
			}
		
		if( limit != FindParameters.INT_NULL )
			newSql = newSql.replace( ":limit", "limit "+limit );
		
		return newSql;
	}
	
	public void printSql( PrintStream out, PreparedStatement ps ) {
		out.println( ps.toString().substring( ps.toString().indexOf(' ')+1 ) ); 
	}
		
	private String buildWhere( FindParameters findParameters, String[][] fields ) {		
		String sql = this.buildQueriesCondition( fields ); 
						
		Timestamp controlDate = findParameters.getControlDate();
		
		if( findParameters.getControlDate() != null ) {
			if( sql.length() > 0 )
				sql = controlDateField + "<" + "'" + controlDate + "'" + " and " + sql;
			else sql = controlDateField + "<" + "'" + controlDate + "'"; 
		}		
		return sql;	
	}	
	
	private String buildQueriesCondition( String[][] fields ) {
		String sql = "";
		int count = 0;
		for( String[] field:fields ) {
			String name = field[0];
			String value = field[1]; 
			if( value != null ) {
				for( String token:value.split("\\s+") ) { 
					if( count > 0 )
						sql += " or ";
					sql += name + " like concat('%" + token + "%')";
					count++;
				}
			}
		}
		return ( count > 1 ? "("+sql+")" : sql );
	}	
		
	public String getControlDateField() {
		return controlDateField;
	}

	public void setControlDateField(String controlDateField) {
		this.controlDateField = controlDateField;
	}

}

