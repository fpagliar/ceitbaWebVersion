package ar.edu.itba.paw.presentation.command.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.paw.domain.user.User;
import ar.edu.itba.paw.domain.user.UserRepo;
import ar.edu.itba.paw.presentation.command.LoginForm;

@Component
public class LoginFormValidator implements Validator {

	private UserRepo userRepo;

	@Autowired
	public LoginFormValidator(UserRepo userRepo) {
		super();
		this.userRepo = userRepo;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return LoginForm.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		LoginForm target = (LoginForm) obj;
		User user = userRepo.get(target.getUsername());

		if (user == null) {
			errors.rejectValue("username", "unexisting");
			return;
		}

		if (!user.getPassword().equals(target.getPassword())) {
			errors.rejectValue("password", "invalid");
			return;
		}
		return;
	}
}