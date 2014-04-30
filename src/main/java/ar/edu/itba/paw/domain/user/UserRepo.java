package ar.edu.itba.paw.domain.user;

import java.util.List;

public interface UserRepo {

	public void add(User user);

	public User get(int id);
	
	public User get(String username);
	
	public List<User> getAll();
	
	public void remove(User user);
	
}
