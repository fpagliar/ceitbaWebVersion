package ar.edu.itba.paw.presentation;

public interface UserManager {

	public boolean existsUser();

	public String getName();

	public void setUser(String name);

	public void resetUser(String name);

}
