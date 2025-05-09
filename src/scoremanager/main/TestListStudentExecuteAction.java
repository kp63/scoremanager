package scoremanager.main;

import java.util.List;
import javax.servlet.http.*;
import bean.Student;
import bean.TestListStudent;
import bean.Teacher;
import dao.StudentDao;
import dao.TestListStudentDao;
import tool.Action;
import tool.Auth;
import tool.ServletUtil;

public class TestListStudentExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションから教員情報を取得
		Teacher teacher = Auth.getTeacher();
		if (teacher == null) {
			ServletUtil.throwError(req, res, "権限がありません");
			return;
		}

		req.setCharacterEncoding("UTF-8");
		// dropdown 用（SubjectExecuteAction と同じリストを表示したい場合）
		new TestListSubjectExecuteAction().prepareDropdown(req);

		String no = req.getParameter("studentNo");
		if (no == null || no.isEmpty()) {
			req.setAttribute("studentNotFound", true);
		} else {
			Student student = new StudentDao().get(no);
			if (student == null || !student.getSchool().getCd().equals(teacher.getSchool().getCd())) {
				req.setAttribute("studentNotFound", true);
			} else {
				List<TestListStudent> results =
					new TestListStudentDao().filter(student);
				req.setAttribute("studentResults", results);
				req.setAttribute("searchStudent", student);
				if (results.isEmpty()) {
					req.setAttribute("studentResultsNotFound", true);
				}
			}
		}
		req.getRequestDispatcher("test_list.jsp").forward(req, res);
	}
}
