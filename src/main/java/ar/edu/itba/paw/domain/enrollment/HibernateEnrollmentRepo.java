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

	@Override
	public List<Enrollment> getExpired() {
		updateActive();
		return find("from Enrollment where end is not null");		
	}

	@Override
	public List<Enrollment> getExpired(Person p) {
		updateActive();
		return find("from Enrollment where person = ? and end is not null", p.getId());
	}

	@Override
	public List<Enrollment> getExpired(Service s) {
		updateActive();
		return find("from Enrollment where service = ? and end is not null", s.getId());
	}

	@Override
	public List<Enrollment> getActive() {
		updateActive();
		return find("from Enrollment where end is null");		
	}

	@Override
	public List<Enrollment> getActive(Person p) {
		updateActive();
		return find("from Enrollment where person = ? and end is null", p.getId());
	}

	@Override
	public List<Enrollment> getActive(Service s) {
		updateActive();
		return find("from Enrollment where service = ? and end is null", s.getId());
	}

	private boolean duplicatedData(String field, String value) {
		return !find("from Enrollment where ? = ?", field, value).isEmpty();
	}
	
	private void updateActive(){
		for(Enrollment e: getAll())
			e.checkActive();
	}
}