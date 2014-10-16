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
	public HibernateServiceRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public void add(Service service) {
		if (duplicatedData("name", service.getName())) {
			throw new DuplicatedDataException(service);
		}
		save(service);
	}

	@Override
	public Service get(int id) {
		return get(Service.class, id);
	}

	@Override
	public Service get(String name) {
		Criteria c = createCriteria(Service.class).add(
				Restrictions.eq("name", name));
		return (Service) c.uniqueResult();
	}

	@Override
	public PaginatedResult<Service> getAll(final int page) {
		Criteria c = createCriteria(Service.class);
		return getPaginated(c, page - 1, ELEMENTS_PER_PAGE, Service.class);
	}

	@Override
	public PaginatedResult<Service> getSports(final int page) {
		return getByType(Service.Type.SPORT, page);
	}

	@Override
	public PaginatedResult<Service> getActiveSports(final int page) {
		return getActiveByType(Service.Type.SPORT, page);
	}

	@Override
	public List<Service> getActiveSports() {
		return getActiveByType(Service.Type.SPORT);
	}

	@Override
	public PaginatedResult<Service> getCourses(final int page) {
		return getByType(Service.Type.COURSE, page);
	}

	@Override
	public PaginatedResult<Service> getActiveCourses(final int page) {
		return getActiveByType(Service.Type.COURSE, page);
	}

	@Override
	public List<Service> getActiveCourses() {
		return getActiveByType(Service.Type.COURSE);
	}

	@Override
	public PaginatedResult<Service> getOthers(final int page) {
		return getByType(Service.Type.OTHER, page);
	}

	@Override
	public PaginatedResult<Service> getActiveOthers(final int page) {
		return getActiveByType(Service.Type.OTHER, page);
	}
	
	@Override
	public List<Service> getActiveOthers() {
		return getActiveByType(Service.Type.OTHER);
	}

	@Override
	public PaginatedResult<Service> getLockers(final int page) {
		return getByType(Service.Type.LOCKER, page);
	}

	@Override
	public PaginatedResult<Service> getActiveLockers(final int page) {
		return getActiveByType(Service.Type.LOCKER, page);
	}
	
	@Override
	public List<Service> getActiveLockers() {
		return getActiveByType(Service.Type.LOCKER);
	}

	@Override
	public PaginatedResult<Service> getCommons(final int page) {
		return getByType(Service.Type.COMMON, page);
	}

	@Override
	public PaginatedResult<Service> getActiveCommons(final int page) {
		return getActiveByType(Service.Type.COMMON, page);
	}
	
	@Override
	public List<Service> getActiveCommons() {
		return getActiveByType(Service.Type.COMMON);
	}

	private PaginatedResult<Service> getByType(final Service.Type type, final int page) {
		Criteria c = createCriteria(Service.class).add(Restrictions.eq("type", type));
		return getPaginated(c, page - 1, ELEMENTS_PER_PAGE, Service.class);
	}

	private PaginatedResult<Service> getActiveByType(final Service.Type type, final int page) {
		Criteria c = createCriteria(Service.class).add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("status", Service.Status.ACTIVE));
		return getPaginated(c, page - 1, ELEMENTS_PER_PAGE, Service.class);
	}

	@SuppressWarnings("unchecked")
	private List<Service> getActiveByType(final Service.Type type) {
		Criteria c = createCriteria(Service.class).add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("status", Service.Status.ACTIVE));
		return (List<Service>) c.list();
	}

	@Override
	public PaginatedResult<Service> getActive(final int page) {
		Criteria c = createCriteria(Service.class).add(Restrictions.eq("status", Service.Status.ACTIVE));
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
		Criteria c = createCriteria(Service.class).add(Restrictions.eq("status", Service.Status.INACTIVE));
		return getPaginated(c, page - 1, ELEMENTS_PER_PAGE, Service.class);
	}

	@Override
	public PaginatedResult<Service> search(final String s, final int page) {
		Criteria c = createCriteria(Service.class).add(Restrictions.ilike("name", s, MatchMode.ANYWHERE));
		return getPaginated(c, page - 1, ELEMENTS_PER_PAGE, Service.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Service> search(final String s) {
		final Criteria c = createCriteria(Service.class).add(Restrictions.ilike("name", s, MatchMode.ANYWHERE));
		return (List<Service>) c.list();
	}


	private boolean duplicatedData(String field, String value) {
		Criteria c = createCriteria(Service.class).add(Restrictions.eq(field, value));
		return ! c.list().isEmpty();
	}
}