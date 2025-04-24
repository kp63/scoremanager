package scoremanager.main;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestRegistAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 成績登録ページへフォワード
		forward(req, res);
	}

	public static void forward(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションからユーザ情報を取得
		Teacher teacher = (Teacher) req.getSession().getAttribute("user");
		if (teacher == null) {
			// ログインしていない場合はログインページへリダイレクト
			res.sendRedirect(req.getContextPath() + "/login.jsp");
			return;
		}

		// フォーム用のAttributeを設定
		setSearchFormAttributes(req, teacher.getSchool());

		HashMap<String, String> formErrors = validate(req, res, teacher.getSchool());
		HashMap<String, String> errors = (HashMap<String, String>) formErrors.clone();

		// すでに設定されているエラーと合わせて、エラーをリクエストにセット
		Object existingErrors = req.getAttribute("errors");
		if (existingErrors instanceof HashMap) {
			@SuppressWarnings("unchecked")
			HashMap<String, String> oldErrors = (HashMap<String, String>) existingErrors;
			errors.putAll(oldErrors);
		}

		req.setAttribute("errors", errors);

		if (formErrors.size() < 1) {
			// フォームの値を取得
			String f_entYearStr = req.getParameter("f1");
			String f_classNum   = req.getParameter("f2");
			String f_subjectCd  = req.getParameter("f3");
			String f_noStr      = req.getParameter("f4");

			// キャスト
			int f_entYear = 0;
			if (f_entYearStr != null && !f_entYearStr.equals("")) {
				f_entYear = Integer.parseInt(f_entYearStr);
			}
			int f_no = 0;
			if (f_noStr != null && !f_noStr.equals("")) {
				f_no = Integer.parseInt(f_noStr);
			}

			TestDao testDao = new TestDao();
			SubjectDao subjectDao = new SubjectDao();

			Subject subject = subjectDao.get(f_subjectCd, teacher.getSchool());

			List<Test> items = testDao.filter(
				f_entYear,
				f_classNum,
				subject,
				f_no,
				teacher.getSchool()
			);
			req.setAttribute("items", items);
		}

		req.getRequestDispatcher("test_regist.jsp").forward(req, res);
	}

	/**
	 * 検索フォームのSELECTに対するOPTIONのリストをリクエストのAttributeに設定するメソッド
	 * @param req
	 * @param school
	 * @throws Exception
	 */
	private static void setSearchFormAttributes(HttpServletRequest req, School school) throws Exception {
		ClassNumDao cNumDao = new ClassNumDao();
		SubjectDao subjectDao = new SubjectDao();

		// 年度のセットを作成する（今年度から10年分）
		int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
		List<String> entYearSet = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			entYearSet.add(String.valueOf(currentYear - i));
		}
		req.setAttribute("ent_year_set", entYearSet);

		// クラスのセットを作成する（ClassNumDaoのクラスを使用）
		List<String> classNumSet = cNumDao.filter(school);
		req.setAttribute("class_num_set", classNumSet);

		// 科目のセットを作成する（SubjectDaoの科目を使用）
		List<Subject> subjectSet = subjectDao.filter(school);
		req.setAttribute("subject_set", subjectSet);

		// 回数のセットを作成する（1から2まで）
		List<String> noSet = new ArrayList<String>();
		for (int i = 1; i <= 2; i++) {
			noSet.add(String.valueOf(i));
		}
		req.setAttribute("no_set", noSet);


		// ユーザーの入力を保持するためのAttributeを設定
		req.setAttribute("f1", req.getParameter("f1"));
		req.setAttribute("f2", req.getParameter("f2"));
		req.setAttribute("f3", req.getParameter("f3"));
		req.setAttribute("f4", req.getParameter("f4"));

	}

	public static HashMap<String, String> validate(HttpServletRequest req, HttpServletResponse res, School school) {
		String f_entYearStr = req.getParameter("f1");
		String f_classNum   = req.getParameter("f2");
		String f_subjectCd  = req.getParameter("f3");
		String f_countStr   = req.getParameter("f4");

		HashMap<String, String> errors = new HashMap<String, String>();

		// フォームが全部NULLまたは全部埋まってるときはエラーを出さない
		Stream<String> formValuesCheck = Arrays.asList(f_entYearStr, f_classNum, f_subjectCd, f_countStr).stream();
		if (formValuesCheck.allMatch(s -> s == null || s.equals(""))) {
			errors.put("all", "");
			req.setAttribute("is_firstview", true);
			return errors;
		}

		// 年度のチェック
		if (f_entYearStr == null || f_entYearStr.equals("")) {
			errors.put("f1", "年度を選択してください。");
		} else {
			try {
				int entYear = Integer.parseInt(f_entYearStr);
				if (entYear < 2000 || entYear > LocalDate.now().getYear()) {
					errors.put("f1", "年度は2000年から" + LocalDate.now().getYear() + "年までの数字で指定してください。");
				}
			} catch (NumberFormatException e) {
				errors.put("f1", "年度は数字で指定してください。");
			}
		}

		// クラスのチェック
		ClassNumDao cNumDao = new ClassNumDao();
		if (f_classNum == null || f_classNum.equals("")) {
			errors.put("f2", "クラスを選択してください。");
		} else {
			try {
				if (cNumDao.get(f_classNum, school) == null) {
					errors.put("f2", "クラスが存在しません。");
				}
			} catch (Exception e) {
				errors.put("f2", "クラスの取得に失敗しました。");
			}
		}

		// 科目のチェック
		SubjectDao subjectDao = new SubjectDao();
		if (f_subjectCd == null || f_subjectCd.equals("")) {
			errors.put("f3", "科目を選択してください。");
		} else {
			try {
				Subject subject = subjectDao.get(f_subjectCd, school);
				if (subject == null) {
					errors.put("f3", "科目が存在しません。");
				}
				req.setAttribute("subject_name", subject.getName());
			} catch (Exception e) {
				errors.put("f3", "科目の取得に失敗しました。");
			}
		}

		// 回数のチェック
		if (f_countStr == null || f_countStr.equals("")) {
			errors.put("f4", "回数を選択してください。");
		} else {
			try {
				int count = Integer.parseInt(f_countStr);
				if (count < 1 || count > 100) {
					errors.put("f4", "回数は1から100までの数字で指定してください。");
				}
			} catch (NumberFormatException e) {
				errors.put("f4", "回数は数字で指定してください。");
			}
		}

		return errors;
	}

}
