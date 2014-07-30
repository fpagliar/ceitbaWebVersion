package ar.edu.itba.paw.domain.service;

import java.util.List;

public interface ServiceRepo {

	public void add(Service service);

	public Service get(int id);
	
	public Service get(String name);

	public List<Service> getAll();

	public List<Service> getSports();
	public List<Service> getActiveSports();

	public List<Service> getCourses();
	public List<Service> getActiveCourses();
	
	public List<Service> getOthers();
	public List<Service> getActiveOthers();

	public List<Service> getLockers();
	public List<Service> getActiveLockers();
	
	public List<Service> getCommons();
	public List<Service> getActiveCommons();

	public List<Service> getActive();
	public List<Service> getInactive();
	public List<Service> search(String s);
	

}
