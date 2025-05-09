package scoremanager.main;

import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;
import tool.Auth;
import tool.ServletUtil;

public class StudentDeleteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションから教員情報を取得
		Teacher teacher = Auth.getTeacher();
		if (teacher == null) {
			ServletUtil.throwError(req, res, "権限がありません");
			return;
		}

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

		// リクエストがPOSTメソッドではない場合、確認画面を表示
		if (!req.getMethod().equals("POST")) {
			forward(req, res, student);
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

	/**
	 * 削除確認画面を表示
	 */
	private void forward(HttpServletRequest req, HttpServletResponse res, Student student) throws Exception {
		req.setAttribute("no", student.getNo());
		req.setAttribute("name", student.getName());
		req.getRequestDispatcher("student_delete.jsp").forward(req, res);
	}
}
