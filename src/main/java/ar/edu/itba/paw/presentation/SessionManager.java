package ar.edu.itba.paw.presentation;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionManager implements UserManager {
	private static String NAME_ID = "name";

	private HttpSession session;

	SessionManager() {
	}
	
	@Autowired
	public SessionManager(final HttpSession session) {
		this.session = session;
	}

	public boolean existsUser() {
		return session.getAttribute(NAME_ID) != null;
	}
	
	@Override
	public String getUsername() {
		return (String) session.getAttribute(NAME_ID);
	}
	
	@Override
	public void setUser(final String username) {
		session.setAttribute(NAME_ID, username);
	}
	
	@Override
	public void resetUser(final String username) {
		session.setAttribute(NAME_ID, null);
	}
}
