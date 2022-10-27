package convari.persistence.bean;


public class UserProfessionalDataBean implements Bean {

	private String occupation;
	private String academic;

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getAcademic() {
		return academic;
	}

	public void setAcademic(String academic) {
		this.academic = academic;
	}
	
}
