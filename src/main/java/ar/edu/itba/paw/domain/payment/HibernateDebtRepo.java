package ar.edu.itba.paw.domain.payment;

import java.util.List;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.user.Person;

public class HibernateDebtRepo extends AbstractHibernateRepo implements
		DebtRepo {

	@Autowired
	public HibernateDebtRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public List<Debt> getAll() {
		return find("from CashPayment");
	}

	@Override
	public List<Debt> get(Person person) {
		return find("from CashPayment where person = ?", person);
	}

	@Override
	public List<Debt> get(DateTime start, DateTime end) {
		return find("from CashPayment where date > ? and date < ?", start, end);
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
