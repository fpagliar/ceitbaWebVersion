package ar.edu.itba.paw.domain.assistance;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.user.Person;

@Repository
public class HibernateAssistanceRepo extends AbstractHibernateRepo implements
		AssistanceRepo {

	@Autowired
	public HibernateAssistanceRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public void add(Assistance assistance) {
		save(assistance);
	}

	@Override
	public Assistance get(int id) {
		return get(Assistance.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Assistance> getAll(Person person) {
		Criteria c = createCriteria(Assistance.class).add(Restrictions.eq("person", person));
		return (List<Assistance>) c.list();
//		return find("from Assistance where person = ?", person);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Assistance> getAll(DateTime date) {
		Criteria c = createCriteria(Assistance.class).add(Restrictions.eq("date", date));
		return (List<Assistance>) c.list();
//		return find("from Assistance where date = ?",
//				DateHelper.getNormalizedDate(date));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Assistance> getAll(DateTime startDate, DateTime endDate) {
		Criteria c = createCriteria(Assistance.class).add(Restrictions.between("date", startDate, endDate));
		return (List<Assistance>) c.list();
//		return find("from Assistance where date <= ? and date >= ?",
//				DateHelper.getNormalizedDate(startDate),
//				DateHelper.getNormalizedDate(endDate));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Assistance> getAll(Person person, DateTime startDate,
			DateTime endDate) {
		Criteria c = createCriteria(Assistance.class).add(Restrictions.eq("person", person));
		c.add(Restrictions.between("date", startDate, endDate));
		return (List<Assistance>) c.list();
//		return find(
//				"from Assistance where date <= ? and date >= ? and person = ?",
//				DateHelper.getNormalizedDate(startDate),
//				DateHelper.getNormalizedDate(endDate), person);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Assistance> getAll() {
		Criteria c = createCriteria(Assistance.class);
		return (List<Assistance>) c.list();
//		return find("from Assistance");
	}
}