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
	public PaginatedResult<Person> getAll(final int page);
	public PaginatedResult<Person> search(final String s, final int page);

	/**
	 * Returns the unique {@link Person} associated with a specific id.
	 * @return the {@link Person} or null if it doesn't exist
	 */
	public Person getById(int id);
	public Person getByLegacy(int legacy);
	public Person getByDni(String dni);
	/**
	 * Returns the unique {@link Person} associated with the username provided.
	 * @return the {@link Person} or null if it doesn't exist
	 */
//	public Person get(String username);

	/**
	 * Adds the received {@link Person} to the database.
	 */
	public void add(Person person);
	
	public List<Debt> billCashPayments();
	
}
