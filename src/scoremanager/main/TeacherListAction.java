package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;
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
public class TeacherListAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションから先生の情報を取得
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher) session.getAttribute("user");

		if (teacher == null || !"admin".equals(teacher.getId())) {
			req.getRequestDispatcher("/error.jsp").forward(req, res);
			return;
		}

		// DAOの呼び出し
		TeacherDao tDao = new TeacherDao();

		// 先生が所属する学校の教員一覧を取得
		List<Teacher> teacherSet = tDao.filter(teacher.getSchool());

		// レスポンス値を設定
		// 教員データ一覧
		req.setAttribute("teacher_set", teacherSet);
		// 学校名
		req.setAttribute("school_name", teacher.getSchool().getName());
		// 学校コード
		req.setAttribute("school_cd", teacher.getSchool().getCd());

		// JSPにフォワード
		req.getRequestDispatcher("teacher_list.jsp").forward(req, res);

	}
}