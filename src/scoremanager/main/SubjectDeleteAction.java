package scoremanager.main;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectDeleteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションから教師情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // 削除対象の科目コードを取得
        String cd = req.getParameter("no");

        SubjectDao sDao = new SubjectDao();
        Subject subject = sDao.get(cd, teacher.getSchool());


        // POSTでなければ削除確認画面へ
        if (!req.getMethod().equalsIgnoreCase("POST")) {
            forward(req, res, subject);
            return;
        }

        // 削除処理実行
        if (sDao.delete(subject)) {
            req.setAttribute("title", "科目情報削除");
            req.setAttribute("message", "削除が完了しました。");

            LinkedHashMap<String, String> links = new LinkedHashMap<>();
            links.put("科目一覧", "SubjectList.action");
            req.setAttribute("links", links);

            req.getRequestDispatcher("/success.jsp").forward(req, res);
        } else {
            req.setAttribute("errorMessage", "科目削除に失敗しました。");
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }

    /**
     * 削除確認画面を表示
     */
    private void forward(HttpServletRequest req, HttpServletResponse res, Subject subject) throws Exception {
        req.setAttribute("no", subject.getCd());

        if (req.getAttribute("subject_name") == null) {
            req.setAttribute("subject_name", subject.getName());
        }

        req.getRequestDispatcher("subject_delete.jsp").forward(req, res);
    }
}
