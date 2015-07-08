package ar.edu.itba.paw.domain.enrollment;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.user.Person;

@Repository
public class HibernatePurchaseRepo extends AbstractHibernateRepo implements PurchaseRepo {

	@Autowired
	public HibernatePurchaseRepo(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Purchase> getPending(final Person p) {
		final Criteria criteria = createCriteria(Purchase.class);
		criteria.add(Restrictions.eq("person", p));
		criteria.add(Restrictions.eq("billed", false));
		return (List<Purchase>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Purchase> getBilled(final Person p) {
		final Criteria criteria = createCriteria(Purchase.class);
		criteria.add(Restrictions.eq("person", p));
		criteria.add(Restrictions.eq("billed", true));
		return (List<Purchase>) criteria.list();
	}

	@Override
	public void save(final Purchase purchase) {
		super.save(purchase);
	}

	@Override
	public void delete(final Purchase purchase) {
		super.delete(purchase);
	}

	@Override
	public Purchase get(final int id) {
		return super.get(Purchase.class, id);
	}
}
