package convari.messages;

import java.util.List;


public interface MessageManager {

	public void processConfigFile() throws MessageManagerException;
		
	public String getMessage( String lang, String key, String... params ) throws MessageManagerException;
	
	public String getHTMLText( String lang, String key, String... params ) throws MessageManagerException;
	
	public List<String> getSupportedLanguages();

	public String getDefaultLang();
	
	public String getLocaleForLang( String lang );
	
}
