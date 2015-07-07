package ar.edu.itba.paw.domain.user;

import java.util.List;

import ar.edu.itba.paw.domain.PaginatedResult;
import ar.edu.itba.paw.domain.payment.Debt;


/**
 * User repository.
 */
public interface PersonRepo {

	/**
	 * Lists all the registered {@link Person}.
	 */
	PaginatedResult<Person> getAll(final int page);
	PaginatedResult<Person> search(final String s, final int page);

	/**
	 * Returns the unique {@link Person} associated with a specific id.
	 * @return the {@link Person} or null if it doesn't exist
	 */
	Person getById(final int id);
	Person getByLegacy(final int legacy);
	Person getByDni(final String dni);

	/**
	 * Adds the received {@link Person} to the database.
	 */
	void add(final Person person);
	
	List<Debt> billCashPayments();
	
}
