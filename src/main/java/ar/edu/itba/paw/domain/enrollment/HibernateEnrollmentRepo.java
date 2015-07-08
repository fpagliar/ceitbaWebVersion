package ar.edu.itba.paw.domain.enrollment;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
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
public class HibernateEnrollmentRepo extends AbstractHibernateRepo implements EnrollmentRepo {

	private static final int ELEMENTS_PER_PAGE = 15;

	@Autowired
	public HibernateEnrollmentRepo(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean add(final Enrollment enrollment) {
		final Criteria c = createCriteria(Enrollment.class);
		c.add(Restrictions.eq("person", enrollment.getPerson()));
		c.add(Restrictions.eq("service", enrollment.getService()));
		final List<Enrollment> list = (List<Enrollment>) c.list();
		for (final Enrollment e : list) {
			if (e.isActive()) {
				return false;		
			}
		}
		save(enrollment);
		enrollment.getPerson().enroll(enrollment);
		enrollment.getService().enroll(enrollment);
		return true;
	}

	@Override
	public Enrollment get(final int id) {
		return get(Enrollment.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> get(final Person p) {
		final Criteria c = createCriteria(Enrollment.class).add(Restrictions.eq("person", p));
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> get(final Service s) {
		final Criteria c = createCriteria(Enrollment.class).add(Restrictions.eq("service", s));
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getActive(final Person p, final Service s) {
		final Criteria c = createCriteria(Enrollment.class);
		c.add(Restrictions.eq("person", p));
		c.add(Restrictions.eq("service", s));
		c.add(Restrictions.isNotNull("endDate"));
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getAll() {
		final Criteria c = createCriteria(Enrollment.class);
		return (List<Enrollment>) c.list();
	}

	@Override
	public PaginatedResult<Enrollment> getExpired(final int page) {
		updateActive();
		final Criteria c = createCriteria(Enrollment.class).add(Restrictions.isNotNull("endDate"));
		c.createAlias("person", "p");
		c.addOrder(Order.asc("p.legacy"));
		return getPaginated(c, page - 1, ELEMENTS_PER_PAGE, Enrollment.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getExpired(final Person p) {
		updateActive();
		final Criteria c = createCriteria(Enrollment.class).add(Restrictions.isNotNull("endDate"));
		c.add(Restrictions.eq("person", p));
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getExpired(final Service s) {
		updateActive();
		final Criteria c = createCriteria(Enrollment.class).add(Restrictions.isNotNull("endDate"));
		c.add(Restrictions.eq("service", s));
		return (List<Enrollment>) c.list();
	}

	@Override
	public PaginatedResult<Enrollment> getActive(final int page) {
		updateActive();
		final Criteria c = createCriteria(Enrollment.class).add(Restrictions.isNull("endDate"));
		c.createAlias("person", "p");
		c.addOrder(Order.asc("p.legacy"));
		return getPaginated(c, page - 1, ELEMENTS_PER_PAGE, Enrollment.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getActive(final Person p) {
		updateActive();
		final Criteria c = createCriteria(Enrollment.class).add(Restrictions.isNull("endDate"));
		c.add(Restrictions.eq("person", p));
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getBilledActive(final Service s) {
		updateActive();
		final Criteria c = createCriteria(Enrollment.class).add(Restrictions.isNull("endDate"));
		c.add(Restrictions.eq("service", s));
		c.createAlias("person", "p").add(Restrictions.eq("p.paymentMethod", PaymentMethod.BILL));
		c.addOrder(Order.asc("p.legacy"));
		return (List<Enrollment>) c.list();
	}

	private void updateActive() {
		for (final Enrollment e : getAll()) {
			e.checkActive();			
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getBilledNewEnrollments(final Boolean personnel, final Service s, final DateTime start, 
			final DateTime end) {
		final Criteria c = createCriteria(Enrollment.class).add(Restrictions.eq("service", s));
		c.createAlias("person", "p").add(Restrictions.eq("p.paymentMethod", PaymentMethod.BILL));
		c.addOrder(Order.asc("p.legacy"));
		if (personnel) {
			c.add(Restrictions.le("p.legacy", 10000));			
		} else {
			c.add(Restrictions.ge("p.legacy", 10000));			
		}
		c.add(Restrictions.between("startDate", start, end));
		c.add(Restrictions.isNull("endDate"));
		return (List<Enrollment>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Enrollment> getBilledCancelledEnrollments(final Boolean personnel, final Service s, final DateTime start, 
			final DateTime end) {
		final Criteria c = createCriteria(Enrollment.class).add(Restrictions.eq("service", s));
		if (personnel) {
			c.createCriteria("person").add(Restrictions.le("legacy", 10000))
				.add(Restrictions.eq("paymentMethod", PaymentMethod.BILL));			
		} else {
			c.createCriteria("person").add(Restrictions.ge("legacy", 10000))
				.add(Restrictions.eq("paymentMethod", PaymentMethod.BILL));			
		}

		c.add(Restrictions.between("endDate", start, end));
		return (List<Enrollment>) c.list();
	}

	@Override
	public PaginatedResult<Enrollment> getActive(final Service s, final int page) {
		updateActive();
		final Criteria c = createCriteria(Enrollment.class).add(Restrictions.isNull("endDate"));
		c.add(Restrictions.eq("service", s));
		c.createAlias("person", "p");
		c.addOrder(Order.asc("p.legacy"));
		return getPaginated(c, page - 1, ELEMENTS_PER_PAGE, Enrollment.class);
	}
}