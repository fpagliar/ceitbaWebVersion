package ar.edu.itba.paw.presentation.command;


public class RegisterUserForm {

	String username;
	String password;
	String rePassword;
	String level;

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
	
	public String getRePassword(){
		return this.rePassword;
	}
	
	public void setRePassword(String pass){
		this.rePassword = pass;
	}
	
	public String getLevel(){
		return level;
	}
	
	public void setLevel(String l){
		this.level = l;
	}
}