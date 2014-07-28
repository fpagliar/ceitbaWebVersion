package ar.edu.itba.paw.domain.service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.DuplicatedDataException;
import ar.edu.itba.paw.domain.user.Person;

@Repository
public class HibernateServiceRepo extends AbstractHibernateRepo implements
		ServiceRepo {

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
		// List<Service> services = find("from Service where name = ?", name);
		// if(services.size() == 0)
		// return null;
		// if(services.size() == 1)
		// return services.get(0);
		// throw new RuntimeException("An internal error occurred");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Service> getAll() {
		Criteria c = createCriteria(Service.class);
		return (List<Service>) c.list();
//		return find("from Service");
	}

	@Override
	public List<Service> getSports() {
		return getByType(Service.Type.SPORT);
	}

	@Override
	public List<Service> getActiveSports() {
		return getActiveByType(Service.Type.SPORT);
	}

	@Override
	public List<Service> getCourses() {
		return getByType(Service.Type.COURSE);
	}

	@Override
	public List<Service> getActiveCourses() {
		return getActiveByType(Service.Type.COURSE);
	}

	@Override
	public List<Service> getOthers() {
		return getByType(Service.Type.OTHER);
	}

	@Override
	public List<Service> getActiveOthers() {
		return getActiveByType(Service.Type.OTHER);
	}

	@Override
	public List<Service> getLockers() {
		return getByType(Service.Type.LOCKER);
	}

	@Override
	public List<Service> getActiveLockers() {
		return getActiveByType(Service.Type.LOCKER);
	}

	@SuppressWarnings("unchecked")
	private List<Service> getByType(Service.Type type) {
		Criteria c = createCriteria(Service.class).add(Restrictions.eq("type", type));
		return (List<Service>) c.list();
//		return find("from Service where type = ?", type);
	}

	@SuppressWarnings("unchecked")
	private List<Service> getActiveByType(Service.Type type) {
		Criteria c = createCriteria(Service.class).add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("status", Service.Status.ACTIVE));
		return (List<Service>) c.list();
//		return find("from Service where type = ? AND status = 'ACTIVE'", type);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Service> getActive() {
		Criteria c = createCriteria(Service.class).add(Restrictions.eq("status", Service.Status.ACTIVE));
		return (List<Service>) c.list();
//		return find("from Service where status = 'ACTIVE'");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Service> getInactive() {
		Criteria c = createCriteria(Service.class).add(Restrictions.eq("status", Service.Status.INACTIVE));
		return (List<Service>) c.list();
//		return find("from Service where status = 'INACTIVE'");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Service> search(String s) {
		Criteria c = createCriteria(Service.class).add(Restrictions.ilike("name", s));
		return (List<Service>) c.list();
//		List<Service> ans = new ArrayList<Service>();
//		List<Service> all = find("from Service where UPPER(name) like ?", "%"
//				+ s.toUpperCase() + "%");
//		ans.addAll(all);
//		return ans;
	}

	private boolean duplicatedData(String field, String value) {
		Criteria c = createCriteria(Service.class).add(Restrictions.eq(field, value));
		return ! c.list().isEmpty();
//		return !find("from Service where ? = ?", field, value).isEmpty();
	}

}