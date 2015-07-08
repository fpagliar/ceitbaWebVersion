package ar.edu.itba.paw.domain.enrollment;

import java.util.List;

import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.PaginatedResult;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.user.Person;

public interface EnrollmentRepo {

	public boolean add(final Enrollment enrollment);
	public Enrollment get(int id);
	public List<Enrollment> get(final Person p);
	public List<Enrollment> get(final Service s);
	public List<Enrollment> getAll();
	public PaginatedResult<Enrollment> getExpired(final int page);
	public List<Enrollment> getExpired(final Person p);
	public List<Enrollment> getExpired(final Service s);
	public PaginatedResult<Enrollment> getActive(final int page);
	public List<Enrollment> getActive(final Person p);
	public PaginatedResult<Enrollment> getActive(final Service s, final int page);

	public List<Enrollment> getActive(final Person p, final Service s);
	
	public List<Enrollment> getBilledActive(final Service s);
	public List<Enrollment> getBilledNewEnrollments(final Boolean personnel, final Service s, final DateTime start, 
			final DateTime end);
	public List<Enrollment> getBilledCancelledEnrollments(final Boolean personnel, final Service s, final DateTime start, 
			final DateTime end);
}
