package ar.edu.itba.paw.presentation;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUserManager implements UserManager {
	private static String NAME_ID = "name";

	private final HttpServletRequest request;
	private final HttpServletResponse response;

	public CookieUserManager (final HttpServletRequest request, final HttpServletResponse response) {
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

	public void setUser(final String username) {
		setCookie(NAME_ID, username, false);
	}

	public void resetUser(final String username) {
		setCookie(NAME_ID, username, true);
	}

	private void setCookie(final String name, final String value, final boolean delete) {
		final Cookie cookie = new Cookie(name, value);
		if (delete) {
			cookie.setMaxAge(0);
		}
		response.addCookie(cookie);
	}

	private Cookie getCookie(final String name) {
		final Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(name)) {
					return c;
				}
			}
		}
		return null;
	}

	private String getByID(final String id) {
		final Cookie c = getCookie(id);
		if (c != null) {
			return c.getValue();
		} else {
			return request.getParameter(id);
		}
	}
}
