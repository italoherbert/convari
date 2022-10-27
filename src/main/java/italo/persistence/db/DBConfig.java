package italo.persistence.db;

public interface DBConfig {

	public String getDataSourceURL();

	public void setDataSourceURL(String dataSourceName);

	public boolean isDataSourceConnection();

	public void setDataSourceConnection(boolean dataSourceConnection);
	
	public String getDriver();

	public String getUrl();
	
	public String getUsername();

	public String getPassword();

	public void setDriver(String driver);

	public void setUrl(String url);

	public void setUsername(String username);

	public void setPassword(String password);
	
}
