package convari.response;

public class ResponseMessage {

	public final static String INFO_TYPE = "info";
	public final static String ERROR_TYPE = "error";
	
	private String key;
	private String text;
	private String[] params;
	private String type;
	
	public ResponseMessage(String type, String text, String key, String... params) {
		super();
		this.type = type;
		this.text = text;
		this.key = key;
		this.params = params;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String... params) {
		this.params = params;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
