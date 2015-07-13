package ar.edu.itba.paw.domain.user;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.DuplicatedDataException;
import ar.edu.itba.paw.domain.PaginatedResult;
import ar.edu.itba.paw.domain.enrollment.Enrollment;
import ar.edu.itba.paw.domain.enrollment.EnrollmentRepo;
import ar.edu.itba.paw.domain.enrollment.Purchase;
import ar.edu.itba.paw.domain.enrollment.PurchaseRepo;
import ar.edu.itba.paw.domain.payment.Debt;
import ar.edu.itba.paw.domain.service.Product;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.user.Person.PaymentMethod;

@Repository
public class HibernatePersonRepo extends AbstractHibernateRepo implements
		PersonRepo {
	
	private static final int ELEMENTS_PER_PAGE = 15;

	private final EnrollmentRepo enrollmentRepo;
	private final PurchaseRepo purchaseRepo;
	
	@Autowired
	public HibernatePersonRepo(final SessionFactory sessionFactory, final EnrollmentRepo enrollmentRepo, 
			final PurchaseRepo purchaseRepo) {
		super(sessionFactory);
		this.enrollmentRepo = enrollmentRepo;
		this.purchaseRepo = purchaseRepo;
	}

	/**
	 * @param person
	 *            - the user that has to be added.
	 * @throws DuplicatedUserException
	 *             if the username or mail already exists for another user
	 */
	@Override
	public void add(Person person) {
		Criteria c = createCriteria(Person.class);
		c.add(Restrictions.eq("legacy", person.getLegacy()));
		if (!c.list().isEmpty())
			throw new DuplicatedDataException(person);
		save(person);
	}

	@Override
	public Person getById(int id) {
		return get(Person.class, id);
	}

	public Person getByLegacy(int legacy) {
		Criteria c = createCriteria(Person.class).add(
				Restrictions.eq("legacy", legacy));
		return (Person) c.uniqueResult();
	}

	public Person getByDni(String dni) {
		Criteria c = createCriteria(Person.class).add(
				Restrictions.eq("dni", dni));
		return (Person) c.uniqueResult();
	}

	@Override
	public PaginatedResult<Person> getAll(final int page) {
		Criteria c = createCriteria(Person.class);
		c.addOrder(Order.asc("legacy"));
		return getPaginated(c, page - 1, ELEMENTS_PER_PAGE, Person.class);
	}

	@Override
	public PaginatedResult<Person> search(final String s, final int page) {
		try {
			Person p = getByLegacy(Integer.valueOf(s));
			final List<Person> elements = new ArrayList<Person>();
			elements.add(p);
			return new PaginatedResult<Person>(1, elements, 1, 1);
		} catch (NumberFormatException e) { }
		Criteria c = createCriteria(Person.class);
		c.add(Restrictions.or(Restrictions.ilike("firstName", s, MatchMode.ANYWHERE),
				Restrictions.ilike("lastName", s, MatchMode.ANYWHERE)));
		c.addOrder(Order.asc("legacy"));
		return getPaginated(c, page-1, ELEMENTS_PER_PAGE, Person.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Debt> billCashPayments() {
		final Criteria c = createCriteria(Person.class).add(Restrictions.eq("paymentMethod", PaymentMethod.CASH));
		final List<Person> list = (List<Person>) c.list();
		final List<Debt> debts = new ArrayList<Debt>();
		for (final Person p : list) {
			double total = 0;
			String reason = "";
			for (final Enrollment e : enrollmentRepo.getActive(p)) {
				if (e.getService().getValue() > 0) {
					final Service service = e.getService();
					total += service.getValue();
					reason += service.getName() + ":" + service.getValue() + " ";
				}				
			}
			for (final Purchase purchase : purchaseRepo.getPending(p)) {
				final Product product = purchase.getProduct();
				total += product.getValue();
				reason += product.getName() + ":" + product.getValue() + " ";
				purchase.bill();
				purchaseRepo.save(purchase);
			}
			if(total > 0) {
				debts.add(new Debt(p, total, DateTime.now(), reason));						
			}
		}
		return debts;
	}

}