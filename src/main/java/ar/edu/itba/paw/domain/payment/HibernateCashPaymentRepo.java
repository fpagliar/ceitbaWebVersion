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
public class HibernateCashPaymentRepo extends AbstractHibernateRepo implements
		CashPaymentRepo {
	
	@Autowired
	public HibernateCashPaymentRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public CashPayment get(int id) {
		return get(CashPayment.class, id);
	}

	@Override
	public void add(CashPayment payment) {
		save(payment);		
	}

	@Override
	public void remove(CashPayment payment) {
		delete(payment);		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CashPayment> getAll() {
		Criteria c = createCriteria(CashPayment.class);
		return (List<CashPayment>) c.list();
		//		return find("from CashPayment");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CashPayment> getAll(DateTime start, DateTime end) {
		Criteria c = createCriteria(CashPayment.class);
		if(start != null)
			c.add(Restrictions.gt("paymentDate", start));
		if(end != null)
			c.add(Restrictions.lt("paymentDate", end));
		return (List<CashPayment>) c.list();
//		if(start == null)
//			start = DateTime.parse("0");
//		if(end == null)
//			end = DateTime.now();
//		return find("from CashPayment where paymentDate > ? and paymentDate < ?", start, end);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CashPayment> getAll(Person p) {
		Criteria c = createCriteria(CashPayment.class);
		if(p != null)
			c.add(Restrictions.eq("person", p));
		return (List<CashPayment>) c.list();
//		if(p == null)
//			return getAll();
//		return find("from CashPayment where person = ?", p);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CashPayment> getAll(Person p, DateTime start, DateTime end) {
		Criteria c = createCriteria(CashPayment.class);
		if(p != null)
			c.add(Restrictions.eq("person", p));
		if(start != null)
			c.add(Restrictions.gt("paymentDate", start));
		if(end != null)
			c.add(Restrictions.lt("paymentDate", end));
		return (List<CashPayment>) c.list();
//		if(p == null)
//			return getAll(start, end);
//		if(start == null)
//			start = DateTime.parse("0");
//		if(end == null)
//			end = DateTime.now();
//		return find("from CashPayment where paymentDate > ? and paymentDate < ? and person = ?", start, end, p);
	}

}