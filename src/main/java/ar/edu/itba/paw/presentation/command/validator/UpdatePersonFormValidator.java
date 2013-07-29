package ar.edu.itba.paw.presentation.command.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.PersonRepo;
import ar.edu.itba.paw.presentation.command.UpdatePersonForm;
import ar.edu.itba.paw.presentation.command.UpdateUserForm;
import ar.edu.itba.paw.validators.CeitbaValidator;

@Component
public class UpdatePersonFormValidator implements Validator {

	private PersonRepo personRepo;

	@Autowired
	public UpdatePersonFormValidator(PersonRepo personRepo) {
		super();
		this.personRepo = personRepo;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UpdateUserForm.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		UpdatePersonForm target = (UpdatePersonForm) obj;
		Person p = personRepo.getByDni(target.getDni());
		if (p != null && p.getId() != target.getId()) {
			errors.rejectValue("dni", "exists");
		}
		try {
			int legacy = Integer.parseInt(target.getLegacy());
			if (legacy < 0) {
				errors.rejectValue("legacy", "negative");
			}
			p = personRepo.getByLegacy(legacy);
			if (p != null && p.getId() != target.getId()) {
				errors.rejectValue("legacy", "exists");
			}
		} catch (NumberFormatException e) {
			errors.rejectValue("legacy", "invalid");
		}

		if (!CeitbaValidator.validatePhone(target.getPhone())) {
			errors.rejectValue("phone", "invalid");
		}
		if (!CeitbaValidator.validatePhone(target.getCellphone())) {
			errors.rejectValue("cellphone", "invalid");
		}
		if (!CeitbaValidator.validateMail(target.getEmail())) {
			errors.rejectValue("email", "invalid");
		}
		if (!CeitbaValidator.validateMail(target.getEmail2())) {
			errors.rejectValue("email2", "invalid");
		}
		return;
	}
}