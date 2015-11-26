package ua.nure.yakovenko.Task2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.postgresql.util.PSQLException;

import ua.nure.yakovenko.Task2.Security;

public final class DbController {
	
	private Validator validator;
	
	@Resource(name="java:comp/env/jdbc/java_lab2")
	private DataSource ds;
	
	public DbController(Context ctx) {
		try {
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/java_lab2");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public User getUserByEmail(String email) throws SQLException {
		User u = null;
		Connection conn = null;
		PreparedStatement pstmt;
		
		try {
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement("SELECT * FROM users WHERE email = ? LIMIT 1;");
			pstmt.setString(1, email);
			
			System.out.println(pstmt.toString());
			
			ResultSet res = null;

			if(pstmt.execute()) {
				res = pstmt.getResultSet();
				res.next();
				
				System.out.println("RESULT: "+ (res == null));
				
				try {
					if (res != null) {
						u = new User(res.getString("id"), res.getString("name"), res.getString("email"),
								 res.getString("login"), res.getString("password"), res.getString("role"));
						
						System.out.println("USER INFO:\n" + u.toString());
					}
				} catch (PSQLException e) {
					u = new User(null, null, null, null, null, null);
				}
			}
			
			conn.commit();
			pstmt.close();
			
			System.out.println("GET USER By EMAIL: SUCCESS");

		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return u;
	}
	
	public void createNewUser(String name, String login, String email, String password) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt;
		
		try {
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement("INSERT INTO users (name, login, email, password, role) " +
					"VALUES (?, ?, ?, ?, ?);");
			pstmt.setString(1, name);
			pstmt.setString(2, login);
			pstmt.setString(3, email);
			pstmt.setString(4, Security.generateSHA256(password));
			pstmt.setString(5, "user");
			
			System.out.println(pstmt.toString());
			
			pstmt.executeUpdate();
			
			conn.commit();
			pstmt.close();
			
			System.out.println("CREATE NEW USER: SUCCESS");

		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public String validateUserExistsInDB(String email, String encryptedPassword,
			ResourceBundle bundle, boolean isSignup) {
		try {
			User u = getUserByEmail(email);
			if (isSignup && null != u.email) {
				return bundle.getString("validation.user_already_exists");
			} else {
				if (null == u.name || u.name == "") {
					return bundle.getString("validation.no_user_in_db");
				}
				else if (!u.password.trim().equals(encryptedPassword)){
					return bundle.getString("validation.wrong_pass_or_email");
				} else {
					System.out.println("VALID USER!");
					return null;
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> validateUserSignup(User u, ResourceBundle bundle) {
		validator = new Validator(bundle);
		return validator.validateUserAuth(u, false);
	}
	
	public ArrayList<String> validateUserLogin(User u, ResourceBundle bundle) {
		validator = new Validator(bundle);
		return validator.validateUserAuth(u, true);
	}
}
