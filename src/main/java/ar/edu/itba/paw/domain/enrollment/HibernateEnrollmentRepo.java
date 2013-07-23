package ar.edu.itba.paw.domain.enrollment;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.DuplicatedDataException;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.user.Person;

@Repository
public class HibernateEnrollmentRepo extends AbstractHibernateRepo implements
		EnrollmentRepo {

	@Autowired
	public HibernateEnrollmentRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	/**
	 * @param person
	 *            - the user that has to be added.
	 * @throws DuplicatedUserException
	 *             if the username or mail already exists for another user
	 */
	@Override
	public void add(Enrollment enrollment) {
		if (duplicatedData("person", "" + enrollment.getPerson().getId()) || 
				duplicatedData("service", "" + enrollment.getService().getId()) ) {
			throw new DuplicatedDataException(enrollment);
		}
		save(enrollment);
		enrollment.getPerson().enroll(enrollment);
	}

	@Override
	public Enrollment get(int id) {
		return get(Enrollment.class, id);
	}
	
	@Override
	public List<Enrollment> get(Person p){
		return find("from Enrollment where person = ?", p.getId());
	}

	@Override
	public List<Enrollment> get(Service s){
		return find("from Enrollment where service = ?", s.getId());
	}

	@Override
	public List<Enrollment> getAll() {
		return find("from Enrollment");
	}

	private boolean duplicatedData(String field, String value) {
		return !find("from Enrollment where ? = ?", field, value).isEmpty();
	}
}