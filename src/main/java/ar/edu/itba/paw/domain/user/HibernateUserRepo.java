package ar.edu.itba.paw.domain.user;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.DuplicatedDataException;

@Repository
public class HibernateUserRepo extends AbstractHibernateRepo implements
		UserRepo {

	@Autowired
	public HibernateUserRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	/**
	 * @param user
	 *            - the user that has to be added.
	 * @throws DuplicatedUserException
	 *             if the username or mail already exists for another user
	 */
	@Override
	public void add(User user) {
		if (duplicatedData("username", "" + user.getUsername())) {
			throw new DuplicatedDataException(user);
		}
		save(user);
	}

	@Override
	public User get(int id) {
		return get(User.class, id);
	}
	
	@Override
	public User get(String username){
		List<User> users = find("from User where username = ?", username);
		if(users.size() == 0)
			return null;
		if(users.size() == 1)
			return users.get(0);
		throw new RuntimeException("An internal error occurred");
	}

	@Override
	public List<User> getAll() {
		return find("from User");
	}

	private boolean duplicatedData(String field, String value) {
		return !find("from User where ? = ?", field, value).isEmpty();
	}

//	@Override
//	public List<Person> getAllAdministrators() {
//		return find("from User where level = '" + Person.Level.ADMINISTRATOR + "'");
//	}
//
//	@Override
//	public List<Person> getAllRegularUsers() {
//		return find("from User where level = '" + Person.Level.REGULAR + "'");
//	}
}