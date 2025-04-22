package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectListAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションから先生の情報を取得
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher) session.getAttribute("user");

		// 科目リストを取得
		SubjectDao subjectDao = new SubjectDao();
		List<Subject> subjects = subjectDao.filter(teacher.getSchool());

		req.setAttribute("subjects", subjects);

		// JSPにフォワード
		req.getRequestDispatcher("subject_list.jsp").forward(req, res);

	}
}
