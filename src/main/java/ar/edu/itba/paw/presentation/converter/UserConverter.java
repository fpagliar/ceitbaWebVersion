package ar.edu.itba.paw.presentation.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.PersonRepo;

@Component
public class UserConverter implements Converter<String, Person>{
	
	private PersonRepo repo;
	
	@Autowired
	public UserConverter(PersonRepo repo) {
		this.repo = repo;
	}

	@Override
	public Person convert(String source) {
		try {
			return repo.getById(Integer.valueOf(source));
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
