package ar.edu.itba.paw.domain;

import java.io.Serializable;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.impl.CriteriaImpl;

public abstract class AbstractHibernateRepo {
	private final SessionFactory sessionFactory;

	public AbstractHibernateRepo(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> type, Serializable id) {
		return (T) getSession().get(type, id);
	}

	// @SuppressWarnings("unchecked")
	// public <T> List<T> find(String hql, Object... params) {
	// Session session = getSession();
	//
	// Query query = session.createQuery(hql);
	// for (int i = 0; i < params.length; i++) {
	// query.setParameter(i, params[i]);
	// }
	// List<T> list = query.list();
	// return list;
	// }
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

	public void delete(Object o) {
		getSession().delete(o);
		return;
	}

	public void update(Object o) {
		sessionFactory.getCurrentSession().update(o);
	}

	@SuppressWarnings("unchecked")
	public <E> PaginatedResult<E> getPaginated(final Criteria criteria, final int page, final int amount, Class<E> clazz) {
		try {
			final Session session = sessionFactory.getCurrentSession();
			final CloneableCriteria cc = new CloneableCriteria((CriteriaImpl) criteria);
			final CriteriaImpl clone = (CriteriaImpl) cc.clone().getExecutableCriteria(session);
			final Iterator<?> iterator = clone.iterateOrderings();
			while (iterator.hasNext()) {
				iterator.next();
				iterator.remove();
			}
			final long totalElements = getCount(clone);
			int pageNumber = page;
			if (pageNumber * amount >= totalElements) {
				pageNumber = (int) Math.ceil(totalElements / (double) amount) - 1;
			}
			criteria.setFirstResult(pageNumber * amount).setMaxResults(amount);
			return new PaginatedResult<E>(pageNumber + 1, criteria.list(), totalElements, amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Retrieves the count of rows matching the given criteria Beware, the given
	 * criteria is modified by this method
	 * 
	 * @param criteria
	 *            The criteria for which to get matching row count
	 * @return The number of rows matching the given criteria
	 */
	protected long getCount(final Criteria criteria) {
		return new Long((Integer)criteria.setProjection(Projections.rowCount()).uniqueResult());
	}

	/**
	 * Criteria that allows to be cloneable.
	 * 
	 * @author jsotuyod
	 */
	private static class CloneableCriteria extends DetachedCriteria implements Cloneable {
		private static final long serialVersionUID = 6483528856617926063L;

		public CloneableCriteria(final CriteriaImpl criteria) {
			super(criteria, criteria);
		}

		// CHECKSTYLE:OFF
		@Override
		protected CloneableCriteria clone() throws CloneNotSupportedException {
			return SerializationUtils.clone(this);
		}
		// CHECKSTYLE:ON
	}
}