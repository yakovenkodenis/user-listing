package ua.nure.yakovenko.Task2;

import java.io.IOException;
import java.sql.SQLException;
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

@WebServlet(description = "Handles user editing.", urlPatterns = { "/edit", "/edit/", "/edit/*" })
public class EditUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DbController db;

	public EditUser() {
		super();
		try {
			db = new DbController(new InitialContext());
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		boolean editOwnAccount = true;

		User currentUser = (User) request.getSession().getAttribute("user");

		String[] urlParts = request.getRequestURI().split("/");
		if (urlParts.length == 4 && urlParts[3] != "") {
			editOwnAccount = false;
			request.setAttribute("editID", urlParts[3]);
		} else if (urlParts.length == 3 || urlParts[3] == "") {
			editOwnAccount = true;
			request.setAttribute("editID", currentUser.getId());
		} else {
			response.sendRedirect("/Task2/");
		}

		if (editOwnAccount) {
			request.setAttribute("edit_user", (User) request.getSession().getAttribute("user"));
			currentUser = (User) request.getSession().getAttribute("user");
		} else {
			currentUser = db.getUserByID(urlParts[3]);
		}

		request.setAttribute("editName", currentUser.getName());
		request.setAttribute("editEmail", currentUser.getEmail());
		request.setAttribute("editLogin", currentUser.getEmail());
		request.setAttribute("role", currentUser.getRole());
		request.setAttribute("editPass", currentUser.getPassword());
		request.setAttribute("isAdmin", currentUser.getRole() == "admin" ? "on" : null);

		request.getRequestDispatcher("/WEB-INF/edit.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		boolean editOwnAccount = true;

		User currentUser = (User) request.getSession().getAttribute("user");

		String[] urlParts = request.getRequestURI().split("/");
		if (urlParts.length == 4) {
			editOwnAccount = false;
			request.setAttribute("editID", urlParts[3]);
		} else if (urlParts.length == 3) {
			editOwnAccount = true;
			request.setAttribute("editID", currentUser.getId());
		} else {
			response.sendRedirect("/Task2/");
		}

		String lang = request.getParameter("language");

		Locale locale = new Locale(lang);
		ResourceBundle bundle = ResourceBundle.getBundle("ua.nure.yakovenko.Task2.i18n.text", locale);

		ArrayList<User> users = new ArrayList<>();

		System.out.println("USER CREATION");

		String email = request.getParameter("editEmail");
		String password = request.getParameter("editPassword");
		String name = request.getParameter("editName");
		String login = request.getParameter("editLogin");
		boolean isAdmin = request.getParameter("isAdmin") != null;
		String role = isAdmin ? "admin" : "user";

		// User editedUser = new User(null, name, email, login, password, role);
		User userToUpdate = null;

		// if edit own account
		if (editOwnAccount) {
			request.setAttribute("edit_user", (User) request.getSession().getAttribute("user"));
			userToUpdate = currentUser;
		} else {
			System.out.println("SHIIIIIIIIIIIIIIIIT\n:" + urlParts[3]);
			userToUpdate = db.getUserByID(urlParts[3]);
		}

		name = name != null ? name : userToUpdate.getName();
		email = email != null ? email : userToUpdate.getEmail();
		login = login != null ? login : userToUpdate.getLogin();
		if (password == null) {
			password = currentUser.getPassword();
		}
		password = currentUser.getPassword().equals(Security.generateSHA256(password)) ? currentUser.getPassword()
				: Security.generateSHA256(password);
		isAdmin = isAdmin != (userToUpdate.getRole() == "admin") ? isAdmin : userToUpdate.getRole() == "admin";
		role = isAdmin ? "admin" : "user";

//		User updatedUser = new User(null, name, email, login, password, role);

		ArrayList<String> result = new ArrayList<>();

		Validator validator = new Validator(bundle);

		User userExists;
		try {
			userExists = db.getUserByEmail(email);
		} catch (SQLException e) {
			userExists = new User(null, null, null, null, null, null);
			e.printStackTrace();
		}
		System.out.println(userExists);

		result.add(validator.validateAttributeExistence(login, bundle.getString("validation.login")));
		result.add(validator.validateAttributeExistence(name, bundle.getString("validation.name")));
		result.add(validator.validateMinimalLength(login, bundle.getString("validation.login"), 5));
		result.add(validator.validateMinimalLength(password, bundle.getString("validation.password"), 3));
		result.add(userExists.getName() == null ? bundle.getString("validation.no_user_in_db") : null);

		// result = db.validateUserSignup(updatedUser, bundle);

		boolean isOk = false;
		for (String s : result) {
			if (null == s || s.equals("ok") || s.equals("") || s.equals(" ")) {
				isOk = true;
			} else {
				isOk = false;
				break;
			}
		}

		System.out.println("VALIDATION ERRORS:\n" + result);

		if (!isOk) {
			request.setAttribute("errorEditMessage", result);
			request.setAttribute("editName", name);
			request.setAttribute("editEmail", email);
			request.setAttribute("editLogin", login);
			request.setAttribute("isAdmin", isAdmin);
			request.setAttribute("role", isAdmin ? "admin" : "user");
			doGet(request, response);
		} else {
			User res = new User(userToUpdate.getId(), name, email, login, password, role);
			db.updateUser(userToUpdate.getId(), res);
			System.out.println("UPDATED USER:\n" + res);
			users = db.getUsersList();
			HttpSession session = request.getSession(false);
//			if (editOwnAccount) {
//				session.setAttribute("user", res);
//			}
			session.setAttribute("users", users);
			System.out.println("EditUser_SERVLET\n");
			response.sendRedirect(request.getContextPath() + "/");
			System.out.println("User Edition OK");
		}
	}
}
