package ar.edu.itba.paw.presentation.command.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.paw.domain.payment.Debt;
import ar.edu.itba.paw.domain.payment.DebtRepo;
import ar.edu.itba.paw.presentation.command.CreatePaymentForm;

@Component
public class CreatePaymentFormValidator implements Validator {

	private DebtRepo debtRepo;

	@Autowired
	public CreatePaymentFormValidator(DebtRepo debtRepo) {
		super();
		this.debtRepo = debtRepo;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return CreatePaymentForm.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		CreatePaymentForm target = (CreatePaymentForm) obj;
		try {
			Integer id = Integer.parseInt(target.getDebtId());
			Debt debt = debtRepo.get(id);
			if (debt == null) {
				errors.rejectValue("id", "invalid");
			}
		} catch (NumberFormatException e) {
			errors.rejectValue("id", "invalid");
		}
	}
}