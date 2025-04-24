package scoremanager.main;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestRegistExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションから先生の情報を取得
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher) session.getAttribute("user");

		// 検索欄のバリデーション
		Map<String, String> errors = TestRegistAction.validate(req, res, teacher.getSchool());
		if (errors.size() > 0) {
			// バリデーションエラーがある場合は再度入力画面に戻す
			System.out.println("バリデーションエラーが発生しました。");
			System.out.println(errors);
			req.setAttribute("errors", errors);
			forward(req, res, teacher.getSchool());
			return;
		}

		// フォームデータを取得
		String f_entYearStr = req.getParameter("f1");
		String f_classNum   = req.getParameter("f2");
		String f_subjectCd  = req.getParameter("f3");
		String f_noStr      = req.getParameter("f4");

		// フォームデータをキャスト
		int f_entYear = Integer.parseInt(f_entYearStr);
		int f_no = Integer.parseInt(f_noStr);

		// テストデータを取得
		SubjectDao subjectDao = new SubjectDao();
		TestDao testDao = new TestDao();
		School school = teacher.getSchool();
		Subject subject = subjectDao.get(f_subjectCd, school);

		List<Test> tests = testDao.filter(f_entYear, f_classNum, subject, f_no, school);

		HashMap<String, String> values = new HashMap<>();
		for (Test test : tests) {
			// フォームデータを取得
			String f_pointStr = req.getParameter("point_" + test.getStudent().getNo());

			values.put("point_" + test.getStudent().getNo(), f_pointStr);

			// フォームデータをキャスト
			int f_point = 0;
			try {
				f_point = Integer.parseInt(f_pointStr);
				if (f_point < 0 || f_point > 100) {
					errors.put("point_" + test.getStudent().getNo(), "0～100の範囲で入力してください");
					continue;
				}

				// テストデータをセット
				test.setPoint(f_point);
			} catch (NumberFormatException e) {
				errors.put("point_" + test.getStudent().getNo(), "整数で入力してください");
			}
		}

		req.setAttribute("values", values);

		if (errors.size() > 0) {
			// バリデーションエラーがある場合は再度入力画面に戻す
			req.setAttribute("errors", errors);
			forward(req, res, teacher.getSchool());
			return;
		}

		if (testDao.save(tests)) {
			req.setAttribute("title", "成績管理");
			req.setAttribute("message", "登録が成功しました");

			LinkedHashMap<String, String> links = new LinkedHashMap<>();
			links.put("戻る", "TestRegist.action");
			links.put("成績参照", "TestList.action");
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
		// JSPにフォワード
		TestRegistAction.forward(req, res);
	}

}
