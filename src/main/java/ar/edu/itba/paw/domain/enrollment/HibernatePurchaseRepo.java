package ar.edu.itba.paw.domain.enrollment;

import java.util.List;

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
	public List<Purchase> getAll(final Person p) {
		return (List<Purchase>) createCriteria(Purchase.class).add(Restrictions.eq("person", p)).list();
	}

}
