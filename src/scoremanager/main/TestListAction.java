package scoremanager.main;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.TestListSubject;
import bean.TestListStudent;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestListSubjectDao;
import dao.TestListStudentDao;
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
		School school = teacher.getSchool();

		// ドロップダウン用データを準備
		List<Integer> entYears  = new TestListSubjectDao().findDistinctEntYears(school);
		List<String>  classNums = new ClassNumDao().filter(school);
		List<Subject> subjects  = new SubjectDao().filter(school);

		req.setAttribute("entYears",  entYears);
		req.setAttribute("classNums", classNums);
		req.setAttribute("subjects",  subjects);

		// モード判定
		String mode = req.getParameter("mode");
		if ("subject".equals(mode)) {
			// 科目別検索
			String e   = req.getParameter("entYear");
			String c   = req.getParameter("classNum");
			String sCd = req.getParameter("subjectCd");
			// 入力チェック
			if (e == null || e.isEmpty() || c == null || c.isEmpty() || sCd == null || sCd.isEmpty()) {
				req.setAttribute("subjectError", "入学年度とクラスと科目を選択してください");
			} else {
				int entYear = Integer.parseInt(e);
				Subject subject = new SubjectDao().get(sCd, school);
				List<TestListSubject> subjectResults =
						new TestListSubjectDao().filter(entYear, c, subject, school);
				req.setAttribute("subjectResults", subjectResults);
				req.setAttribute("searchSubject", subject);
				if (subjectResults.isEmpty()) {
					req.setAttribute("subjectNotFound", true);
				}
			}

		} else if ("student".equals(mode)) {
			// 生徒別検索
			String no = req.getParameter("studentNo");
			if (no == null || no.isEmpty()) {
				req.setAttribute("studentNotFound", true);
			} else {
				StudentDao stDao = new StudentDao();
				Student student = stDao.get(no);
				if (student == null || !student.getSchool().getCd().equals(school.getCd())) {
					req.setAttribute("studentNotFound", true);
				} else {
					List<TestListStudent> studentResults =
							new TestListStudentDao().filter(student);
					req.setAttribute("searchStudent", student);
					req.setAttribute("studentResults", studentResults);
					if (studentResults.isEmpty()) {
						req.setAttribute("studentResultsNotFound", true);
					}
				}
			}
		}

		// JSP へフォワード
		req.getRequestDispatcher("test_list.jsp")
				.forward(req, res);
	}
}
