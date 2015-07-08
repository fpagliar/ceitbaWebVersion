package ar.edu.itba.paw.domain.service;

import java.util.List;

public interface ProductRepo {

	List<Product> getAll();
	
	void save(final Product product);
	
	Product getById(final int id);
	
	Product get(final String name);
}
