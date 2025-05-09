package scoremanager;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Superuser;
import bean.Teacher;
import dao.SuperuserDao;
import dao.TeacherDao;
import tool.Action;

/**
 * 処理を実行するアクションクラス
 *
 * このクラスは以下の役割を持ちます:
 * - ログインフォームから送信されたID/パスワードの認証
 * - 認証成功時のセッション管理とメニュー画面へのリダイレクト
 * - 認証失敗時のエラー処理
 */
public class LoginExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String id = "";
		String password = "";

		// ログインフォームから送信されたパラメータを取得
		id = req.getParameter("id");
		password = req.getParameter("password");

		// 教員IDとパスワードで認証を実行
		TeacherDao teacherDao = new TeacherDao();
		Teacher teacher = teacherDao.login(id, password);
		if (teacher != null) {
			// セッションの取得または開始
			HttpSession session = req.getSession(true);
			// 教員情報に認証済みフラグを設定
			teacher.setAuthenticated(true);
			// セッションに認証済み教員情報を保存
			session.setAttribute("user", teacher);
			session.setAttribute("user_type", "teacher");
			// メニュー画面へリダイレクト
			res.sendRedirect("main/Menu.action");
			return;
		}

		// スーパーユーザーIDとパスワードで認証を実行
		SuperuserDao superuserDao = new SuperuserDao();
		Superuser superuser = superuserDao.login(id, password);
		if (superuser != null) {
			// セッションの取得または開始
			HttpSession session = req.getSession(true);
			// スーパーユーザー情報に認証済みフラグを設定
			superuser.setAuthenticated(true);
			// セッションに認証済みスーパーユーザー情報を保存
			session.setAttribute("user", superuser);
			session.setAttribute("user_type", "superuser");
			// メニュー画面へリダイレクト
			res.sendRedirect("main/Menu.action");
			return;
		}

		// ログイン失敗時の処理

		// エラーメッセージの構築
		List<String> errors = new ArrayList<>();
		errors.add("IDまたはパスワードが確認できませんでした");
		req.setAttribute("errors", errors);

		// 教員IDを再入力する手間を省くため、リクエスト属性に教員IDを保持
		req.setAttribute("id", id);

		// ログイン画面に戻す
		req.getRequestDispatcher("login.jsp").forward(req, res);
	}
}
