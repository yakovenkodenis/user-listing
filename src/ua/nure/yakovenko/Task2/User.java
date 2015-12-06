package ua.nure.yakovenko.Task2;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class User implements Serializable {

	private static final long serialVersionUID = -6825807152638963673L;
	
	static final Logger LOG = Logger.getLogger(User.class);

	String id;
	String role;
	String name;
	String email;
	String login;
	String password;

	public User(String id, String name, String email, String login, String password, String role) {
		this.id = id;
		this.name = name;
		this.role = role;
		this.email = email;
		this.login = login;
		this.password = password;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String toString() {
		return "Id: " + this.id + "\nName: " + this.name + "\nEmail: " + this.email + "\nLogin: " + this.login
				+ "\nRole: " + this.role + "\n";

	}
}
