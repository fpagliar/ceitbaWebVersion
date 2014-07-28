package ar.edu.itba.paw.domain.payment;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.user.Person;

@Repository
public class HibernateDebtRepo extends AbstractHibernateRepo implements
		DebtRepo {

	@Autowired
	public HibernateDebtRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Debt> getAll() {
		Criteria c = createCriteria(Debt.class);
		return (List<Debt>) c.list();
//		return find("from Debt");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Debt> get(Person person) {
		Criteria c = createCriteria(Debt.class).add(Restrictions.eq("person", person));
		return (List<Debt>) c.list();
//		return find("from Debt where person = ?", person);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Debt> get(DateTime start, DateTime end) {
		Criteria c = createCriteria(Debt.class);
		if(start != null)
			c.add(Restrictions.gt("billingDate", start));
		if(end != null)
			c.add(Restrictions.lt("billingDate", end));
		return (List<Debt>) c.list();
//		if (start == null)
//			start = DateTime.parse("0");
//		if (end == null)
//			end = DateTime.now();
//		return find("from Debt where billingDate > ? and billingDate < ?",
//				DateHelper.getNormalizedDate(start),
//				DateHelper.getNormalizedDate(end));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Debt> get(Person p, DateTime start, DateTime end) {
		Criteria c = createCriteria(Debt.class);
		if(p != null)
			c.add(Restrictions.eq("person", p));
		if(start != null)
			c.add(Restrictions.gt("billingDate", start));
		if(end != null)
			c.add(Restrictions.lt("billingDate", end));
		return (List<Debt>) c.list();
//		if(start != null)
//			c.add(Restrictions.gt("billingDate", start));
//		if (p == null)
//			return get(start, end);
//		if (start == null)
//			start = DateTime.parse("0");
//		if (end == null)
//			end = DateTime.now();
//		return find(
//				"from Debt where billingDate > ? and billingDate < ? and person = ?",
//				DateHelper.getNormalizedDate(start),
//				DateHelper.getNormalizedDate(end), p);
	}

	@Override
	public void add(Debt debt) {
		save(debt);
	}

	@Override
	public void pay(Debt debt, DateTime date) {
		debt.pay(date);
		delete(debt);
		return;
	}

	@Override
	public void pay(Debt debt) {
		pay(debt, DateTime.now());
	}

	@Override
	public Debt get(int id) {
		return get(Debt.class, id);
	}

}
