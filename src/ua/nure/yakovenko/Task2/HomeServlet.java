package ua.nure.yakovenko.Task2;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.yakovenko.Task2.DbController;


@WebServlet(description = "The main servlet of the app. Should redirect unauthenticated users to to AuthServlet.", urlPatterns = { "/" })
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("HomeServlet doGet");
		if(!authenticateUser()) {
			response.sendRedirect("http://localhost:8080/Task2/authentication");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private boolean authenticateUser() {
		// TODO write user authentication
		return false;
	}
	
	private void attemptConnection() {
		try {
//			db.createNewUser("Ivan Bunin", "ivan_007", "ivan@gmail.com", "password");
			db.getUserByEmail("ivan@gmail.com");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

}
