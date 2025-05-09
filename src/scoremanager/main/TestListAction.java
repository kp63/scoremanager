package scoremanager.main;

import javax.servlet.http.*;
import bean.Teacher;
import tool.Action;
import tool.Auth;
import tool.ServletUtil;

public class TestListAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションから教員情報を取得
		Teacher teacher = Auth.getTeacher();
		if (teacher == null) {
			ServletUtil.throwError(req, res, "権限がありません");
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
