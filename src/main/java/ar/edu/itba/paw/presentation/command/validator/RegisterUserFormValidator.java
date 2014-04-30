package ar.edu.itba.paw.presentation.command.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.paw.domain.user.User;
import ar.edu.itba.paw.domain.user.UserRepo;
import ar.edu.itba.paw.presentation.command.RegisterUserForm;
import ar.edu.itba.paw.presentation.command.UpdateUserForm;

@Component
public class RegisterUserFormValidator implements Validator{
	private UserRepo userRepo;
	
	@Autowired
	public RegisterUserFormValidator(UserRepo userRepo) {
		super();
		this.userRepo = userRepo;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UpdateUserForm.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		RegisterUserForm target = (RegisterUserForm) obj;
		if (target.getUsername().equals("")) {
			errors.rejectValue("username", "missing");
		}
		if (target.getLevel() == null) {
			errors.rejectValue("level", "missing");
		}
		if (target.getPassword().equals("")) {
			errors.rejectValue("password", "missing");
		}
		if(target.getRePassword().equals("")){
			errors.rejectValue("rePassword", "missing");
		}
		if (!target.getPassword().equals(target.getRePassword())) {
			errors.rejectValue("password", "match");
			errors.rejectValue("rePassword", "match");
		}

		List<User> userList = userRepo.getAll();
		for (User user : userList) {
			if (user.getUsername().equals(target.getUsername())) {
				errors.rejectValue("username", "exists");
			}
		}
	}
}