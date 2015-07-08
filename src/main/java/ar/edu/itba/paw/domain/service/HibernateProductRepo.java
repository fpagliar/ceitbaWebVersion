package ar.edu.itba.paw.domain.service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.DuplicatedDataException;

@Repository
public class HibernateProductRepo extends AbstractHibernateRepo implements ProductRepo {

	@Autowired
	public HibernateProductRepo(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getAll() {
		final Criteria c = createCriteria(Product.class);
		return (List<Product>) c.list();
	}

	@Override
	public void save(final Product product) {
		if (duplicatedData("name", product.getName())) {
			throw new DuplicatedDataException(product);
		}
		super.save(product);
	}

	private boolean duplicatedData(final String field, final String value) {
		final Criteria c = createCriteria(Service.class).add(Restrictions.eq(field, value));
		return ! c.list().isEmpty();
	}

	@Override
	public Product getById(final int id) {
		return get(Product.class, id);
	}

	@Override
	public Product get(final String name) {
		final Criteria c = createCriteria(Product.class).add(Restrictions.eq("name", name));
		return (Product) c.uniqueResult();
	}
}
