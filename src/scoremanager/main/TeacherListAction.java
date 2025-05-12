package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Teacher;
import dao.SchoolDao;
import dao.TeacherDao;
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
public class TeacherListAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// 学校管理者からアクセスしたときに受け取る学校コード
		String cd = req.getParameter("cd");

		// セッションから先生の情報を取得
		Teacher teacher = Auth.getTeacher();

		// 学校管理者の場合
		if (Auth.isSuperuser()) {

			// DAOの呼び出し
			TeacherDao tDao = new TeacherDao();
			SchoolDao sDao = new SchoolDao();

			// 受け取った学校コードの学校の教員一覧を取得
			List<Teacher> teacherSet = tDao.filter(sDao.get(cd));

			// レスポンス値を設定
			// 教員データ一覧
			req.setAttribute("teacher_set", teacherSet);
			// 学校名
			req.setAttribute("school_name", sDao.get(cd).getName());
			// 学校コード
			req.setAttribute("school_cd", cd);

			// JSPにフォワード
			req.getRequestDispatcher("teacher_list.jsp").forward(req, res);

		// 教員管理者の場合
		} else if(Auth.isAdminTeacher()) {

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

		// それ以外の場合
		} else {
			ServletUtil.throwError(req, res, "権限がありません");
			return;
		}
	}
}
