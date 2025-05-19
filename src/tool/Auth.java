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


	//////////////////////////////////
	/// 基本メソッド
	//////////////////////////////////

	/**
	 * 認証されたユーザーを取得するメソッド
	 * @return User 認証されたユーザー、認証されていない場合はnull
	 * @throws Exception
	 */
	public static User getUser() {
		User user = (User) req.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}

		return user;
	}

	/**
	 * 認証されたユーザーのタイプを取得するメソッド
	 * @return String "teacher" | "superuser" | null
	 * @throws Exception
	 */
	public static String getUserType() {
		String userType = (String) req.getSession().getAttribute("user_type");
		if (userType == null) {
			return null;
		}

		return userType;
	}

	/**
	 * 認証されているかどうかを確認するメソッド
	 * @return boolean 認証されている場合はtrue、そうでない場合はfalse
	 */
	public static boolean isAuthenticated() {
		// セッションからユーザー情報を取得し、認証されているかどうかをチェック
		String userType = getUserType();
		User user = getUser();
		if (userType == null || user == null) {
			return false;
		}

		// 認証されているかどうかをチェック
		return user.isAuthenticated();
	}

	/**
	 * 認証されたユーザーのIDを取得するメソッド
	 * @return String ユーザーのID、認証されていない場合はnull
	 * @throws Exception
	 */
	public static String getUserId() {
		if (!isAuthenticated()) {
			return null;
		}

		User user = (User) getUser();
		if (user instanceof Teacher) {
			return ((Teacher) user).getId();
		} else if (user instanceof Superuser) {
			return ((Superuser) user).getId();
		}

		return null;
	}


	//////////////////////////////////
	/// 応用isメソッド
	//////////////////////////////////

	/**
	 * 教員かどうかを確認するメソッド
	 * @return boolean 教員の場合はtrue、そうでない場合はfalse
	 */
	public static boolean isTeacher() {
		if (!isAuthenticated()) {
			return false;
		}

		return "teacher".equals(getUserType());
	}

	/**
	 * 教員が管理者権限を持っているかどうかを確認するメソッド
	 * @return boolean 管理者権限を持っている場合はtrue、そうでない場合はfalse
	 */
	public static boolean isAdminTeacher() {
		if (!isTeacher()) {
			return false;
		}

		Teacher teacher = getTeacher();
		if (teacher == null) {
			return false;
		}

		return "admin".equals(teacher.getRole());
	}

	/**
	 * スーパーユーザーかどうかを確認するメソッド
	 * @return boolean スーパーユーザーの場合はtrue、そうでない場合はfalse
	 */
	public static boolean isSuperuser() {
		if (!isAuthenticated()) {
			return false;
		}

		return "superuser".equals(getUserType());
	}


	//////////////////////////////////
	/// 応用メソッド
	//////////////////////////////////

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
	public static Superuser getSuperuser() {
		if (!isSuperuser()) {
			return null;
		}

		return (Superuser) req.getSession().getAttribute("user");
	}

}
