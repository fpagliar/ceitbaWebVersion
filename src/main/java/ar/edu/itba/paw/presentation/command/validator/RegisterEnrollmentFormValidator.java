package ar.edu.itba.paw.presentation.command.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.PersonRepo;
import ar.edu.itba.paw.presentation.command.RegisterEnrollmentForm;
import ar.edu.itba.paw.presentation.command.UpdateUserForm;

@Component
public class RegisterEnrollmentFormValidator implements Validator {

	private PersonRepo personRepo;

	@Autowired
	public RegisterEnrollmentFormValidator(PersonRepo personRepo) {
		super();
		this.personRepo = personRepo;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UpdateUserForm.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		RegisterEnrollmentForm target = (RegisterEnrollmentForm) obj;
		try {
			int legacy = Integer.parseInt(target.getLegacy());
			if (legacy < 0)
				errors.rejectValue("legacy", "negative");
			Person p = personRepo.getByLegacy(legacy);
			if (p == null)
				errors.rejectValue("legacy", "inexistent");
		} catch (NumberFormatException e) {
			errors.rejectValue("legacy", "invalid");
		}
		return;
	}
}