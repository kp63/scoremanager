package tool;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;

// *.actionで終わるURLパターンに対してマッピング
@WebServlet(urlPatterns = { "*.action" })
public class FrontController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			// リクエストURLからコンテキストパスを除いたパスを取得
			// Login.action → scoremanager/Login.action
			String path = req.getServletPath().substring(1);

			// パスをJavaクラス名の形式に変換
			// 例: scoremanager/Login.action → scoremanager.LoginAction
			String name = path.replace(".action", "Action").replace('/', '.');

			// scoremanager.main配下は認証が必須
			if (name.startsWith("scoremanager.main")) {
				// ログインされていない場合はログイン画面へリダイレクト
				User user = (User) req.getSession().getAttribute("user");
				if (user == null || !user.isAuthenticated()) {
					res.sendRedirect(req.getContextPath() + "/scoremanager/index.jsp");
					return;
				}
			}

			 // 変換したクラス名を使用してアクションクラスのインスタンスを動的に生成
			Action action = (Action) Class.forName(name).getDeclaredConstructor().newInstance();
			// アクションクラスのexecuteメソッドを実行
			// 各アクションクラスで定義された処理を実行
			action.execute(req, res);
		} catch (ClassNotFoundException e) {
			// クラスが見つからない場合は404エラーを返す
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (NoSuchMethodException e) {
			// executeメソッドが見つからない場合は500エラーを返す
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			// 例外発生時はスタックトレースを出力
			e.printStackTrace();
			// エラーページ(/error.jsp)へフォワード
			req.getRequestDispatcher("/error.jsp").forward(req, res);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}

}
