package ar.edu.itba.paw.domain.enrollment;

import java.util.List;

import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.PaginatedResult;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.user.Person;

public interface EnrollmentRepo {

	boolean add(final Enrollment enrollment);
	Enrollment get(int id);
	List<Enrollment> get(final Person p);
	List<Enrollment> get(final Service s);

	List<Enrollment> getAll();
	PaginatedResult<Enrollment> getExpired(final int page);
	List<Enrollment> getExpired(final Person p);
	List<Enrollment> getExpired(final Service s);

	PaginatedResult<Enrollment> getActive(final int page);
	List<Enrollment> getActive(final Person p);
	PaginatedResult<Enrollment> getActive(final Service s, final int page);
	List<Enrollment> getActive(final Person p, final Service s);

	List<Enrollment> getBilledActive(final Service s);
	List<Enrollment> getBilledNewEnrollments(final Service s, final DateTime start, final DateTime end);
	List<Enrollment> getBilledCancelledEnrollments(final Service s, final DateTime start, final DateTime end);
}
