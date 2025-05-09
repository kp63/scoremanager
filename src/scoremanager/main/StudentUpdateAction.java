package scoremanager.main;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import tool.Action;
import tool.Auth;
import tool.ServletUtil;

public class StudentUpdateAction extends Action {
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

		// POST時以外は、そのまま学生登録画面を表示
		if (!req.getMethod().equals("POST")) {
			forward(req, res, student);
			return;
		}

		// エラーメッセージのリストを初期化
		Map<String, String> errors = new HashMap<>();

		// フォームデータを取得
		String f_name = req.getParameter("name");
		String f_classNum = req.getParameter("class_num");
		String f_isAttendStr = req.getParameter("is_attend");

		// フォームデータをキャスト
		boolean f_isAttend = false;
		if (f_isAttendStr != null) {
			f_isAttend = f_isAttendStr.equals("t");
		}

		// バリデーション
		// ○名前
		if (f_name == null || f_name.equals("")) {
			errors.put("name", "名前を入力してください");
		} else if (f_name.length() > 10) {
			errors.put("name", "名前は10文字以下で入力してください");
		}

		// ○クラス
		if (f_classNum == null || f_classNum.equals("")) {
			errors.put("class_num", "クラスを選択してください");
		} else {
			// クラスの実在チェック
			ClassNumDao cDao = new ClassNumDao();
			if (cDao.get(f_classNum, teacher.getSchool()) == null) {
				errors.put("class_num", "クラスはプルダウンから選択してください");
			}
		}

		// バリデーションエラーがある場合は再度入力画面に戻す
		if (errors.size() > 0) {
			// レスポンス値を設定
			req.setAttribute("f_class_num", f_classNum);
			req.setAttribute("f_name", f_name);
			req.setAttribute("f_is_attend", f_isAttend);

			req.setAttribute("errors", errors);

			forward(req, res, student);
			return;
		}

		// バリデーション通過時は、学生を作成する
		student.setName(f_name);
		student.setClassNum(f_classNum);
		student.setAttend(f_isAttend);

		if (sDao.save(student)) {
			req.setAttribute("title", "学生情報変更");
			req.setAttribute("message", "変更が成功しました");

			LinkedHashMap<String, String> links = new LinkedHashMap<>();
			links.put("学生一覧", "StudentList.action");
			req.setAttribute("links", links);

			req.getRequestDispatcher("/success.jsp").forward(req, res);
		} else {
			req.getRequestDispatcher("/error.jsp").forward(req, res);
		}
	}

	/**
	 * 学生登録画面を表示
	 * @param req
	 * @param res
	 * @param school 学校情報
	 * @throws Exception
	 */
	public void forward(HttpServletRequest req, HttpServletResponse res, Student student) throws Exception {
		// 学校のクラス番号一覧を取得
		ClassNumDao cNumDao = new ClassNumDao();
		List<String> classNumSet = cNumDao.filter(student.getSchool());

		// アトリビュートのセット
		req.setAttribute("class_num_set", classNumSet);
		req.setAttribute("ent_year", student.getEntYear());
		req.setAttribute("no", student.getNo());

		if (req.getAttribute("f_name") == null) {
			req.setAttribute("f_name", student.getName());
		}
		if (req.getAttribute("f_class_num") == null) {
			req.setAttribute("f_class_num", student.getClassNum());
		}
		if (req.getAttribute("f_is_attend") == null) {
			req.setAttribute("f_is_attend", student.isAttend());
		}

		// JSPにフォワード
		req.getRequestDispatcher("student_update.jsp").forward(req, res);
	}
}
