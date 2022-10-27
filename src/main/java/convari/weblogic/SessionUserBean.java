package convari.weblogic;

import java.util.List;

import convari.persistence.bean.OperationBean;
import convari.upload.ImageUploadProcess;




public class SessionUserBean  {

	private int uid = -1;
	private String username;
	private List<OperationBean> operations;
	private boolean absent = false;
	
	private int loginSuccessLogId = -1;
	private ImageUploadProcess imageUploadProcess = new ImageUploadProcess();

	public SessionUserBean( int uid, String username ) {
		super();
		this.uid = uid;
		this.username = username;
	}

	public int getUID() {
		return uid;
	}

	public void setUID(int uid) {
		this.uid = uid;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public boolean isAbsent() {
		return absent;
	}

	public void setAbsent(boolean absent) {
		this.absent = absent;
	}

	public List<OperationBean> getOperations() {
		return operations;
	}

	public void setOperations(List<OperationBean> operations) {
		this.operations = operations;
	}

	public int getLoginSuccessLogId() {
		return loginSuccessLogId;
	}

	public void setLoginSuccessLogId(int loginSuccessLogId) {
		this.loginSuccessLogId = loginSuccessLogId;
	}

	public ImageUploadProcess getImageUploadProcess() {
		return imageUploadProcess;
	}
	
}
