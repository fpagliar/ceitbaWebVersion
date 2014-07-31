package ar.edu.itba.paw.domain.user;

import java.util.List;

import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.user.UserAction.Action;
import ar.edu.itba.paw.domain.user.UserAction.ControllerType;

public interface UserActionRepo {

	public void add(UserAction action);

	public UserAction get(int id);
	
	public List<UserAction> getAll();

	public List<UserAction> getAll(User user);
	
	public List<UserAction> getAll(Action action);
	
	public List<UserAction> getAll(ControllerType controller);

	public List<UserAction> getAll(DateTime start, DateTime end);

	public void remove(UserAction action);
	
}
