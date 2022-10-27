package convari.persistence.bean;



public class UserGeneralDataBean implements Bean {

	private String name;
	private String lastname;
	private String sex;
		
	private String website;
	private String imagepath;
	
	public String getImagePath() {
		return imagepath;
	}

	public void setImagePath(String imagepath) {
		this.imagepath = imagepath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getImagepath() {
		return imagepath;
	}

	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}

}
