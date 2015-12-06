package ua.nure.yakovenko.Task2;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebServlet(description = "Handles user deletion.", urlPatterns = { "/delete/*" })
public class DeleteUser extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	static final Logger LOG = Logger.getLogger(DeleteUser.class);

	private DbController db;

	public DeleteUser() {
		super();
		try {
			db = new DbController(new InitialContext());
		} catch (NamingException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String[] urlParts = request.getRequestURI().split("/");
		if (urlParts.length != 4 || urlParts[3] == "") {
			response.sendRedirect(request.getContextPath() + "/");
			return;
		}

		request.setAttribute("deleteID", urlParts[3]);
		request.setAttribute("deleteUser", db.getUserByID(urlParts[3]));
		request.getRequestDispatcher("/WEB-INF/delete.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		String[] urlParts = request.getRequestURI().split("/");
		if (urlParts.length != 4) {
			response.sendRedirect(request.getContextPath() + "/");
		} else {
			if (request.getParameter("delete") != null) {
				String id = urlParts[3];
				db.deleteUser(id);
				request.getSession().setAttribute("users", db.getUsersList());
			}
			response.sendRedirect(request.getContextPath() + "/");
		}
	}

}
