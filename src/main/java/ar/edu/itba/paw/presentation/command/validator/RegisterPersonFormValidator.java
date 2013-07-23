package ar.edu.itba.paw.presentation.command.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.paw.domain.user.PersonRepo;
import ar.edu.itba.paw.presentation.command.RegisterPersonForm;
import ar.edu.itba.paw.presentation.command.UpdateUserForm;
import ar.edu.itba.paw.validators.CeitbaValidator;

@Component
public class RegisterPersonFormValidator implements Validator{

	private PersonRepo personRepo;
	
	@Autowired
	public RegisterPersonFormValidator(PersonRepo personRepo) {
		super();
		this.personRepo = personRepo;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UpdateUserForm.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		RegisterPersonForm target = (RegisterPersonForm) obj;
		
		if(target.getLegacy() < 0){
			errors.rejectValue("negative", "legacy");			
		}
		if(personRepo.getByLegacy(target.getLegacy()) != null){
			errors.rejectValue("exists", "legacy");
		}
		if(target.getDni() < 0){
			errors.rejectValue("negative", "dni");
		}
		if(target.getEmail() != null && ! CeitbaValidator.validateMail(target.getEmail())){
			errors.rejectValue("invalid", "mail");			
		}
		if(target.getEmail2() != null && ! CeitbaValidator.validateMail(target.getEmail2())){
			errors.rejectValue("invalid", "mail");			
		}
		if(target.getPhone() != null && !CeitbaValidator.validatePhone(target.getPhone())){
			errors.rejectValue("invalid", "phone");						
		}
		if(target.getCellphone() != null && !CeitbaValidator.validatePhone(target.getCellphone())){
			errors.rejectValue("invalid", "cellphone");						
		}
	}
}