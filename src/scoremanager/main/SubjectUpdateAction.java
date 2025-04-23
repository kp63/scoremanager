package scoremanager.main;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectUpdateAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションから先生の情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // 更新対象の科目コードをリクエストパラメータから取得
        String cd = req.getParameter("no");
        // 科目情報を取得
        SubjectDao sDao = new SubjectDao();
        Subject subject = sDao.get(cd, teacher.getSchool());
        if (subject == null) {
            req.getRequestDispatcher("/error.jsp").forward(req, res);
            return;
        }

        // POST以外のリクエストの場合は、入力画面に遷移
        if (!req.getMethod().equalsIgnoreCase("POST")) {
            forward(req, res, subject);
            return;
        }

        // エラーメッセージのリストを初期化
        Map<String, String> errors = new HashMap<>();

        // フォームデータを取得
        String name = req.getParameter("name");

        // バリデーション
        if (name == null || name.isEmpty()) {
            errors.put("name", "科目名を入力してください");
        } else if (name.length() > 20) {
            errors.put("name", "科目名は20文字以下で入力してください");
        }

        // バリデーションエラーがある場合は入力画面に戻す
        if (!errors.isEmpty()) {
            req.setAttribute("name", name);
            req.setAttribute("errors", errors);
            forward(req, res, subject);
            return;
        }

        // 科目情報を更新
        subject.setName(name);

        if (sDao.save(subject)) {
            // 更新成功時のレスポンス設定
            req.setAttribute("title", "科目情報変更");
            req.setAttribute("message", "変更が成功しました");

            LinkedHashMap<String, String> links = new LinkedHashMap<>();
            links.put("戻る", "SubjectUpdate.action?no=" + subject.getCd());
            links.put("科目一覧", "SubjectList.action");
            req.setAttribute("links", links);

            req.getRequestDispatcher("/success.jsp").forward(req, res);
        } else {
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }

    /**
     * 科目情報変更画面を表示
     * @param req リクエスト
     * @param res レスポンス
     * @param subject 科目情報
     * @throws Exception
     */
    private void forward(HttpServletRequest req, HttpServletResponse res, Subject subject) throws Exception {
        req.setAttribute("cd", subject.getCd());

        if (req.getAttribute("name") == null) {
            req.setAttribute("name", subject.getName());
        }

        req.getRequestDispatcher("subject_update.jsp").forward(req, res);
    }
}