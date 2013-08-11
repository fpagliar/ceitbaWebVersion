package ar.edu.itba.paw.domain.enrollment;

import java.util.List;

import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.user.Person;

public interface EnrollmentRepo {

	public void add(Enrollment enrollment);
	public Enrollment get(int id);
	public List<Enrollment> get(Person p);
	public List<Enrollment> get(Service s);
	public List<Enrollment> getAll();
	public List<Enrollment> getExpired();
	public List<Enrollment> getExpired(Person p);
	public List<Enrollment> getExpired(Service s);
	public List<Enrollment> getActive();
	public List<Enrollment> getActive(Person p);
	public List<Enrollment> getActivePersonsList(List<Person> persons);
	public List<Enrollment> getActiveServiceList(List<Service> services);
	public List<Enrollment> getActive(Service s);
	public List<Enrollment> get(Person p, Service s);
}
