package scoremanager.main;

import java.util.List;
import javax.servlet.http.*;
import bean.School;
import bean.Subject;
import bean.TestListSubject;
import bean.Teacher;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestListSubjectDao;
import tool.Action;
import tool.Auth;
import tool.ServletUtil;

public class TestListSubjectExecuteAction extends Action {
	/** dropdown 用データを req にセット */
	public void prepareDropdown(HttpServletRequest req) throws Exception {
		HttpSession session = req.getSession();
		School school = ((Teacher)session.getAttribute("user")).getSchool();
		req.setAttribute("entYears", new TestListSubjectDao().findDistinctEntYears(school));
		req.setAttribute("classNums", new ClassNumDao().filter(school));
		req.setAttribute("subjects", new SubjectDao().filter(school));
	}

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションから教員情報を取得
		Teacher teacher = Auth.getTeacher();
		if (teacher == null) {
			ServletUtil.throwError(req, res, "権限がありません");
			return;
		}

		req.setCharacterEncoding("UTF-8");
		prepareDropdown(req);

		String e   = req.getParameter("entYear");
		String c   = req.getParameter("classNum");
		String sCd = req.getParameter("subjectCd");
		if (e == null || e.isEmpty() || c == null || c.isEmpty() || sCd == null || sCd.isEmpty()) {
			req.setAttribute("subjectError", "入学年度・クラス・科目を選択してください");
		} else {
			int entYear = Integer.parseInt(e);
			School school = teacher.getSchool();
			Subject subject = new SubjectDao().get(sCd, school);
			List<TestListSubject> results =
				new TestListSubjectDao().filter(entYear, c, subject, school);
			req.setAttribute("subjectResults", results);
			req.setAttribute("searchSubject", subject);
			if (results.isEmpty()) {
				req.setAttribute("subjectNotFound", true);
			}
		}
		req.getRequestDispatcher("test_list.jsp").forward(req, res);
	}
}
