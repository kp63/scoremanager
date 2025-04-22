package scoremanager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tool.Action;

/**
 * ログインアクション
 *
 * このクラスは以下の役割を持ちます:
 * - ログインページの初期表示を担当
 * - ログインフォームを表示するJSPへフォワード
 */
public class LoginAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// ログインページ(login.jsp)へフォワード
		req.getRequestDispatcher("login.jsp").forward(req, res);
	}
}
