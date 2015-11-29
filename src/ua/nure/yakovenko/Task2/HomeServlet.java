package ua.nure.yakovenko.Task2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ua.nure.yakovenko.Task2.DbController;

@WebServlet(description = "The main servlet of the app. Should redirect unauthenticated users to to AuthServlet.", urlPatterns = {
		"/" })
public class HomeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	DbController db = null;

	public HomeServlet() {
		super();
		try {
			db = new DbController(new InitialContext());
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("HomeServlet doGet");

		HttpSession session = request.getSession();
		User u = (User) session.getAttribute("user");
		System.out.println("HomeServlet USER:\n" + u);

		request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		if (request.getParameter("logout") != null) {
			System.out.println("INITIATE LOGOUT");
			request.getSession().invalidate();
			response.sendRedirect("authentication");
		} else {

			HttpSession session = request.getSession(false);

			Enumeration<String> params = request.getParameterNames();

			while (params.hasMoreElements()) {
				String param = params.nextElement();
				if (param.startsWith("delete")) {
					String id = param.split("-")[1];
					db.deleteUser(id);
					session.setAttribute("users", db.getUsersList());
					break;
				}
			}
			// response.sendRedirect("/Task2/");
			doGet(request, response);
		}

	}
}
