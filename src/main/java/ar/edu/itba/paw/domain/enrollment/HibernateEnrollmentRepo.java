package ar.edu.itba.paw.domain.enrollment;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.PaginatedResult;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.Person.PaymentMethod;

@Repository
public class HibernateEnrollmentRepo extends AbstractHibernateRepo implements
		EnrollmentRepo {

	private static final int ELEMENTS_PER_PAGE = 15;

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
	@SuppressWarnings("unchecked")
	@Override
	public void add(Enrollment enrollment) {
		Criteria c = createCriteria(Enrollment.class);
		c.add(Restrictions.eq("person", enrollment.getPerson()));
		c.add(Restrictions.eq("service", enrollment.getService()));
		List<Enrollment> list = (List<Enrollment>) c.list();
		for (Enrollment e : list)
			if (e.isActive()
					&& !Service.Type.OTHER.equals(e.getService().getType())) {
				return; // The subscriptions typed OTHER can have unlimited
						// active enrollments in the same period
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
	public List<Enrollment> get(Person p) {
		Criteria c = createCriteria(Enrollment.class).add(
				Restrictions.eq("person", p));
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> get(Service s) {
		Criteria c = createCriteria(Enrollment.class).add(
				Restrictions.eq("service", s));
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> get(Person p, Service s) {
		Criteria c = createCriteria(Enrollment.class).add(
				Restrictions.eq("person", p));
		c.add(Restrictions.eq("service", s));
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getAll() {
		Criteria c = createCriteria(Enrollment.class);
		return (List<Enrollment>) c.list();
	}

	@Override
	public PaginatedResult<Enrollment> getExpired(final int page) {
		updateActive();
		Criteria c = createCriteria(Enrollment.class).add(
				Restrictions.isNotNull("endDate"));
		return getPaginated(c, page - 1, ELEMENTS_PER_PAGE, Enrollment.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getExpired(Person p) {
		updateActive();
		Criteria c = createCriteria(Enrollment.class).add(
				Restrictions.isNotNull("endDate"));
		c.add(Restrictions.eq("person", p));
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getExpired(Service s) {
		updateActive();
		Criteria c = createCriteria(Enrollment.class).add(
				Restrictions.isNotNull("endDate"));
		c.add(Restrictions.eq("service", s));
		return (List<Enrollment>) c.list();
	}

	@Override
	public PaginatedResult<Enrollment> getActive(final int page) {
		updateActive();
		Criteria c = createCriteria(Enrollment.class).add(
				Restrictions.isNull("endDate"));
		return getPaginated(c, page - 1, ELEMENTS_PER_PAGE, Enrollment.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getActive(Person p) {
		updateActive();
		Criteria c = createCriteria(Enrollment.class).add(
				Restrictions.isNull("endDate"));
		c.add(Restrictions.eq("person", p));
		return (List<Enrollment>) c.list();
	}

//	@Override
//	public List<Enrollment> getActivePersonsList(List<Person> persons, final int page) {
//		updateActive();
//		List<Enrollment> all = new ArrayList<Enrollment>();
//		for (Person p : persons)
//			all.addAll(getActive(p));
//		return all;
//	}
//
//	@Override
//	public List<Enrollment> getActiveServiceList(List<Service> services) {
//		updateActive();
//		List<Enrollment> all = new ArrayList<Enrollment>();
//		for (Service s : services)
//			all.addAll(getActive(s));
//		return all;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Enrollment> getActive(Service s) {
//		updateActive();
//		Criteria c = createCriteria(Enrollment.class).add(
//				Restrictions.isNull("endDate"));
//		c.add(Restrictions.eq("service", s));
//		return (List<Enrollment>) c.list();
//	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getBilledActive(Service s) {
		updateActive();
		Criteria c = createCriteria(Enrollment.class).add(
				Restrictions.isNull("endDate"));
		c.add(Restrictions.eq("service", s));
		c.createCriteria("person").add(
				Restrictions.eq("paymentMethod", PaymentMethod.BILL));
		return (List<Enrollment>) c.list();
	}

	private void updateActive() {
		for (Enrollment e : getAll())
			e.checkActive();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getBilledNewEnrollments(Boolean personnel,
			Service s, DateTime start, DateTime end) {
		Criteria c = createCriteria(Enrollment.class).add(
				Restrictions.eq("service", s));
		if (personnel)
			c.createCriteria("person").add(Restrictions.le("legacy", 10000))
					.add(Restrictions.eq("paymentMethod", PaymentMethod.BILL));
		else
			c.createCriteria("person").add(Restrictions.ge("legacy", 10000))
					.add(Restrictions.eq("paymentMethod", PaymentMethod.BILL));

		c.add(Restrictions.between("startDate", start, end));
		c.add(Restrictions.isNull("endDate"));
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getBilledCancelledEnrollments(Boolean personnel,
			Service s, DateTime start, DateTime end) {
		Criteria c = createCriteria(Enrollment.class).add(
				Restrictions.eq("service", s));
		if (personnel)
			c.createCriteria("person").add(Restrictions.le("legacy", 10000))
					.add(Restrictions.eq("paymentMethod", PaymentMethod.BILL));
		else
			c.createCriteria("person").add(Restrictions.ge("legacy", 10000))
					.add(Restrictions.eq("paymentMethod", PaymentMethod.BILL));

		c.add(Restrictions.between("endDate", start, end));
		return (List<Enrollment>) c.list();
	}

	@Override
	public PaginatedResult<Enrollment> getActive(final Service s, final int page) {
		updateActive();
		Criteria c = createCriteria(Enrollment.class).add(
				Restrictions.isNull("endDate"));
		c.add(Restrictions.eq("service", s));
		return getPaginated(c, page - 1, ELEMENTS_PER_PAGE, Enrollment.class);
	}
}