package ar.edu.itba.paw.domain.payment;

import java.util.List;

import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.user.Person;

public interface DebtRepo {

	public List<Debt> getAll();

	public List<Debt> get(Person person);

	public List<Debt> get(DateTime start, DateTime end);
	
	public List<Debt> get(Person p, DateTime start, DateTime end);

	public List<Debt> getBilledDebts(Boolean personnel, DateTime start, DateTime end);

	public List<Debt> removeBilledDebts();

	public List<Debt> getCashedDebts(DateTime start, DateTime end);

	public void add(Debt debt);

	public void add(List<Debt> debt);
	
//	public void pay(Debt debt, DateTime date);
//	
//	public void pay(Debt debt);

	public Debt get(int id);
}
