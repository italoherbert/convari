package convari.persistence.bean;

import java.util.List;

public class UserBean implements Bean {

	private int id;
	private LoginBean login;
	private UserGeneralDataBean generalData;
	private UserContactDataBean contactData;
	private UserProfessionalDataBean professionalData;
	private UserConfigBean config;
	
	private List<OperationBean> operations;
	private List<InvitationBean> invitations;
	
	public List<OperationBean> getOperations() {
		return operations;
	}

	public void setOperations(List<OperationBean> operations) {
		this.operations = operations;
	}

	public List<InvitationBean> getInvitations() {
		return invitations;
	}

	public void setInvitations(List<InvitationBean> invitations) {
		this.invitations = invitations;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public LoginBean getLogin() {
		return login;
	}

	public void setLogin(LoginBean login) {
		this.login = login;
	}

	public UserGeneralDataBean getGeneralData() {
		return generalData;
	}

	public void setGeneralData(UserGeneralDataBean generalData) {
		this.generalData = generalData;
	}

	public UserContactDataBean getContactData() {
		return contactData;
	}

	public void setContactData(UserContactDataBean contactData) {
		this.contactData = contactData;
	}

	public UserProfessionalDataBean getProfessionalData() {
		return professionalData;
	}

	public void setProfessionalData(UserProfessionalDataBean professionalData) {
		this.professionalData = professionalData;
	}

	public UserConfigBean getConfig() {
		return config;
	}

	public void setConfig(UserConfigBean config) {
		this.config = config;
	}
	
}
