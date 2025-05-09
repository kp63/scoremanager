package scoremanager.main;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.ClassNum;
import bean.School;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;
import tool.Auth;
import tool.ServletUtil;

public class ClassCreateAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションから先生の情報を取得
		Teacher teacher = Auth.getTeacher();
		if (teacher == null) {
			ServletUtil.throwError(req, res, "権限がありません");
			return;
		}

		// POST時以外は、そのまま登録画面を表示
		if (!req.getMethod().equals("POST")) {
			forward(req, res, teacher.getSchool());
			return;
		}

		// エラーメッセージのリストを初期化
		Map<String, String> errors = new HashMap<>();

		// フォームデータ(クラス番号)を取得
		String num = req.getParameter("num");

		// DAOの呼び出し
		ClassNumDao cDao = new ClassNumDao();

		// エラーチェック
		// クラス番号の文字数エラー
		if (num.length() != 3) {
			errors.put("num", "クラス番号は3文字で入力してください");

		// クラス番号の重複エラー
		} else {
			ClassNum class_num = cDao.get(num, teacher.getSchool());
			if (class_num != null) {
				errors.put("num", "クラス番号が重複しています");
			}
		}

		// バリデーションエラーがある場合は再度入力画面に戻す
		if (errors.size() > 0) {

			req.setAttribute("errors", errors);

			forward(req, res, teacher.getSchool());
			return;
		}

		// バリデーション通過時は、新しいクラスを作成
		ClassNum class_num = new ClassNum();
		class_num.setClass_num(num);
		class_num.setSchool(teacher.getSchool());

		if (cDao.save(class_num)) {
			// 科目追加して成功ページに飛ぶ
			req.setAttribute("title", "クラス登録");
			req.setAttribute("message", "登録が完了しました");

			LinkedHashMap<String, String> links = new LinkedHashMap<>();
			links.put("戻る", "ClassCreate.action");
			links.put("クラス一覧", "ClassList.action");
			req.setAttribute("links", links);

			req.getRequestDispatcher("/success.jsp").forward(req, res);
		} else {
			req.getRequestDispatcher("/error.jsp").forward(req, res);
		}
	}

	/**
	 * クラス登録画面を表示
	 * @param req
	 * @param res
	 * @param school 学校情報
	 * @throws Exception
	 */
	public void forward(HttpServletRequest req, HttpServletResponse res, School school) throws Exception {
		// セッションから先生の情報を取得
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher) session.getAttribute("user");

		// 先生の学校を取得
		School school2 = teacher.getSchool();

		// レスポンス値の設定
		req.setAttribute("school_name", school2.getName());

		// JSPにフォワード
		req.getRequestDispatcher("class_create.jsp").forward(req, res);
	}

}
