package bean;

import java.io.Serializable;

public class Teacher extends User implements Serializable {
	private String id;
	private String password;
	private String name;
	private School school;
	private String role;

	public String getId() {
		return this.id;
	}
	public String getPassword() {
		return this.password;
	}
	public String getName() {
		return this.name;
	}
	public School getSchool() {
		return this.school;
	}
	public String getRole() {
		return this.role;
	}

	public void setId(String id) {
		this.id = id;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	public void setRole(String role) {
		this.role = role;
	}

}
