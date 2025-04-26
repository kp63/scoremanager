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
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher) session.getAttribute("user");
		if (teacher == null) {
			res.sendRedirect(req.getContextPath() + "/login.jsp");
			return;
		}

		// 検索条件バリデーション
		Map<String, String> errors = TestRegistAction.validate(req, res, teacher.getSchool());
		if (!errors.isEmpty()) {
			req.setAttribute("errors", errors);
			forward(req, res, teacher.getSchool());
			return;
		}

		// パラメータ取得
		String f1 = req.getParameter("f1");
		String f2 = req.getParameter("f2");
		String f3 = req.getParameter("f3");
		String f4 = req.getParameter("f4");
		int entYear = Integer.parseInt(f1);
		int no      = Integer.parseInt(f4);

		// テストデータ取得
		Subject subject = new SubjectDao().get(f3, teacher.getSchool());
		TestDao testDao = new TestDao();
		List<Test> tests = testDao.filter(entYear, f2, subject, no, teacher.getSchool());

		// 入力／削除チェック保持
		LinkedHashMap<String, String> values = new LinkedHashMap<>();
		for (Test t : tests) {
			String key = t.getStudent().getNo();
			if (req.getParameter("delete_" + key) != null) {
				t.setPoint(-1);
				values.put("point_" + key, "");
			} else {
				String pointStr = req.getParameter("point_" + key);
				values.put("point_" + key, pointStr);
				try {
					int v = Integer.parseInt(pointStr);
					if (v < 0 || v > 100) {
						errors.put("point_" + key, "0～100の範囲で入力してください");
					} else {
						t.setPoint(v);
					}
				} catch (NumberFormatException e) {
					errors.put("point_" + key, "整数で入力してください");
				}
			}
		}

		req.setAttribute("values", values);
		if (!errors.isEmpty()) {
			req.setAttribute("errors", errors);
			forward(req, res, teacher.getSchool());
			return;
		}

		// DB 更新（INSERT or UPDATE。削除は point を NULL に）
		boolean ok = true;
		for (Test t : tests) {
			ok &= testDao.update(t);
		}

		// メッセージをセット
		String msg = ok ? "登録が成功しました" : "登録に失敗しました";
		req.setAttribute("message", msg);

		// 登録成功時のみ「戻る」「成績参照」リンクをセット
		if (ok) {
			LinkedHashMap<String, String> links = new LinkedHashMap<>();
			links.put("戻る",     "TestRegist.action");
			links.put("成績参照", "TestList.action");
			req.setAttribute("links", links);
		}

		// 成功／失敗にかかわらず、一覧画面へフォワード
		forward(req, res, teacher.getSchool());
	}

	private void forward(HttpServletRequest req, HttpServletResponse res, School school) throws Exception {
		TestRegistAction.forward(req, res);
	}
}
