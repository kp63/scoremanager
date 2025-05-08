package scoremanager.main;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;
import dao.TeacherDao;
import tool.Action;

public class TeacherDeleteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションから教師情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        if ("admin".equals(teacher.getRole())) {
        	req.getRequestDispatcher("/error.jsp");
		}
        // 削除対象の教師IDを取得
        String teacherId = req.getParameter("teacher_id");

        // DAOのインスタンスを生成
        TeacherDao teacherDao = new TeacherDao();
        if (!req.getMethod().equalsIgnoreCase("POST")) {
            Teacher teacherToDelete = teacherDao.get(teacherId);
            if (teacherToDelete != null) {
                forward(req, res, teacherToDelete);
            } else {
                req.setAttribute("errorMessage", "指定された教師情報が見つかりません。");
                req.getRequestDispatcher("/error.jsp").forward(req, res);
            }
            return;
        }

        // 削除処理実行
        if (teacherDao.delete(teacherId)) {
            req.setAttribute("title", "教師情報削除");
            req.setAttribute("message", "削除が完了しました。");

            LinkedHashMap<String, String> links = new LinkedHashMap<>();
            links.put("教師一覧", "TeacherList.action");
            req.setAttribute("links", links);

            req.getRequestDispatcher("/success.jsp").forward(req, res);
        } else {
            req.setAttribute("errorMessage", "教師削除に失敗しました。");
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }

    /**
     * 削除確認画面を表示
     */
    private void forward(HttpServletRequest req, HttpServletResponse res, Teacher teacher) throws Exception {
        req.setAttribute("teacher_id", teacher.getId());
        if (req.getAttribute("teacher_name") == null) {
            req.setAttribute("teacher_name", teacher.getName());
        }
        req.getRequestDispatcher("teacher_delete.jsp").forward(req, res);
    }
}
