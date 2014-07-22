package ar.edu.itba.paw.domain.payment;

import java.util.List;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.lib.DateHelper;

@Repository
public class HibernateDebtRepo extends AbstractHibernateRepo implements
		DebtRepo {

	@Autowired
	public HibernateDebtRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public List<Debt> getAll() {
		return find("from Debt");
	}

	@Override
	public List<Debt> get(Person person) {
		return find("from Debt where person = ?", person);
	}

	@Override
	public List<Debt> get(DateTime start, DateTime end) {
		if (start == null)
			start = DateTime.parse("0");
		if (end == null)
			end = DateTime.now();
		return find("from Debt where billingDate > ? and billingDate < ?",
				DateHelper.getNormalizedDate(start),
				DateHelper.getNormalizedDate(end));
	}

	@Override
	public List<Debt> get(Person p, DateTime start, DateTime end) {
		if (p == null)
			return get(start, end);
		if (start == null)
			start = DateTime.parse("0");
		if (end == null)
			end = DateTime.now();
		return find(
				"from Debt where billingDate > ? and billingDate < ? and person = ?",
				DateHelper.getNormalizedDate(start),
				DateHelper.getNormalizedDate(end), p);
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
