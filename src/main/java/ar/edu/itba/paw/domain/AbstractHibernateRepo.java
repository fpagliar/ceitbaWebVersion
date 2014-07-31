package ar.edu.itba.paw.domain;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

public abstract class AbstractHibernateRepo {
	private final SessionFactory sessionFactory;

	public AbstractHibernateRepo(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> type, Serializable id) {
		return (T) getSession().get(type, id);
	}

//	@SuppressWarnings("unchecked")
//	public <T> List<T> find(String hql, Object... params) {
//		Session session = getSession();
//
//		Query query = session.createQuery(hql);
//		for (int i = 0; i < params.length; i++) {
//			query.setParameter(i, params[i]);
//		}
//		List<T> list = query.list();
//		return list;
//	}
//	
	/**
	 * Creates a session of criteria.
	 * 
	 * @return The criteria.
	 */
	protected Criteria createCriteria(Class<?> clazz) {
		return sessionFactory.getCurrentSession().createCriteria(clazz);
	}

	protected org.hibernate.classic.Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public Serializable save(Object o) {
		return getSession().save(o);
	}
	
	public void delete(Object o){
		getSession().delete(o);
		return;
	}
	
	public void update(Object o) {
		sessionFactory.getCurrentSession().update(o);
	}
}