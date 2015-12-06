package ua.nure.yakovenko.Task2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

@WebServlet(description = "Handles user creation", urlPatterns = { "/create/", "/create" })
public class CreateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static final Logger LOG = Logger.getLogger(CreateUser.class);
	
	private DbController db;

	public CreateUser() {
		super();
		try {
			db = new DbController(new InitialContext());
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/create.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String lang = request.getParameter("language");

		Locale locale = new Locale(lang);
		ResourceBundle bundle = ResourceBundle.getBundle("ua.nure.yakovenko.Task2.i18n.text", locale);

		ArrayList<User> users = new ArrayList<>();

		System.out.println("USER CREATION");

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String login = request.getParameter("login");
		boolean isAdmin = request.getParameter("isAdmin") != null;

		User u = new User(null, name, email, login, password, isAdmin ? "admin" : "user");

		ArrayList<String> result = db.validateUserSignup(u, bundle);

		// If all fields pass the validation - check user in DB
		if (result.size() > 0 && result.get(0) == "ok") {
			result.remove(0);
			String encryptedPassword = Security.generateSHA256(password);
			String userExists = db.validateUserExistsInDB(email, encryptedPassword, bundle, true);
			if (userExists != null) {
				System.out.println(userExists);
				result.add(userExists);
			}
		}

		System.out.println("VALIDATION ERRORS:\n" + result);

		if (result.size() > 0) {
			request.setAttribute("errorCreateMessage", result);
			request.setAttribute("createEmail", email);
			request.setAttribute("createName", name);
			request.setAttribute("createLogin", login);
			doGet(request, response);
		} else {
			db.createNewUser(u);
			users = db.getUsersList();
			HttpSession session = request.getSession(false);
			session.setAttribute("users", users);
			System.out.println("CreateUser_SERVLET\n");
			response.sendRedirect(request.getContextPath() + "/");
			System.out.println("User Creation OK");
		}
	}
}
