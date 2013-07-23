package ar.edu.itba.paw.presentation.command;

import ar.edu.itba.paw.domain.user.User.Level;

public class RegisterUserForm {

	String username;
	String password;
	String retipedPassword;
	Level level;

	public RegisterUserForm() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String name) {
		this.username = name;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String pass){
		this.password = pass;
	}
	
	public String getRetipedPassword(){
		return this.retipedPassword;
	}
	
	public void setRetipedPassword(String pass){
		this.retipedPassword = pass;
	}
	
	public Level getLevel(){
		return level;
	}
	
	public void setLevel(Level l){
		this.level = l;
	}
}