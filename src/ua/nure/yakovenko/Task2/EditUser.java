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
		
		User currentUser = (User)request.getSession().getAttribute("user");

		String[] urlParts = request.getRequestURI().split("/");
		if (urlParts.length == 4) {
			editOwnAccount = false;
			request.setAttribute("editID", currentUser.getId());
		} else if (urlParts.length == 3) {
			editOwnAccount = true;
			request.setAttribute("editID", urlParts[3]);
		} else {
			response.sendRedirect("/Task2/");
		}
		
		
		if (editOwnAccount) {
			request.setAttribute("edit_user", (User)request.getSession().getAttribute("user"));
			currentUser = (User)request.getSession().getAttribute("user");
		} else {
			currentUser = db.getUserByID(urlParts[3]);
		}
		
		request.setAttribute("editName", currentUser.getName());
		request.setAttribute("editEmail", currentUser.getEmail());
		request.setAttribute("editLogin", currentUser.getEmail());
		request.setAttribute("isAdmin", currentUser.getRole() == "admin" ? "on" : null);
		
		request.getRequestDispatcher("/WEB-INF/edit.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		boolean editOwnAccount = true;
		
		User currentUser = (User)request.getSession().getAttribute("user");

		String[] urlParts = request.getRequestURI().split("/");
		if (urlParts.length == 4) {
			editOwnAccount = false;
			request.setAttribute("editID", currentUser.getId());
		} else if (urlParts.length == 3) {
			editOwnAccount = true;
			request.setAttribute("editID", urlParts[3]);
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

		User editedUser = new User(null, name, email, login, password, role);
		User userToUpdate = null;
		
		// if edit own account
		if (editOwnAccount) {
			request.setAttribute("edit_user", (User)request.getSession().getAttribute("user"));
			userToUpdate = currentUser;
		} else {
			userToUpdate = db.getUserByID(urlParts[3]);
		}
		
		name = name != null ? name : userToUpdate.getName();
		email = email != null ? email : userToUpdate.getEmail();
		login = login != null ? login : userToUpdate.getLogin();
		password = password != null ? Security.generateSHA256(password) : userToUpdate.getPassword();
		isAdmin = isAdmin != (userToUpdate.getRole() == "admin") ? isAdmin : userToUpdate.getRole() == "admin";
		role = isAdmin ? "admin" : "user";
		
		User updatedUser = new User(null, name, email, login, password, role);
		
		ArrayList<String> result = db.validateUserSignup(updatedUser, bundle);

		// If all fields pass the validation - check user in DB
		if (result.size() > 0 && result.get(0) == "ok") {
			result.remove(0);
			String userExists = db.validateUserExistsInDB(email, password, bundle, true);
			if (userExists != null) {
				System.out.println(userExists);
				result.add(userExists);
			}
		}

		System.out.println("VALIDATION ERRORS:\n" + result);

		if (result.size() > 0) {
			request.setAttribute("errorEditMessage", result);
			request.setAttribute("editName", name);
			request.setAttribute("editEmail", email);
			request.setAttribute("editLogin", login);
			request.setAttribute("isAdmin", isAdmin);
			doGet(request, response);
		} else {
			db.updateUser(userToUpdate.getId(), userToUpdate);
			users = db.getUsersList();
			HttpSession session = request.getSession(false);
			session.setAttribute("users", users);
			System.out.println("EditUser_SERVLET\n");
			response.sendRedirect(request.getContextPath() + "/");
			System.out.println("User Edition OK");
		}
	}
}
