package italo.persistence.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBCommand {

	public void execute( Connection connection ) throws SQLException;
	
}
