package ar.edu.itba.paw.domain.payment;

import java.util.List;

import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.user.Person;

public interface DebtRepo {

	List<Debt> getAll();

	List<Debt> get(final Person person);

	List<Debt> get(final DateTime start, final DateTime end);
	
	List<Debt> get(final Person p, final DateTime start, final DateTime end);

	List<Debt> getPendingDebts(final DateTime start, final DateTime end);

//	List<Debt> removeBilledDebts();
	List<Debt> getCashedDebts(final DateTime start, final DateTime end);

	void delete(final Debt debt);

	void add(final Debt debt);

	void add(final List<Debt> debt);
	
//	public void pay(Debt debt, DateTime date);
//	
//	public void pay(Debt debt);

	Debt get(final int id);
}
