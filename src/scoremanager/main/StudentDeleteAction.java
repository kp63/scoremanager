package scoremanager.main;

import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class StudentDeleteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションから先生の情報を取得
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher) session.getAttribute("user");

		// 学生情報を取得
		String studentNo = req.getParameter("no");
		if (studentNo == null || studentNo.equals("")) {
			res.sendRedirect("StudentList.action");
			return;
		}
		StudentDao sDao = new StudentDao();
		Student student = sDao.get(studentNo);
		if (student == null) {
			req.getRequestDispatcher("/error.jsp").forward(req, res);
			return;
		}

		// 学生の学校情報と先生の学校情報が一致するか確認
		if (
			student.getSchool() == null
			|| !student.getSchool().getCd().equals(teacher.getSchool().getCd())
		) {
			req.getRequestDispatcher("/error.jsp").forward(req, res);
			return;
		}

		// 削除
		if (sDao.delete(student.getNo())) {
			req.setAttribute("title", "学生削除");
			req.setAttribute("message", "削除が成功しました");

			LinkedHashMap<String, String> links = new LinkedHashMap<>();
			links.put("学生一覧", "StudentList.action");
			req.setAttribute("links", links);

			req.getRequestDispatcher("/success.jsp").forward(req, res);
		} else {
			req.getRequestDispatcher("/error.jsp").forward(req, res);
		}
	}

}
