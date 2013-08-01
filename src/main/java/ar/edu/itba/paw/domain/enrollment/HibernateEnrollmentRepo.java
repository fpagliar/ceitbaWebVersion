package ar.edu.itba.paw.domain.enrollment;
import java.util.ArrayList;
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
		if (duplicatedData("person", "" + enrollment.getPerson()) || 
				duplicatedData("service", "" + enrollment.getService()) ) {
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
		return find("from Enrollment where person = ?", p);
	}

	@Override
	public List<Enrollment> get(Service s){
		return find("from Enrollment where service = ?", s);
	}
	

	@Override
	public List<Enrollment> get(Person p, Service s) {
		return find("from Enrollment where person = ? and service = ?", p, s);
	}


	@Override
	public List<Enrollment> getAll() {
		return find("from Enrollment");
	}

	@Override
	public List<Enrollment> getExpired() {
		updateActive();
		return find("from Enrollment where endDate is not null");		
	}

	@Override
	public List<Enrollment> getExpired(Person p) {
		updateActive();
		return find("from Enrollment where person = ? and endDate is not null", p);
	}

	@Override
	public List<Enrollment> getExpired(Service s) {
		updateActive();
		return find("from Enrollment where service = ? and endDate is not null", s);
	}

	@Override
	public List<Enrollment> getActive() {
		updateActive();
		return find("from Enrollment where endDate is null");		
	}

	@Override
	public List<Enrollment> getActive(Person p) {
		updateActive();
		return find("from Enrollment where person = ? and endDate is null", p);
	}

	@Override
	public List<Enrollment> getActive(List<Person> persons) {
		updateActive();
		List<Enrollment> all = new ArrayList<Enrollment>();
		for(Person p : persons)
			all.addAll(getActive(p));
		return all;
	}

	@Override
	public List<Enrollment> getActive(Service s) {
		updateActive();
		return find("from Enrollment where service = ? and endDate is null", s);
	}

	private boolean duplicatedData(String field, String value) {
		return !find("from Enrollment where ? = ?", field, value).isEmpty();
	}
	
	private void updateActive(){
		for(Enrollment e: getAll())
			e.checkActive();
	}
}