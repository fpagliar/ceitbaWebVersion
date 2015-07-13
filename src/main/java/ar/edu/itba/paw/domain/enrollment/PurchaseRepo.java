package ar.edu.itba.paw.domain.enrollment;

import java.util.List;

import ar.edu.itba.paw.domain.user.Person;

public interface PurchaseRepo {

	List<Purchase> getPending();

	List<Purchase> getPending(final Person p);

	List<Purchase> getBilled(final Person p);
	
	void save(final Purchase purchase);

	void delete(final Purchase purchase);
	
	Purchase get(final int id);
}
