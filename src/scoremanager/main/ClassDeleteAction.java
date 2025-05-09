package scoremanager.main;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import dao.SchoolDao;
import tool.Action;
import tool.Auth;
import tool.ServletUtil;

public class ClassDeleteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションから教員情報を取得
		Teacher teacher = Auth.getTeacher();
		if (teacher == null) {
			ServletUtil.throwError(req, res, "権限がありません");
			return;
		}

        ClassNumDao cDao = new ClassNumDao();
        SchoolDao sDao = new SchoolDao();

        // パラメーター取得
        String classNumStr = req.getParameter("class_num");
        String schoolCd = req.getParameter("school_cd");

        ClassNum class_num = cDao.get(classNumStr, sDao.get(schoolCd));

        // POSTでなければ削除確認画面へ
        if (!req.getMethod().equalsIgnoreCase("POST")) {
            forward(req, res);
            return;
        }

        // 削除処理実行
        if (cDao.delete(class_num)) {
            req.setAttribute("title", "クラス番号削除");
            req.setAttribute("message", "削除が完了しました。");

            LinkedHashMap<String, String> links = new LinkedHashMap<>();
            links.put("クラス番号一覧", "ClassList.action");
            req.setAttribute("links", links);

            req.getRequestDispatcher("/success.jsp").forward(req, res);
        } else {
            req.setAttribute("errorMessage", "クラス番号削除に失敗しました。");
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }

    }

    /**
     * 削除確認画面を表示
     */
    private void forward(HttpServletRequest req, HttpServletResponse res) throws Exception {
    	HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        String classNumStr = req.getParameter("class_num");

    	ClassNumDao cDao = new ClassNumDao();
    	ClassNum class_num = cDao.get(classNumStr, teacher.getSchool());

    	req.setAttribute("class_num", class_num);

        req.getRequestDispatcher("class_delete.jsp").forward(req, res);
    }
}
