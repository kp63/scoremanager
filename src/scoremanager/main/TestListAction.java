package scoremanager.main;

import javax.servlet.http.*;
import bean.Teacher;
import tool.Action;

public class TestListAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 初期アクセス：認証チェックのみ
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher) session.getAttribute("user");
		if (teacher == null || !teacher.isAuthenticated()) {
			res.sendRedirect(req.getContextPath() + "/index.jsp");
			return;
		}
		// Data for subject form (drop‑downs)
		new TestListSubjectExecuteAction().prepareDropdown(req);
		// forward to master page
		req.getRequestDispatcher("test_list.jsp").forward(req, res);
	}
}
