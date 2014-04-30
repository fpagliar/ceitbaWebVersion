package ar.edu.itba.paw.domain.assistance;

import java.util.List;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.lib.DateHelper;

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

	@Override
	public List<Assistance> getAll(Person person) {
		return find("from Assistance where person = ?", person);
	}

	@Override
	public List<Assistance> getAll(DateTime date) {
		return find("from Assistance where date = ?",
				DateHelper.getNormalizedDate(date));
	}

	@Override
	public List<Assistance> getAll(DateTime startDate, DateTime endDate) {
		return find("from Assistance where date <= ? and date >= ?",
				DateHelper.getNormalizedDate(startDate),
				DateHelper.getNormalizedDate(endDate));
	}

	@Override
	public List<Assistance> getAll(Person person, DateTime startDate,
			DateTime endDate) {
		return find(
				"from Assistance where date <= ? and date >= ? and person = ?",
				DateHelper.getNormalizedDate(startDate),
				DateHelper.getNormalizedDate(endDate), person);
	}

	@Override
	public List<Assistance> getAll() {
		return find("from Assistance");
	}
}