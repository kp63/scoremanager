package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;
import tool.Auth;
import tool.ServletUtil;

/**
 * 処理を実行するアクションクラス
 *
 * このクラスは以下の役割を持ちます:
 * - ログインフォームから送信されたID/パスワードの認証
 * - 認証成功時のセッション管理とメニュー画面へのリダイレクト
 * - 認証失敗時のエラー処理
 */
public class ClassListAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションから教員情報を取得
		Teacher teacher = Auth.getTeacher();
		if (teacher == null) {
			ServletUtil.throwError(req, res, "権限がありません");
			return;
		}

		// DAOの呼び出し
		ClassNumDao cNumDao = new ClassNumDao();

		// 先生が所属する学校のクラス番号一覧を取得
		List<String> classNumsSet = cNumDao.filter(teacher.getSchool());

		// レスポンス値を設定
		// クラス番号一覧
		req.setAttribute("class_num_set", classNumsSet);
		// 学校名
		req.setAttribute("school_name", teacher.getSchool().getName());
		// 学校コード
		req.setAttribute("school_cd", teacher.getSchool().getCd());

		// JSPにフォワード
		req.getRequestDispatcher("class_list.jsp").forward(req, res);

	}
}
