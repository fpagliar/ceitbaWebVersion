package ar.edu.itba.paw.domain.service;

import java.util.List;

public interface ServiceRepo {

	public void add(Service service);

	public Service get(int id);
	
	public Service get(String name);

	public List<Service> getAll();

	public List<Service> getSports();
	
	public List<Service> getCourses();
	
	public List<Service> getOthers();
}
