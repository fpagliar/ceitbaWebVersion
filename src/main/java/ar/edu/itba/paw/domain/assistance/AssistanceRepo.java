package ar.edu.itba.paw.domain.assistance;

import java.util.List;

import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.user.Person;

public interface AssistanceRepo {

	public void add(Assistance assistance);

	public Assistance get(int id);
	
	public List<Assistance> getAll(Person person);
	public List<Assistance> getAll(DateTime date);
	public List<Assistance> getAll(DateTime startDate, DateTime endDate);
	public List<Assistance> getAll(Person person, DateTime startDate, DateTime endDate);
	public List<Assistance> getAll();

}