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
		System.out.println("ACTION:\t" + action);
		
		String lang = request.getParameter("language");
		
	    Locale locale = new Locale(lang);
	    ResourceBundle bundle = ResourceBundle.getBundle("ua.nure.yakovenko.Task2.i18n.text", locale);
		
		if ("login".equals(action)) {
			System.out.println("AUTHENTICATION: LOGIN");
			
			String email = request.getParameter("email");
			String password = request.getParameter("password");

		    User u = new User(null, null, email, null, password, null);
		    
		    ArrayList<String> result = new ArrayList<String>();
		    result.addAll(db.validateUserLogin(u,  bundle));
			
		    // If all fields pass the validation - check user in DB  
		    if(result.size() > 0 && result.get(0) == "ok") {
		    	result.remove(0);
		    	
		    	String enctyptedPassword = Security.generateSHA256(password);
 
			    System.out.println("ENTERED PASS:\n" + password);
			    System.out.println("ENTERED ENCRYPTED PASS:\n" + enctyptedPassword);
			    
			    String userExists = db.validateUserExistsInDB(email, enctyptedPassword, bundle, false);
			    System.out.println(userExists);
			    if (userExists != null) {
			    	result.add(userExists);
			    }
		    }
			
		    System.out.println("VALIDATION ERRORS:\n" + result);
		    
			if (result.size() > 0) {
				request.setAttribute("errorLoginMessage", result);
				request.setAttribute("email", email);
				doGet(request, response);
			} else {
				System.out.println("Validation OK");
			}
		} else if ("signup".equals(action)) {
			System.out.println("AUTHENTICATION: SIGNUP");
			
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String login = request.getParameter("login");
		    
		    User u = new User(null, name, email, login, password, null);
		    
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
				request.setAttribute("errorSignupMessage", result);
				request.setAttribute("email", email);
				request.setAttribute("name", name);
				request.setAttribute("login", login);
				doGet(request, response);
			} else {
				try {
					db.createNewUser(name, login, email, password);
					request.setAttribute("email", email);
					doGet(request, response);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("Validation OK");
			}
			
			
		} else {
			System.out.println("UNKNOWN POST REQUEST FROM AUTH PAGE");
			doGet(request, response);
		}
	}

}
