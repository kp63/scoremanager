package scoremanager.main;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectCreateAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションから先生の情報を取得
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher) session.getAttribute("user");

		// POST時以外は、そのまま学生登録画面を表示
		if (!req.getMethod().equals("POST")) {
			forward(req, res, teacher.getSchool());
			return;
		}

		// エラーメッセージのリストを初期化
		Map<String, String> errors = new HashMap<>();

		// フォームデータを取得
		String cd = req.getParameter("cd");// 科目コード
		String name = req.getParameter("name"); // 科目名

		// DAOの呼び出し
		SubjectDao sDao = new SubjectDao();

		// 科目コードエラー
		// 科目コードの文字数エラー
		if (cd.length() != 3) {
			errors.put("subject_cd", "科目コードは3文字で入力してください");

		// 科目コードの重複エラー
		} else {
			Subject subject = sDao.get(cd, teacher.getSchool());
			if (subject != null) {
				errors.put("subject_cd", "科目コードが重複しています");
			}
		}

		// バリデーションエラーがある場合は再度入力画面に戻す
		if (errors.size() > 0) {

			req.setAttribute("errors", errors);

			forward(req, res, teacher.getSchool());
			return;
		}

		// バリデーション通過時は、新しい科目を作成
		Subject subject = new Subject();
		subject.setCd(cd);
		subject.setName(name);
		subject.setSchool(teacher.getSchool());

		if (sDao.save(subject)) {
			req.setAttribute("title", "科目情報登録");
			req.setAttribute("message", "登録が完了しました");

			LinkedHashMap<String, String> links = new LinkedHashMap<>();
			links.put("戻る", "SubjectCreate.action");
			links.put("科目一覧", "SubjectList.action");
			req.setAttribute("links", links);

			req.getRequestDispatcher("/success.jsp").forward(req, res);
		} else {
			req.getRequestDispatcher("/error.jsp").forward(req, res);
		}
	}

	/**
	 * 科目登録画面を表示
	 * @param req
	 * @param res
	 * @param school 学校情報
	 * @throws Exception
	 */
	public void forward(HttpServletRequest req, HttpServletResponse res, School school) throws Exception {
		// JSPにフォワード
		req.getRequestDispatcher("subject_create.jsp").forward(req, res);
	}

}
