package convari.logger;

import convari.persistence.Persistence;
import convari.weblogic.WebLogic;

public interface LoggerDriver {

	public Persistence getPersistence();
	
	public WebLogic getWebLogic();
	
}
