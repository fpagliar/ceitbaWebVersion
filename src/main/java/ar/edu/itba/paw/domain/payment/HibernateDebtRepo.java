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
public class HibernateDebtRepo extends AbstractHibernateRepo implements DebtRepo {

	@Autowired
	public HibernateDebtRepo(final SessionFactory sessionFactory) {
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
	public List<Debt> get(final Person person) {
		final Criteria c = createCriteria(Debt.class).add(Restrictions.eq("person", person));
		c.add(Restrictions.eq("status", DebtStatus.PENDING));
		return (List<Debt>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Debt> get(final DateTime start, final DateTime end) {
		final Criteria c = createCriteria(Debt.class);
		c.add(Restrictions.eq("status", DebtStatus.PENDING));
		if (start != null)
			c.add(Restrictions.gt("billingDate", start));
		if (end != null)
			c.add(Restrictions.lt("billingDate", end));
		return (List<Debt>) c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Debt> get(final Person p, final DateTime start, final DateTime end) {
		final Criteria c = createCriteria(Debt.class);
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
	public List<Debt> getPendingDebts(final DateTime start, final DateTime end) {
		final Criteria c = createCriteria(Debt.class);
		c.add(Restrictions.eq("status", DebtStatus.PENDING));
		if (start != null)
			c.add(Restrictions.gt("billingDate", start));
		if (end != null)
			c.add(Restrictions.lt("billingDate", end));
		return (List<Debt>) c.list();
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Debt> removeBilledDebts() {
//		final Criteria c = createCriteria(Debt.class);
//		c.createAlias("person", "p").add(Restrictions.eq("p.paymentMethod", PaymentMethod.BILL));
//		final List<Debt> debts = (List<Debt>) c.list();
//		for (final Debt debt : debts) {
//			debt.billed();
//			update(debt);
//		}
//		return debts;
//	}
	@Override
	public void delete(final Debt debt) {
		super.delete(debt);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Debt> getCashedDebts(final DateTime start, final DateTime end) {
		final Criteria c = createCriteria(Debt.class);
		c.add(Restrictions.eq("status", DebtStatus.PENDING));
		if (start != null)
			c.add(Restrictions.gt("billingDate", start));
		if (end != null)
			c.add(Restrictions.lt("billingDate", end));
		c.createAlias("person", "p").add(Restrictions.eq("p.paymentMethod", PaymentMethod.CASH));
		return (List<Debt>) c.list();
	}

	@Override
	public void add(final Debt debt) {
		save(debt);
	}

	@Override
	public void add(final List<Debt> debts) {
		for (final Debt debt: debts) {
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
	public Debt get(final int id) {
		return get(Debt.class, id);
	}

}
