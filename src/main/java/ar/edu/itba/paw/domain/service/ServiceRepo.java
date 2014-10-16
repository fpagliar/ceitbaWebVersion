package ar.edu.itba.paw.domain.service;

import java.util.List;

import ar.edu.itba.paw.domain.PaginatedResult;

public interface ServiceRepo {

	public void add(final Service service);

	public Service get(final int id);
	
	public Service get(final String name);

	public PaginatedResult<Service> getAll(final int page);

	public PaginatedResult<Service> getSports(final int page);
	public List<Service> getActiveSports();
	public PaginatedResult<Service> getActiveSports(final int page);

	public PaginatedResult<Service> getCourses(final int page);
	public List<Service> getActiveCourses();
	public PaginatedResult<Service> getActiveCourses(final int page);
	
	public PaginatedResult<Service> getOthers(final int page);
	public List<Service> getActiveOthers();
	public PaginatedResult<Service> getActiveOthers(final int page);

	public PaginatedResult<Service> getLockers(final int page);
	public List<Service> getActiveLockers();
	public PaginatedResult<Service> getActiveLockers(final int page);
	
	public PaginatedResult<Service> getCommons(final int page);
	public List<Service> getActiveCommons();
	public PaginatedResult<Service> getActiveCommons(final int page);

	public List<Service> getActive();
	public PaginatedResult<Service> getActive(final int page);
	public PaginatedResult<Service> getInactive(final int page);
	public List<Service> search(final String s);
	/**
	 * Search method that uses pagination for the results.
	 */
	public PaginatedResult<Service> search(final String s, final int page);
	

}
