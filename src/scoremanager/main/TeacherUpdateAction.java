package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;
import dao.TeacherDao;
import tool.Action;
import tool.Auth;
import tool.ServletUtil;

/**
 * 教員情報更新アクション
 */
public class TeacherUpdateAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (!Auth.isAuthenticated()) {
			ServletUtil.throwError(req, res, "ログインしてください");
			return;
		}
		// スーパーユーザーか教員かによって、学校コードの取得方法を変える
		// 	管理者教員なら: 教員情報から学校コードを取得
		// 	スーパーユーザーなら: URLから学校コードを取得
		String school_cd;
		if (Auth.isAdminTeacher()) {
			Teacher teacher = Auth.getTeacher();
			school_cd = teacher.getSchool().getCd();
		} else if (Auth.isSuperuser()) {
			school_cd = req.getParameter("cd");
		} else {
			ServletUtil.throwError(req, res, "権限がありません");
			return;
		}

		// リクエストパラメータを直接取得
		String teacher_id  = req.getParameter("teacher_id");
		String name        = req.getParameter("name");
		String password    = req.getParameter("password");

		TeacherDao dao = new TeacherDao();

		// 初回表示：name/password が null → フォーム表示
		if (name == null || password == null) {
			Teacher teacher = dao.get(teacher_id);
			req.setAttribute("teacher", teacher);
			req.setAttribute("school_cd", school_cd);
			req.getRequestDispatcher("teacher_update.jsp").forward(req, res);
			return;
		}

		// POST 更新処理
		Teacher t = new Teacher();
		t.setId(teacher_id);
		t.setName(name);
		t.setPassword(password);
		// school 情報は既存データから再取得
		t.setSchool(dao.get(teacher_id).getSchool());
		dao.save(t);
		HttpSession session = req.getSession();
		Teacher loginUser = (Teacher) session.getAttribute("user");
		// 更新対象がセッションのログインユーザーなら、セッション上のオブジェクトを直接更新
		if (loginUser.getId().equals(teacher_id)) {
			// セッションの Teacher インスタンスを書き換え
			loginUser.setName(name);
			loginUser.setPassword(password);
			// 必要なら学校情報も更新
			loginUser.setSchool(t.getSchool());
			session.setAttribute("user", loginUser);
		}

		// 更新完了メッセージ付きで一覧へリダイレクト
		res.sendRedirect("TeacherList.action?school_cd=" + school_cd + "&msg=updated");
	}
}
