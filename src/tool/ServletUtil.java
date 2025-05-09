package tool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtil {
	public static void throwError(HttpServletRequest req, HttpServletResponse res, String errorMessage) throws Exception {
		req.setAttribute("errorMessage", errorMessage);
		req.getRequestDispatcher("/error.jsp").forward(req, res);
	}
}

