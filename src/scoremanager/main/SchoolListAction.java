package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import dao.SchoolDao;
import tool.Action;

public class SchoolListAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// ここに学校管理者チェック

		// 学校リストを取得
		SchoolDao sDao = new SchoolDao();
		List<School> SchoolList = sDao.filter();

		req.setAttribute("school_list", SchoolList);

		// JSPにフォワード
		req.getRequestDispatcher("school_list.jsp").forward(req, res);

	}
}
