package scoremanager.main;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Teacher;
import dao.TeacherDao;
import tool.Action;

public class TeacherCreateAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションから先生の情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        req.setAttribute("school_name", teacher.getSchool().getName());
        // POST時以外は、そのまま登録画面を表示
        if (!req.getMethod().equals("POST")) {
            forward(req, res, teacher.getSchool());
            return;
        }
        if ("admin".equals(teacher.getRole())) {
        	req.getRequestDispatcher("/error.jsp");
		}

        // エラーメッセージのリストを初期化
        Map<String, String> errors = new HashMap<>();

        // フォームデータ(教員情報)を取得
        String school_cd = req.getParameter("school_cd");
        String id = req.getParameter("id");
        String password = req.getParameter("password");
        String name = req.getParameter("name");

        // DAOの呼び出し
        TeacherDao tDao = new TeacherDao();

        // IDの文字数チェック
        if (id.length() > 10) {
            errors.put("id", "idは10文字で入力してください");
        } else {
            // IDの重複チェック

            if (tDao.get(id) != null) {
                errors.put("id", "idが重複しています");
            }
        }

        // バリデーションエラーがある場合は入力画面に戻す
        if (errors.size() > 0) {
            req.setAttribute("errors", errors);
            req.setAttribute("id", id);
            req.setAttribute("password", password);
            req.setAttribute("name",name);
            forward(req, res, teacher.getSchool());
            return;
        }

        // 新しい Teacher オブジェクトを作成
        Teacher newTeacher = new Teacher();
        newTeacher.setId(id);
        newTeacher.setPassword(password);
        newTeacher.setName(name);
        newTeacher.setSchool(teacher.getSchool());


        if (tDao.save(newTeacher)) {
        	//成功ぺージに飛ぶ
            req.setAttribute("title", "教員登録");
            req.setAttribute("message", "登録が完了しました");

            LinkedHashMap<String, String> links = new LinkedHashMap<>();
            links.put("戻る", "TeacherCreate.action");
            links.put("教員一覧", "TeacherList.action");
            req.setAttribute("links", links);

            req.getRequestDispatcher("/success.jsp").forward(req, res);
        } else {
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }

	public void forward(HttpServletRequest req, HttpServletResponse res, School school) throws Exception {
		// JSPにフォワード
		req.getRequestDispatcher("teacher_create.jsp").forward(req, res);

	}
}