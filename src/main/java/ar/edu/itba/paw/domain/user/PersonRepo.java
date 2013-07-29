package ar.edu.itba.paw.domain.user;

import java.util.List;


/**
 * User repository.
 */
public interface PersonRepo {

	/**
	 * Lists all the registered {@link Person}.
	 */
	public List<Person> getAll();
	public List<Person> search(String s);

	/**
	 * Lists all the {@link Person} registered as administrators.
	 */
//	public List<Person> getAllAdministrators();
	
	/**
	 * Lists all the {@link Person} registered as regular users.
	 */
//	public List<Person> getAllRegularUsers();

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
}
