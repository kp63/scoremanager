package scoremanager.main;

import javax.servlet.http.*;
import bean.Teacher;
import tool.Action;

public class TestListAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 認証チェック
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher) session.getAttribute("user");
		if (teacher == null || !teacher.isAuthenticated()) {
			res.sendRedirect(req.getContextPath() + "/index.jsp");
			return;
		}

		// 科目フォームのドロップダウン用データ準備
		new TestListSubjectExecuteAction().prepareDropdown(req);

		// 検索パラメータの有無を確認
		boolean hasSubjectParams = req.getParameter("entYear") != null
			|| req.getParameter("classNum") != null
			|| req.getParameter("subjectCd") != null;
		boolean hasStudentParam  = req.getParameter("studentNo") != null;
		// いずれの検索も未実行ならガイドを表示
		req.setAttribute("showSearchGuide", !(hasSubjectParams || hasStudentParam));

		// JSPへフォワード
		req.getRequestDispatcher("test_list.jsp").forward(req, res);
	}
}
