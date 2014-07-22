package ar.edu.itba.paw.domain.payment;

import java.util.List;

import org.hibernate.SessionFactory;
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

	@Override
	public List<CashPayment> getAll() {
		return find("from CashPayment");
	}

	@Override
	public List<CashPayment> getAll(DateTime start, DateTime end) {
		return find("from CashPayment where paymentDate > ? and paymentDate < ?", start, end);
	}

	@Override
	public List<CashPayment> getAll(Person p) {
		return find("from CashPayment where person = ?", p);
	}

	@Override
	public List<CashPayment> getAll(Person p, DateTime start, DateTime end) {
		return find("from CashPayment where paymentDate > ? and paymentDate < ? and person = ?", start, end, p);
	}

}