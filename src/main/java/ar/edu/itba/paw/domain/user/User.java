package ar.edu.itba.paw.domain.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.edu.itba.paw.domain.PersistentEntity;

@Entity
@Table(name = "my_user")
public class User extends PersistentEntity {

	@Column(name = "username", unique = true, nullable = false)
	private String username;
	@Column(name = "password", nullable = false)
	private String password;

	public static enum Level {
		ADMINISTRATOR, MODERATOR, REGULAR
	};

	@Enumerated(EnumType.STRING)
	@Column(name = "level", nullable = false)
	private Level level;

	@OneToMany
	private List<UserAction> actions = new ArrayList<UserAction>();

	// Hibernate requirement
	User() {
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/* Getters */
	public String getUsername() {
		return this.username;
	}

	public Level getLevel() {
		return this.level;
	}

	public String getPassword() {
		return this.password;
	}

	@Override
	public String toString() {
		return "id: " + getId() + " username: " + username + " password: " + password + " level: " + level;
	}

	/* Setters */

	public void setPassword(String password) {
		validateEmpty(password);
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		User user = (User) obj;
		return this.username == (user.getUsername());
	}

	@Override
	public int hashCode() {
		return getId();
	}

	private void validateEmpty(String s) {
		if (s == null || s.length() == 0) {
			throw new IllegalArgumentException();
		}
		return;
	}

	public boolean isAdmin() {
		return Level.ADMINISTRATOR == level;
	}

	public boolean isModerator() {
		return Level.MODERATOR == level || isAdmin();
	}

	public List<UserAction> getActions() {
		return actions;
	}

	public void reportAction(UserAction action) {
		actions.add(action);
	}
}
