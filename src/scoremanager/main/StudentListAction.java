package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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

/**
 * 処理を実行するアクションクラス
 *
 * このクラスは以下の役割を持ちます:
 * - ログインフォームから送信されたID/パスワードの認証
 * - 認証成功時のセッション管理とメニュー画面へのリダイレクト
 * - 認証失敗時のエラー処理
 */
public class StudentListAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションから教員情報を取得
		Teacher teacher = Auth.getTeacher();
		if (teacher == null) {
			ServletUtil.throwError(req, res, "権限がありません");
			return;
		}

		// フォームデータを取得
		String entYearStr = req.getParameter("f1");
		String classNum = req.getParameter("f2");
		String isAttendStr = req.getParameter("f3");
		String name = req.getParameter("student-name");

		// フォームデータをキャスト
		int entYear = 0;
		if (entYearStr != null) {
			entYear = Integer.parseInt(entYearStr);
		}
		boolean isAttend = false;
		if (isAttendStr != null && isAttendStr.equals("t")) {
			isAttend = true;
		}

		// 10年前から1年後まで年をentYearSetリストに格納
		LocalDate todaysDate = LocalDate.now();
		int year = todaysDate.getYear();
		List<Integer> entYearSet = new ArrayList<>();
		for (int i = year - 10; i < year + 1; i++) {
			entYearSet.add(i);
		}

		// DAOの呼び出し
		StudentDao sDao = new StudentDao();
		ClassNumDao cNumDao = new ClassNumDao();

		// 学生データ、エラーメッセージのリストを初期化
		List<Student> students = null;
		Map<String, String> errors = new HashMap<>();

		// 先生が所属する学校のクラス番号一覧を取得
		List<String> classNumsSet = cNumDao.filter(teacher.getSchool());

		// フィルタリング
		if (name != null && !name.equals("")){
			if (entYear != 0 || !classNum.equals("0") || isAttend){
				errors.put("f1", "※名前検索をする場合ほかの条件は適用されません");
				req.setAttribute("errors", errors);
			}
			students = sDao.filter(teacher.getSchool(), name);
		} else if (entYear != 0 && !classNum.equals("0")) {
			students = sDao.filter(teacher.getSchool(), entYear, classNum, isAttend);
		} else if (entYear != 0 && classNum.equals("0")) {
			students = sDao.filter(teacher.getSchool(), entYear, isAttend);
		} else if (entYear == 0 && classNum == null || entYear == 0 && classNum.equals("0")) {
			students = sDao.filter(teacher.getSchool(), isAttend);
		} else {
			errors.put("f1", "クラスを指定する場合は入学年度も指定してください");
			req.setAttribute("errors", errors);
			students = sDao.filter(teacher.getSchool(), isAttend);
		}

		// レスポンス値を設定
		req.setAttribute("f1", entYear);
		req.setAttribute("f2", classNum);
		req.setAttribute("f3", isAttendStr);

		req.setAttribute("students", students);
		req.setAttribute("class_num_set", classNumsSet);
		req.setAttribute("ent_year_set", entYearSet);

		// JSPにフォワード
		req.getRequestDispatcher("student_list.jsp").forward(req, res);

	}
}
