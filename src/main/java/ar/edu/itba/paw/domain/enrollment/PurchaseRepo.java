package ar.edu.itba.paw.domain.enrollment;

import java.util.List;

import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.Person.PaymentMethod;

public interface PurchaseRepo {

	List<Purchase> getPending();

	List<Purchase> getPending(final PaymentMethod method);

	List<Purchase> getPending(final Person p);

	List<Purchase> getBilled(final Person p);
	
	void save(final Purchase purchase);

	void update(final Purchase purchase);

	void delete(final Purchase purchase);
	
	Purchase get(final int id);
}
