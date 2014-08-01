package ar.edu.itba.paw.presentation.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.domain.user.User;
import ar.edu.itba.paw.domain.user.UserRepo;

@Component
public class UserConverter implements Converter<String, User>{
	
	private UserRepo repo;
	
	@Autowired
	public UserConverter(UserRepo repo) {
		this.repo = repo;
	}

	@Override
	public User convert(String source) {
		try {
			return repo.get(Integer.valueOf(source));
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
