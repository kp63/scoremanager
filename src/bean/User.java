package bean;

public class User {
	/**
	 * ユーザーの認証状態を表すフラグ
	 */
	private boolean isAuthenticated;

	public boolean isAuthenticated() {
		return this.isAuthenticated;
	}
	public void setAuthenticated(boolean flg) {
		this.isAuthenticated = flg;
	}

}
