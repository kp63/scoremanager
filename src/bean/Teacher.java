package bean;

import java.io.Serializable;

public class Teacher extends User implements Serializable {
	private String id;
	private String password;
	private String name;
	private School school;
	/**
	 * 教員のロール
	 * default: 教員, admin: 管理者
	 */
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

	/**
	 * 教員のロールを取得する
	 * 戻り値は("default" | "admin" | null)
	 * @return 教員のロール
	 * (default: 一般教員, admin: 管理者教員)
	 */
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

	/**
	 * 教員のロールを設定する
	 * @param role 教員のロール
	 * (default: 一般教員, admin: 管理者教員)
	 */
	public void setRole(String role) {
		this.role = role;
	}

}
