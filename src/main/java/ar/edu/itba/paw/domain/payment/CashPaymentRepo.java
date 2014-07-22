package ar.edu.itba.paw.domain.payment;

import java.util.List;

import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.user.Person;

public interface CashPaymentRepo {
	
	public CashPayment get(int id);
	
	public void add(CashPayment payment);
	
	public void remove(CashPayment payment);

	public List<CashPayment> getAll();
	
	public List<CashPayment> getAll(DateTime start, DateTime end);

	public List<CashPayment> getAll(Person p);

	public List<CashPayment> getAll(Person p, DateTime start, DateTime end);
}
