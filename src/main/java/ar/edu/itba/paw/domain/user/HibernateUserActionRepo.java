package ar.edu.itba.paw.domain.user;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.user.UserAction.Action;
import ar.edu.itba.paw.domain.user.UserAction.ControllerType;

@Repository
public class HibernateUserActionRepo extends AbstractHibernateRepo implements
		UserActionRepo {

	@Autowired
	public HibernateUserActionRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public void add(UserAction action) {
		save(action);
	}

	@Override
	public UserAction get(int id) {
		return get(UserAction.class, id);
	}
	
	@Override
	public void remove(UserAction action){
		delete(action);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserAction> getAll(User user) {
		Criteria c = createCriteria(UserAction.class);
		c.add(Restrictions.eq("user", user));
		return (List<UserAction>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserAction> getAll() {
		Criteria c = createCriteria(UserAction.class);
		return (List<UserAction>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserAction> getAll(Action action) {
		Criteria c = createCriteria(UserAction.class);
		c.add(Restrictions.eq("action", action));
		return (List<UserAction>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserAction> getAll(ControllerType controller) {
		Criteria c = createCriteria(UserAction.class);
		c.add(Restrictions.eq("controller", controller));
		return (List<UserAction>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserAction> getAll(DateTime start, DateTime end) {
		Criteria c = createCriteria(UserAction.class);
		c.add(Restrictions.between("date", start, end));
		return (List<UserAction>) c.list();
	}
}