package convari.persistence.bean;

public class UserConfigBean implements Bean {

	private String enderVisibility;
	private String telVisibility;
	private String mailVisibility;

	public String getEnderVisibility() {
		return enderVisibility;
	}

	public void setEnderVisibility(String enderVisibility) {
		this.enderVisibility = enderVisibility;
	}

	public String getTelVisibility() {
		return telVisibility;
	}

	public void setTelVisibility(String telVisibility) {
		this.telVisibility = telVisibility;
	}

	public String getMailVisibility() {
		return mailVisibility;
	}

	public void setMailVisibility(String mailVisibility) {
		this.mailVisibility = mailVisibility;
	}

}
