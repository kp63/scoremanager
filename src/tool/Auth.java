package tool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import bean.Superuser;
import bean.Teacher;
import bean.User;

public class Auth {
	private static HttpServletRequest req = null;

	public static void _setRequest(HttpServletRequest request) {
		req = request;
	}

	public static String getLoginUrl() {
		return req.getContextPath() + "/login.jsp";
	}

	/**
	 * 認証されているかどうかを確認するメソッド
	 * @return boolean 認証されている場合はtrue、そうでない場合はfalse
	 */
	public static boolean isAuthenticated() {
		// セッションの取得
		HttpSession session = req.getSession();

		// ユーザータイプがnullの場合は認証されていないとみなす
		String userType = (String) session.getAttribute("user_type");
		if (userType == null) {
			return false;
		}

		// セッションからユーザー情報を取得し、が認証されているかどうかをチェック
		User user = (User) session.getAttribute("user");
		return user != null && user.isAuthenticated();
	}

	/**
	 * 教員かどうかを確認するメソッド
	 * @return boolean 教員の場合はtrue、そうでない場合はfalse
	 */
	public static boolean isTeacher() {
		if (!isAuthenticated()) {
			return false;
		}

		String userType = (String) req.getSession().getAttribute("user_type");
		if (userType == null) {
			return false;
		}

		if ("teacher".equals(userType)) {
			return true;
		}

		return false;
	}

	/**
	 * 教員が管理者権限を持っているかどうかを確認するメソッド
	 * @return boolean 管理者権限を持っている場合はtrue、そうでない場合はfalse
	 */
	public static boolean isAdminTeacher() {
		if (!isTeacher()) {
			return false;
		}

		// セッションから教員情報を取得し、管理者権限を持っているかどうかをチェック
		Teacher user = (Teacher) req.getSession().getAttribute("user");
		return user != null && "admin".equals(user.getRole());
	}

	/**
	 * スーパーユーザーかどうかを確認するメソッド
	 * @return boolean スーパーユーザーの場合はtrue、そうでない場合はfalse
	 */
	public static boolean isSuperuser() {
		if (!isAuthenticated()) {
			return false;
		}

		String userType = (String) req.getSession().getAttribute("user_type");
		if (userType == null) {
			return false;
		}

		if ("superuser".equals(userType)) {
			return true;
		}

		return false;
	}

	/**
	 * 教員情報を取得するメソッド
	 * @return Teacher 教員情報、認証されていない場合または教員でない場合はnull
	 * @throws Exception
	 */
	public static Teacher getTeacher() {
		if (!isTeacher()) {
			return null;
		}

		return (Teacher) req.getSession().getAttribute("user");
	}

	/**
	 * スーパーユーザー情報を取得するメソッド
	 * @return Superuser スーパーユーザー情報、認証されていない場合またはスーパーユーザーでない場合はnull
	 * @throws Exception
	 */
	public static Superuser getSuperuser() throws Exception {
		if (!isSuperuser()) {
			return null;
		}

		return (Superuser) req.getSession().getAttribute("user");
	}

	public static void guard(String role) throws Exception {
		if (role == null) {
			return;
		}

		if (!Auth.isAuthenticated()) {
			throw new Exception("認証してください");
		}
	}
}
