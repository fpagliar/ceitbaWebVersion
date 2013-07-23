package ar.edu.itba.paw.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import ar.edu.itba.paw.domain.PersistentEntity;

@Entity
@Table(name="my_user")
public class User extends PersistentEntity {

	@Column(name="username", unique=true, nullable=false)
	private String username;
	@Column(name="password", nullable=false)
	private String password;

	public static enum Level {ADMINISTRATOR, MODERATOR, REGULAR};
	@Enumerated(EnumType.STRING)
	@Column(name="level", nullable=false)
	private Level level;

	// Hibernate requirement
	User() {
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/* Getters */
	public String getUsername(){
		return this.username;
	}

	public Level getLevel(){
		return this.level;
	}
	
	public String getPassword(){
		return this.password;
	}

	@Override
	public String toString() {
		return "Username: " + username + "\n Password: " + password + "\n ------------------ ";
	}

	/* Setters */

	public void setPassword(String password) {
		validateEmpty(password);
		this.password = password;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
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
}
