package ar.edu.itba.paw.presentation.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.PersonRepo;

@Component
public class PersonConverter implements Converter<String, Person> {

	private PersonRepo persons;

	@Autowired
	public PersonConverter(PersonRepo persons) {
		this.persons = persons;
	}

	@Override
	public Person convert(String source) {
		try {
			return persons.getById(Integer.valueOf(source));
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
