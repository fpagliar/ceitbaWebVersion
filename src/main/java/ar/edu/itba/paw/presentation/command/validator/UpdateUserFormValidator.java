package ar.edu.itba.paw.presentation.command.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.paw.domain.user.User;
import ar.edu.itba.paw.domain.user.UserRepo;
import ar.edu.itba.paw.presentation.command.UpdateUserForm;

@Component
public class UpdateUserFormValidator implements Validator {
	private UserRepo userRepo;

	@Autowired
	public UpdateUserFormValidator(UserRepo userRepo) {
		super();
		this.userRepo = userRepo;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UpdateUserForm.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		UpdateUserForm target = (UpdateUserForm) obj;
		String previous = target.getCurrentUsername();
		String next = target.getUsername();

		User currentUser = userRepo.get(previous);

		// Check old password
		if (!(target.isAdmin() && (!currentUser.isAdmin()))) {
			if (target.getOldPassword().equals(""))
				errors.rejectValue("oldPassword", "missing");
			else if (!target.getOldPassword().equals(currentUser.getPassword()))
				errors.rejectValue("oldPassword", "invalid");
		}

		// Password changed
		if (!target.getNewPassword().equals("")) {
			if (target.getReNewPassword().equals("")) {
				errors.rejectValue("reNewPassword", "missing");
			}
			if (!target.getNewPassword().equals(target.getReNewPassword())) {
				errors.rejectValue("newPassword", "match");
				errors.rejectValue("reNewPassword", "match");
			}
			if (target.getNewPassword().length() < 6) {
				errors.rejectValue("newPassword", "short");
			}
		}

		// Username changed
		if (!previous.equals(next)) {
			if (next.equals("")) {
				errors.rejectValue("username", "missing");
			}
			List<User> userList = userRepo.getAll();
			for (User user : userList) {
				if ((!user.getUsername().equals(previous))
						&& user.getUsername().equals(next)) {
					errors.rejectValue("username", "exists");
				}
			}
		}
	}
}