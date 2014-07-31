package ar.edu.itba.paw.domain.user;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.DuplicatedDataException;
import ar.edu.itba.paw.domain.enrollment.Enrollment;
import ar.edu.itba.paw.domain.payment.Debt;
import ar.edu.itba.paw.domain.user.Person.PaymentMethod;

@Repository
public class HibernatePersonRepo extends AbstractHibernateRepo implements
		PersonRepo {

	@Autowired
	public HibernatePersonRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Person> getAll() {
		Criteria c = createCriteria(Person.class);
		return (List<Person>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Person> search(String s) {
		List<Person> ans = new ArrayList<Person>();
		try {
			Person p = getByLegacy(Integer.valueOf(s));
			ans.add(p);
			return ans;
		} catch (NumberFormatException e) {
		}
		Criteria c = createCriteria(Person.class);
		c.add(Restrictions.or(Restrictions.ilike("firstName", s),
				Restrictions.ilike("lastName", s)));
		ans.addAll((List<Person>) c.list());
		return ans;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Debt> billCashPayments() {
		Criteria c = createCriteria(Person.class).add(
				Restrictions.eq("paymentMethod", PaymentMethod.CASH));
		List<Person> list = (List<Person>) c.list();
		List<Debt> debts = new ArrayList<Debt>();
		for (Person p : list) {
			double total = 0;
			String reason = "";
			for(Enrollment e : p.getActiveEnrollments())
				if (e.getService().getValue() > 0){
					total += e.getService().getValue();
					reason += e.getService().getName() + " ";
				}
			if(total > 0)
				debts.add(new Debt(p, total, DateTime.now(), reason));						
		}
		return debts;
	}

}