package ar.edu.itba.paw.presentation;

public interface UserManager {

	public boolean existsUser();

	public String getUsername();

	public void setUser(String username);

	public void resetUser(String username);

}
