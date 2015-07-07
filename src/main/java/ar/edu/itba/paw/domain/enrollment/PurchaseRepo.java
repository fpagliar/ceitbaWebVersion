package ar.edu.itba.paw.domain.enrollment;

import java.util.List;

import ar.edu.itba.paw.domain.user.Person;

public interface PurchaseRepo {

	List<Purchase> getAll(final Person p);

}
