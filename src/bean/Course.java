package bean;

import java.io.Serializable;

public class Course implements Serializable {
	private int id;
	private String name;

	public int getId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}

	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}

}
