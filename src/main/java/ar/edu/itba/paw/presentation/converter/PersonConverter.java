package ar.edu.itba.paw.presentation.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.PersonRepo;

@Component
public class PersonConverter implements Converter<String, Person> {

	private PersonRepo personRepo;

	@Autowired
	public PersonConverter(final PersonRepo persons) {
		this.personRepo = persons;
	}

	@Override
	public Person convert(final String source) {
		try {
			return personRepo.getById(Integer.valueOf(source));
		} catch (final NumberFormatException e) {
			return null;
		}
	}
}
