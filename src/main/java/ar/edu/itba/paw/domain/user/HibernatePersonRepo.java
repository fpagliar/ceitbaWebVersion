package ar.edu.itba.paw.domain.user;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.DuplicatedDataException;
import ar.edu.itba.paw.domain.service.Service;

@Repository
public class HibernatePersonRepo extends AbstractHibernateRepo implements
		PersonRepo {

	@Autowired
	public HibernatePersonRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	/**
	 * @param person
	 *            - the user that has to be added.
	 * @throws DuplicatedUserException
	 *             if the username or mail already exists for another user
	 */
	@Override
	public void add(Person person) {
		if (duplicatedData("legacy", "" + person.getLegacy())) {
			throw new DuplicatedDataException(person);
		}
		save(person);
	}

	@Override
	public Person getById(int id) {
		return get(Person.class, id);
	}
	
	public Person getByLegacy(int legacy){
		List<Person> persons = find("from Person where legacy = ?", legacy);
		if(persons.size() == 0)
			return null;
		if(persons.size() == 1)
			return persons.get(0);
		throw new RuntimeException("An internal error occurred");
	}

	public Person getByDni(String dni){
		if(dni == null || dni.equals(""))
			return null;
		List<Person> persons = find("from Person where dni = ?", dni);
		if(persons.size() == 0)
			return null;
		if(persons.size() == 1)
			return persons.get(0);
		throw new RuntimeException("An internal error occurred");
	}
//	@Override
//	public Person get(String username) {
//		List<Person> all = getAll();
//		for (Person user : all) {
//			if (user.getUsername().equals(username)) {
//				return user;
//			}
//		}
//		return null;
//	}

	@Override
	public List<Person> getAll() {
		return find("from Person");
	}

	@Override
	public List<Person> search(String s) {
		List<Person> ans = new ArrayList<Person>();
		try{
			Person p = getByLegacy(Integer.valueOf(s));
			ans.add(p);
			return ans;
		}catch (NumberFormatException e) {
		}
		List<Person> allByName = find("from Person where firstName like ? or lastName like ?", "%"+ s + "%", "%" + s + "%");
		ans.addAll(allByName);
		return ans;
	}

	private boolean duplicatedData(String field, String value) {
		return !find("from Person where ? = ?", field, value).isEmpty();
	}

//	@Override
//	public List<Person> getAllAdministrators() {
//		return find("from User where level = '" + Person.Level.ADMINISTRATOR + "'");
//	}
//
//	@Override
//	public List<Person> getAllRegularUsers() {
//		return find("from User where level = '" + Person.Level.REGULAR + "'");
//	}
}