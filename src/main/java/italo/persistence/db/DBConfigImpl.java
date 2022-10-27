package italo.persistence.db;


public class DBConfigImpl implements DBConfig {

	private String driver;
	private String url;
	private String username;
	private String password;
	private String dataSourceURL;
	private boolean dataSourceConnection = true;
	private boolean autoCommit = true;

	public DBConfigImpl() {}
	
	public DBConfigImpl(String driver, String url, String username,	String password, boolean autoCommit) {
		super();
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
		this.autoCommit = autoCommit;
	}

	public String getDataSourceURL() {
		return dataSourceURL;
	}

	public void setDataSourceURL(String dataSourceName) {
		this.dataSourceURL = dataSourceName;
	}

	public boolean isDataSourceConnection() {
		return dataSourceConnection;
	}

	public void setDataSourceConnection(boolean dataSourceConnection) {
		this.dataSourceConnection = dataSourceConnection;
	}

	public String getDriver() {
		return driver;
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}
	
}
