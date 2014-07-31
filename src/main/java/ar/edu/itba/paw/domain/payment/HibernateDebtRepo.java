package ar.edu.itba.paw.domain.payment;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.payment.Debt.DebtStatus;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.Person.PaymentMethod;

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
		c.add(Restrictions.eq("status", DebtStatus.PENDING));
		return (List<Debt>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Debt> get(Person person) {
		Criteria c = createCriteria(Debt.class).add(
				Restrictions.eq("person", person));
		c.add(Restrictions.eq("status", DebtStatus.PENDING));
		return (List<Debt>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Debt> get(DateTime start, DateTime end) {
		Criteria c = createCriteria(Debt.class);
		c.add(Restrictions.eq("status", DebtStatus.PENDING));
		if (start != null)
			c.add(Restrictions.gt("billingDate", start));
		if (end != null)
			c.add(Restrictions.lt("billingDate", end));
		return (List<Debt>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Debt> get(Person p, DateTime start, DateTime end) {
		Criteria c = createCriteria(Debt.class);
		c.add(Restrictions.eq("status", DebtStatus.PENDING));
		if (p != null)
			c.add(Restrictions.eq("person", p));
		if (start != null)
			c.add(Restrictions.gt("billingDate", start));
		if (end != null)
			c.add(Restrictions.lt("billingDate", end));
		return (List<Debt>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Debt> getBilledDebts(Boolean personnel, DateTime start,
			DateTime end) {
		Criteria c = createCriteria(Debt.class);
		c.add(Restrictions.eq("status", DebtStatus.PENDING));
		if (start != null)
			c.add(Restrictions.gt("billingDate", start));
		if (end != null)
			c.add(Restrictions.lt("billingDate", end));
		if (personnel) {
			c.createCriteria("person").add(Restrictions.le("legacy", 1000))
					.add(Restrictions.eq("paymentMethod", PaymentMethod.BILL));
		} else {
			c.createCriteria("person").add(Restrictions.ge("legacy", 1000))
					.add(Restrictions.eq("paymentMethod", PaymentMethod.BILL));
		}
		return (List<Debt>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Debt> removeBilledDebts() {
		Criteria c = createCriteria(Debt.class);
		c.createCriteria("person").add(
				Restrictions.eq("paymentMethod", PaymentMethod.BILL));
		List<Debt> debts = (List<Debt>) c.list();
		for (Debt debt : debts) {
			debt.billed();
			update(debt);
		}
		return debts;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Debt> getCashedDebts(DateTime start, DateTime end) {
		Criteria c = createCriteria(Debt.class);
		c.add(Restrictions.eq("status", DebtStatus.PENDING));
		if (start != null)
			c.add(Restrictions.gt("billingDate", start));
		if (end != null)
			c.add(Restrictions.lt("billingDate", end));
		c.createCriteria("person").add(
				Restrictions.eq("paymentMethod", PaymentMethod.CASH));
		return (List<Debt>) c.list();
	}

	@Override
	public void add(Debt debt) {
		save(debt);
	}

	@Override
	public void add(List<Debt> debts) {
		for (Debt debt : debts) {
			add(debt);
		}
	}

//	@Override
//	public void pay(Debt debt, DateTime date) {
//		debt.pay(date);
//		update(debt);
//		return;
//	}
//
//	@Override
//	public void pay(Debt debt) {
//		pay(debt, DateTime.now());
//		update(debt);
//	}

	@Override
	public Debt get(int id) {
		return get(Debt.class, id);
	}

}
