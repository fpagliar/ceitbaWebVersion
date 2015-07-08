package ar.edu.itba.paw.domain.service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.DuplicatedDataException;
import ar.edu.itba.paw.domain.PaginatedResult;

@Repository
public class HibernateServiceRepo extends AbstractHibernateRepo implements
		ServiceRepo {

	private static final int ELEMENTS_PER_PAGE = 15;

	@Autowired
	public HibernateServiceRepo(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public void add(final Service service) {
		if (duplicatedData("name", service.getName())) {
			throw new DuplicatedDataException(service);
		}
		save(service);
	}

	@Override
	public Service get(final int id) {
		return get(Service.class, id);
	}

	@Override
	public Service get(final String name) {
		final Criteria c = createCriteria(Service.class).add(Restrictions.eq("name", name));
		return (Service) c.uniqueResult();
	}

	@Override
	public PaginatedResult<Service> getAll(final int page) {
		final Criteria c = createCriteria(Service.class);
		return getPaginated(c, page - 1, ELEMENTS_PER_PAGE, Service.class);
	}

	@Override
	public PaginatedResult<Service> getActive(final int page) {
		final Criteria c = createCriteria(Service.class).add(Restrictions.eq("status", Service.Status.ACTIVE));
		return getPaginated(c, page - 1, ELEMENTS_PER_PAGE, Service.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Service> getActive() {
		final Criteria c = createCriteria(Service.class).add(Restrictions.eq("status", Service.Status.ACTIVE));
		return (List<Service>) c.list();
	}

	@Override
	public PaginatedResult<Service> getInactive(final int page) {
		final Criteria c = createCriteria(Service.class).add(Restrictions.eq("status", Service.Status.INACTIVE));
		return getPaginated(c, page - 1, ELEMENTS_PER_PAGE, Service.class);
	}

	@Override
	public PaginatedResult<Service> search(final String s, final int page) {
		final Criteria c = createCriteria(Service.class).add(Restrictions.ilike("name", s, MatchMode.ANYWHERE));
		return getPaginated(c, page - 1, ELEMENTS_PER_PAGE, Service.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Service> search(final String s) {
		final Criteria c = createCriteria(Service.class).add(Restrictions.ilike("name", s, MatchMode.ANYWHERE));
		return (List<Service>) c.list();
	}

	private boolean duplicatedData(final String field, final String value) {
		final Criteria c = createCriteria(Service.class).add(Restrictions.eq(field, value));
		return ! c.list().isEmpty();
	}
}