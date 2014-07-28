package ar.edu.itba.paw.domain.enrollment;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.user.Person;

@Repository
public class HibernateEnrollmentRepo extends AbstractHibernateRepo implements
		EnrollmentRepo {

	@Autowired
	public HibernateEnrollmentRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	/**
	 * @param person
	 *            - the user that has to be added.
	 * @throws DuplicatedUserException
	 *             if the username or mail already exists for another user
	 */
	@Override
	public void add(Enrollment enrollment) {
		List<Enrollment> list = find("from Enrollment where person = ? and service = ?", enrollment.getPerson(), enrollment.getService()); 
		for(Enrollment e: list)
				if(e.isActive() && !Service.Type.OTHER.equals(e.getService().getType())){
					return;		// The subscriptions typed OTHER can have unlimited active enrollments in the same period				
				}
		save(enrollment);
		enrollment.getPerson().enroll(enrollment);
		enrollment.getService().enroll(enrollment);
	}

	@Override
	public Enrollment get(int id) {
		return get(Enrollment.class, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> get(Person p){
		Criteria c = createCriteria(Enrollment.class).add(Restrictions.eq("person", p));
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> get(Service s){
		Criteria c = createCriteria(Enrollment.class).add(Restrictions.eq("service", s));
		return (List<Enrollment>) c.list();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> get(Person p, Service s) {
		Criteria c = createCriteria(Enrollment.class).add(Restrictions.eq("person", p));
		c.add(Restrictions.eq("service", s));
		return (List<Enrollment>) c.list();
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getAll() {
		Criteria c = createCriteria(Enrollment.class);
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getExpired() {
		updateActive();
		Criteria c = createCriteria(Enrollment.class).add(Restrictions.isNotNull("endDate"));
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getExpired(Person p) {
		updateActive();
		Criteria c = createCriteria(Enrollment.class).add(Restrictions.isNotNull("endDate"));
		c.add(Restrictions.eq("person", p));
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getExpired(Service s) {
		updateActive();
		Criteria c = createCriteria(Enrollment.class).add(Restrictions.isNotNull("endDate"));
		c.add(Restrictions.eq("service", s));		
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getActive() {
		updateActive();
		Criteria c = createCriteria(Enrollment.class).add(Restrictions.isNull("endDate"));
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getActive(Person p) {
		updateActive();
		Criteria c = createCriteria(Enrollment.class).add(Restrictions.isNull("endDate"));
		c.add(Restrictions.eq("person", p));
		return (List<Enrollment>) c.list();
	}

	@Override
	public List<Enrollment> getActivePersonsList(List<Person> persons) {
		updateActive();
		List<Enrollment> all = new ArrayList<Enrollment>();
		for(Person p : persons)
			all.addAll(getActive(p));
		return all;
	}

	@Override
	public List<Enrollment> getActiveServiceList(List<Service> services) {
		updateActive();
		List<Enrollment> all = new ArrayList<Enrollment>();
		for(Service s : services)
			all.addAll(getActive(s));
		return all;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getActive(Service s) {
		updateActive();
		Criteria c = createCriteria(Enrollment.class).add(Restrictions.isNull("endDate"));
		c.add(Restrictions.eq("service", s));		
		return (List<Enrollment>) c.list();
	}
	
	private void updateActive(){
		for(Enrollment e: getAll())
			e.checkActive();
	}
}