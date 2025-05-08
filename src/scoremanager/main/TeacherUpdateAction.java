package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;
import dao.TeacherDao;
import tool.Action;

/**
 * 教員情報更新アクション
 */
public class TeacherUpdateAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		HttpSession session = req.getSession();
		Teacher loginUser = (Teacher) session.getAttribute("user");
		if (loginUser == null || !"admin".equals(loginUser.getId())) {
			req.getRequestDispatcher("/error.jsp").forward(req, res);
			return;
		}

		// リクエストパラメータを直接取得
		String school_cd   = req.getParameter("school_cd");
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
