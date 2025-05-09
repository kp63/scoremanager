package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import tool.Action;
import tool.Auth;
import tool.ServletUtil;

public class StudentCreateAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションから教員情報を取得
		Teacher teacher = Auth.getTeacher();
		if (teacher == null) {
			ServletUtil.throwError(req, res, "権限がありません");
			return;
		}

		// POST時以外は、そのまま学生登録画面を表示
		if (!req.getMethod().equals("POST")) {
			forward(req, res, teacher.getSchool());
			return;
		}

		// エラーメッセージのリストを初期化
		Map<String, String> errors = new HashMap<>();

		// フォームデータを取得
		String f_entYearStr = req.getParameter("ent_year");
		String f_no = req.getParameter("no");
		String f_name = req.getParameter("name");
		String f_classNum = req.getParameter("class_num");

		// フォームデータをキャスト
		int f_entYear = 0;
		if (f_entYearStr != null) {
			try {
				f_entYear = Integer.parseInt(f_entYearStr);
			} catch (NumberFormatException e) {
				errors.put("ent_year", "入学年度は数字で指定してください");
			}
		}

		// DAOの呼び出し
		StudentDao sDao = new StudentDao();

		// バリデーション
		// ○入学年度
		if (!errors.containsKey("ent_year")) {
			if (!getEntYearSet().contains(f_entYear)) {
				errors.put("ent_year", "入学年度はプルダウンから選択してください");
			}
		} else if (f_entYear == 0) {
			errors.put("ent_year", "入学年度を選択してください");
		}

		// ○学生番号
		if (f_no == null || f_no.equals("")) {
			errors.put("no", "学生番号を入力してください");
		} else if (f_no.length() > 10) {
			errors.put("no", "学生番号は10桁以下で入力してください");
		} else {
			// 学生番号の重複チェック
			Student student = sDao.get(f_no);
			if (student != null) {
				errors.put("no", "学生番号が重複しています");
			}
		}

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
			req.setAttribute("f_ent_year", f_entYearStr);
			req.setAttribute("f_class_num", f_classNum);
			req.setAttribute("f_no", f_no);
			req.setAttribute("f_name", f_name);

			req.setAttribute("errors", errors);

			forward(req, res, teacher.getSchool());
			return;
		}

		// バリデーション通過時は、学生を作成する
		Student student = new Student();
		student.setEntYear(f_entYear);
		student.setNo(f_no);
		student.setName(f_name);
		student.setClassNum(f_classNum);
		student.setSchool(teacher.getSchool());
		student.setAttend(true);

		if (sDao.save(student)) {
			req.setAttribute("title", "学生新規登録");
			req.setAttribute("message", "登録が成功しました");

			LinkedHashMap<String, String> links = new LinkedHashMap<>();
			links.put("戻る", "StudentCreate.action");
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
	public void forward(HttpServletRequest req, HttpServletResponse res, School school) throws Exception {
		// 学校のクラス番号一覧を取得
		ClassNumDao cNumDao = new ClassNumDao();
		List<String> classNumSet = cNumDao.filter(school);

		// 10年前から10年後まで年をentYearSetリストに格納
		List<Integer> entYearSet = getEntYearSet();

		// アトリビュートのセット
		req.setAttribute("class_num_set", classNumSet);
		req.setAttribute("ent_year_set", entYearSet);

		// JSPにフォワード
		req.getRequestDispatcher("student_create.jsp").forward(req, res);
	}

	/**
	 * 10年前から10年後までの年をリストで取得
	 * @return 年のリスト
	 */
	List<Integer> getEntYearSet() {
		// 10年前から10年後まで年をentYearSetリストに格納
		LocalDate todaysDate = LocalDate.now();
		int year = todaysDate.getYear();
		List<Integer> entYearSet = new ArrayList<>();
		for (int i = year - 10; i < year + 10; i++) {
			entYearSet.add(i);
		}
		return entYearSet;
	}
}
