package ar.edu.itba.paw.domain.user;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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
		Criteria c = createCriteria(User.class).add(Restrictions.eq("username", username));
		return (User) c.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAll() {
		Criteria c = createCriteria(User.class);
		return (List<User>) c.list();
	}

	@SuppressWarnings("unchecked")
	private boolean duplicatedData(String field, String value) {
		Criteria c = createCriteria(User.class).add(Restrictions.eq(field, value));
		return ! ((List<User>) c.list()).isEmpty();
	}

	
	public void remove(User user){
		delete(user);
	}
}