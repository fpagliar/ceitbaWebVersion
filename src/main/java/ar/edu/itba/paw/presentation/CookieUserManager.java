package ar.edu.itba.paw.presentation;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUserManager implements UserManager {
	private static String NAME_ID = "name";

	private HttpServletRequest request;
	private HttpServletResponse response;

	public CookieUserManager ( HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public boolean existsUser() {
		if (getCookie(NAME_ID) != null) {
			return true;
		} else {
			String name = request.getParameter(NAME_ID);
			return name != null;
		}
	}

	public String getUsername() {
		return getByID(NAME_ID);
	}

	public void setUser(String username) {
		setCookie(NAME_ID, username, false);
	}

	public void resetUser(String username) {
		setCookie(NAME_ID, username, true);
	}

	private void setCookie(String name, String value, boolean delete) {
		Cookie cookie = new Cookie(name, value);
		if (delete) {
			cookie.setMaxAge(0);
		}
		response.addCookie(cookie);
	}

	private Cookie getCookie(String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for( Cookie c: cookies) {
				if (c.getName().equals(name)) {
					return c;
				}
			}
		}
		return null;
	}

	private String getByID(String id) {
		Cookie c = getCookie(id);
		if (c != null) {
			return c.getValue();
		} else {
			return request.getParameter(id);
		}
	}
}
