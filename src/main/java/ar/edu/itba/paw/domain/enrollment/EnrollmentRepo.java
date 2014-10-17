package ar.edu.itba.paw.domain.enrollment;

import java.util.List;

import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.PaginatedResult;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.user.Person;

public interface EnrollmentRepo {

	public void add(Enrollment enrollment);
	public Enrollment get(int id);
	public List<Enrollment> get(Person p);
	public List<Enrollment> get(Service s);
	public List<Enrollment> getAll();
	public PaginatedResult<Enrollment> getExpired(final int page);
	public List<Enrollment> getExpired(Person p);
	public List<Enrollment> getExpired(Service s);
	public PaginatedResult<Enrollment> getActive(final int page);
	public List<Enrollment> getActive(Person p);
//	public PaginatedResult<Enrollment> getActivePersonsList(List<Person> persons, final int page);
//	public PaginatedResult<Enrollment> getActiveServiceList(List<Service> services, final int page);
	public List<Enrollment> getActive(Service s);
	public PaginatedResult<Enrollment> getActive(Service s, final int page);
	public List<Enrollment> get(Person p, Service s);
	
	public List<Enrollment> getBilledActive(Service s);
	public List<Enrollment> getBilledNewEnrollments(Boolean personnel, Service s, DateTime start, DateTime end);
	public List<Enrollment> getBilledCancelledEnrollments(Boolean personnel, Service s, DateTime start, DateTime end);
}
