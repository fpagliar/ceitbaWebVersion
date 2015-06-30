package ar.edu.itba.paw.domain.service;

import java.util.List;

import ar.edu.itba.paw.domain.PaginatedResult;

public interface ServiceRepo {

	public void add(final Service service);

	public Service get(final int id);
	
	public Service get(final String name);

	public PaginatedResult<Service> getAll(final int page);

	public PaginatedResult<Service> getConsumables(final int page);
	public List<Service> getActiveConsumables();
	public PaginatedResult<Service> getActiveConsumables(final int page);

	public PaginatedResult<Service> getSubscribables(final int page);
	public List<Service> getActiveSubscribables();
	public PaginatedResult<Service> getActiveSubscribables(final int page);

	public List<Service> getActive();
	public PaginatedResult<Service> getActive(final int page);
	public PaginatedResult<Service> getInactive(final int page);
	public List<Service> search(final String s);
	/**
	 * Search method that uses pagination for the results.
	 */
	public PaginatedResult<Service> search(final String s, final int page);
	

}
