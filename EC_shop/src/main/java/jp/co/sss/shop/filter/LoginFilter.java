package jp.co.sss.shop.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class LoginFilter extends HttpFilter {
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	throws IOException, ServletException {
		String requestURL = request.getRequestURI();
		// 静的リソースへのアクセスをフィルタリングしない
		if (requestURL.startsWith("/resources/") || requestURL.startsWith("/static/") ||
				requestURL.startsWith("/css/") || requestURL.startsWith("/js/") ||
				requestURL.endsWith("/login") || requestURL.endsWith("/userRegister")) {
			chain.doFilter(request, response);
		} else {
			HttpSession session = request.getSession();
			String userId = (String) session.getAttribute("userId");
			if (userId == null) {
				response.sendRedirect("/shop/login");
				return;
			} else {
				chain.doFilter(request, response);
			}
		}
	}
}
