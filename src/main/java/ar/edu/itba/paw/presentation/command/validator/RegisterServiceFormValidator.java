package ar.edu.itba.paw.presentation.command.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.service.ServiceRepo;
import ar.edu.itba.paw.presentation.command.RegisterServiceForm;
import ar.edu.itba.paw.presentation.command.UpdateUserForm;

@Component
public class RegisterServiceFormValidator implements Validator {

	private ServiceRepo serviceRepo;

	@Autowired
	public RegisterServiceFormValidator(ServiceRepo serviceRepo) {
		super();
		this.serviceRepo = serviceRepo;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UpdateUserForm.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		RegisterServiceForm target = (RegisterServiceForm) obj;
		if(target.getName() == null || target.getName() == ""){
			errors.rejectValue("name", "invalid");
		}
		Service s = serviceRepo.get(target.getName());
		if(s!= null){
			errors.rejectValue("name", "existent");			
		}
		try {
			int duration = Integer.parseInt(target.getMonthsDuration());
			// The service duration set to 0 implies infinite service, enrollments don't expire
			if (duration < 0) {
				errors.rejectValue("monthsDuration", "negative");
			}
		} catch (NumberFormatException e) {
			errors.rejectValue("monthsDuration", "invalid");
		}
		try {
			Double value = Double.parseDouble(target.getValue());
			if (value < 0) {
				errors.rejectValue("value", "negative");
			}
		} catch (NumberFormatException e) {
			errors.rejectValue("value", "invalid");
		}
		return;
	}
}