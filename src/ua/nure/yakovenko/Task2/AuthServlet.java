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


@WebServlet("/authentication")
public class AuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	DbController db;
	
    public AuthServlet() {
        super();
        try {
        	db = new DbController(new InitialContext());
        } catch (NamingException e) {
        	e.printStackTrace();
        }
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/auth.jsp").forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter("ACTION");
		
		if ("login".equals(action)) {
			String email = request.getParameter("email");
//			String password = Security.generateSHA256(request.getParameter("password"));
			String password = request.getParameter("password");
			
			String lang = request.getParameter("language");
			
		    Locale locale = new Locale(lang);
		    ResourceBundle bundle = ResourceBundle.getBundle("ua.nure.yakovenko.Task2.i18n.text", locale);

		    User u = new User(null, null, email, null, password, null);
		    
		    ArrayList<String> result = new ArrayList<String>();
		    result.addAll(db.validateUserLogin(u,  bundle));
			
		    if(result.get(0) == "ok") {
		    	result.remove(0);
		    }
		    
		    System.out.println("ENTERED PASS:\n" + Security.generateSHA256(password));
		    
		    String userExists = db.validateUserExistsInDB(email, Security.generateSHA256(password), bundle);
		    System.out.println(userExists);
		    if (userExists != null) {
		    	result.add(userExists);
		    }
			
		    System.out.println(result);
		    
			if (result.size() > 0 && result.get(0) != "ok") {
				request.setAttribute("errorLoginMessage", result);
				request.setAttribute("email", email);
				doGet(request, response);
			} else {
				System.out.println("Validation OK");
			}
		} else if ("signup".equals(action)) {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String login = request.getParameter("login");
			
			String lang = request.getParameter("language");
			
		    Locale locale = new Locale(lang);
		    ResourceBundle bundle = ResourceBundle.getBundle("ua.nure.yakovenko.Task2.i18n.text", locale);

		    User u = new User(null, name, email, login, password, null);
		    
			ArrayList<String> result = db.validateUserSignup(u, bundle);
			
			if (result.size() > 0 && result.get(0) != "ok") {
				request.setAttribute("errorSignupMessage", result);
				request.setAttribute("email", email);
				request.setAttribute("name", name);
				request.setAttribute("login", login);
				doGet(request, response);
			} else {
				System.out.println("Validation OK");
			}
			
			
		} else {
			doGet(request, response);
		}
	}

}
