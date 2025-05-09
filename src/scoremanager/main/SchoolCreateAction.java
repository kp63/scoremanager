package scoremanager.main;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import dao.SchoolDao;
import tool.Action;

public class SchoolCreateAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// ここに学校管理者かチェック

		// POST時以外は、そのまま科目登録画面を表示
		if (!req.getMethod().equals("POST")) {
			forward(req, res);
			return;
		}

		// エラーメッセージのリストを初期化
		Map<String, String> errors = new HashMap<>();

		// フォームデータを取得
		String cd = req.getParameter("cd");// 学校コード
		String name = req.getParameter("name"); // 学校名

		// DAOの呼び出し
		SchoolDao sDao = new SchoolDao();

		// エラー一覧
		// 学校コードの文字数エラー
		if (cd.length() != 3) {
			errors.put("school_cd", "学校コードは3文字で入力してください");

		// 学校コードの重複エラー
		} else {
			School school = sDao.get(cd);
			if (school != null) {
				errors.put("school_cd", "学校コードが重複しています");
			}
		}

		// バリデーションエラーがある場合は再度入力画面に戻す
		if (errors.size() > 0) {

			req.setAttribute("errors", errors);

			forward(req, res);
			return;
		}

		// バリデーション通過時は、新しい科目を作成
		School school = new School();
		school.setCd(cd);
		school.setName(name);

		if (sDao.save(school)) {
			// 科目追加して成功ページに飛ぶ
			req.setAttribute("title", "学校情報登録");
			req.setAttribute("message", "登録が完了しました");

			LinkedHashMap<String, String> links = new LinkedHashMap<>();
			links.put("戻る", "SchoolCreate.action");
			links.put("学校一覧", "SchoolList.action");
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
	public void forward(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// JSPにフォワード
		req.getRequestDispatcher("school_create.jsp").forward(req, res);
	}

}
