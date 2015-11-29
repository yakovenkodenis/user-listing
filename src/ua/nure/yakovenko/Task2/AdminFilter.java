package ua.nure.yakovenko.Task2;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AdminFilter")
public class AdminFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String path = req.getRequestURI();

		System.out.println("ADMIN FILTER PATH\n" + path);

		HttpSession session = req.getSession(false);
		User u = null;
		if (session != null) {
			u = (User) session.getAttribute("user");
		}

		if (u != null) {
			if (u.getRole().equals("admin")) {
				chain.doFilter(request, response);
			} else {
				res.sendRedirect("/Task2/");
			}
		} else {
			res.sendRedirect("authentication");
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
