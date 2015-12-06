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

@WebFilter(filterName="AuthenticationFilter")
public class AuthenticationFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String path = req.getRequestURI();

		System.out.println("FILTER PATH\n" + path);

		HttpSession session = req.getSession(false);
		User u = null;
		if (session != null) {
			u = (User) session.getAttribute("user");
		}

		if (path.contains("/authentication")) {
			if (u != null) {
				res.sendRedirect(request.getServletContext().getContextPath() + "/");
			} else {
				chain.doFilter(request, response);
			}
		} else {
			if (u == null) {
				res.sendRedirect("authentication");
			} else {
				chain.doFilter(request, response);
			}
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
