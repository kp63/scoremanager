package bean;

import java.io.Serializable;

public class Superuser extends User implements Serializable {
	private String id;
	private String password;
	private String name;


	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		if (this.name == null) {
			return this.getId();
		}

		return this.name;
	}

	public void setName(String name) {
		this.setId(name);
	}
}
