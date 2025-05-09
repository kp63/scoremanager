package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;
import tool.Auth;
import tool.ServletUtil;

public class TestRegistAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		forward(req, res);
	}

	public static void forward(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションから教員情報を取得
		Teacher teacher = Auth.getTeacher();
		if (teacher == null) {
			ServletUtil.throwError(req, res, "権限がありません");
			return;
		}

		// 成功メッセージ取得
		Object msg = req.getAttribute("message");
		if (msg instanceof String) {
			req.setAttribute("message", msg);
		}

		// 検索フォーム用データ
		setSearchFormAttributes(req, teacher.getSchool());

		// 検索条件バリデーション
		HashMap<String, String> formErrors = validate(req, res, teacher.getSchool());
		HashMap<String, String> errors = (HashMap<String, String>) formErrors.clone();

		// ExecuteAction からのエラーがあればマージ
		Object existing = req.getAttribute("errors");
		if (existing instanceof HashMap) {
			@SuppressWarnings("unchecked")
			HashMap<String, String> old = (HashMap<String, String>) existing;
			errors.putAll(old);
		}
		req.setAttribute("errors", errors);

		// 一覧取得
		if (formErrors.isEmpty()) {
			String f1 = req.getParameter("f1");
			String f2 = req.getParameter("f2");
			String f3 = req.getParameter("f3");
			String f4 = req.getParameter("f4");
			int entYear = (f1 != null && !f1.isEmpty()) ? Integer.parseInt(f1) : 0;
			int no      = (f4 != null && !f4.isEmpty()) ? Integer.parseInt(f4) : 0;

			Subject subject = new SubjectDao().get(f3, teacher.getSchool());
			List<Test> items = new TestDao()
				.filter(entYear, f2, subject, no, teacher.getSchool());
			req.setAttribute("items", items);
		}

		req.getRequestDispatcher("test_regist.jsp")
			.forward(req, res);
	}

	private static void setSearchFormAttributes(HttpServletRequest req, School school) throws Exception {
		ClassNumDao cNumDao   = new ClassNumDao();
		SubjectDao subjectDao = new SubjectDao();

		int currentYear = java.util.Calendar.getInstance()
			.get(java.util.Calendar.YEAR);
		List<String> entYearSet = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			entYearSet.add(String.valueOf(currentYear - i));
		}
		req.setAttribute("ent_year_set", entYearSet);
		req.setAttribute("class_num_set", cNumDao.filter(school));
		req.setAttribute("subject_set", subjectDao.filter(school));

		List<String> noSet = new ArrayList<>();
		for (int i = 1; i <= 2; i++) {
			noSet.add(String.valueOf(i));
		}
		req.setAttribute("no_set", noSet);

		req.setAttribute("f1", req.getParameter("f1"));
		req.setAttribute("f2", req.getParameter("f2"));
		req.setAttribute("f3", req.getParameter("f3"));
		req.setAttribute("f4", req.getParameter("f4"));
	}

	public static HashMap<String, String> validate(HttpServletRequest req,
												   HttpServletResponse res,
												   School school) {
		String f1 = req.getParameter("f1");
		String f2 = req.getParameter("f2");
		String f3 = req.getParameter("f3");
		String f4 = req.getParameter("f4");

		HashMap<String, String> errors = new HashMap<>();
		List<String> vals = Arrays.asList(f1, f2, f3, f4);
		if (vals.stream().allMatch(s -> s == null || s.isEmpty())) {
			errors.put("all", "");
			req.setAttribute("is_firstview", true);
			return errors;
		}
		if (vals.stream().anyMatch(s -> s == null || s.isEmpty())) {
			errors.put("f_error", "入学年度とクラスと科目と回数を選択してください");
		}

		if (f1 != null && !f1.isEmpty()) {
			try {
				int y = Integer.parseInt(f1);
				int cy = LocalDate.now().getYear();
				if (y < 2000 || y > cy) {
					errors.put("f1", "入学年度は2000年から" + cy + "年までの数字で指定してください。");
				}
			} catch (NumberFormatException e) {
				errors.put("f1", "入学年度は数字で指定してください。");
			}
		}
		if (f2 != null && !f2.isEmpty()) {
			try {
				if (new ClassNumDao().get(f2, school) == null) {
					errors.put("f2", "クラスが存在しません。");
				}
			} catch (Exception e) {
				errors.put("f2", "クラスの取得に失敗しました。");
			}
		}
		if (f3 != null && !f3.isEmpty()) {
			try {
				Subject subj = new SubjectDao().get(f3, school);
				if (subj == null) {
					errors.put("f3", "科目が存在しません。");
				} else {
					req.setAttribute("subject_name", subj.getName());
				}
			} catch (Exception e) {
				errors.put("f3", "科目の取得に失敗しました。");
			}
		}
		if (f4 != null && !f4.isEmpty()) {
			try {
				int c = Integer.parseInt(f4);
				if (c < 1 || c > 100) {
					errors.put("f4", "回数は1から100までの数字で指定してください。");
				}
			} catch (NumberFormatException e) {
				errors.put("f4", "回数は数字で指定してください。");
			}
		}
		return errors;
	}
}
