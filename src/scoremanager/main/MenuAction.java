package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;
import tool.Action;

public class MenuAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher) session.getAttribute("user");
		if ("admin".equals(teacher.getRole())) {
		    session.setAttribute("showExtraData", true);
		}
		req.getRequestDispatcher("menu.jsp").forward(req, res);
	}

}
