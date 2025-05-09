package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tool.Action;
import tool.Auth;

public class MenuAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		HttpSession session = req.getSession();

		if (Auth.isSuperuser()) {
			session.setAttribute("showSuperExtraData", null);
		}
		if (Auth.isAdminTeacher()) {
			session.setAttribute("showExtraData", null);
		}

		req.getRequestDispatcher("menu.jsp").forward(req, res);
	}

}
