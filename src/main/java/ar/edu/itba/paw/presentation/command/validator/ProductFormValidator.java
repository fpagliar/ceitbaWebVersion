package ar.edu.itba.paw.presentation.command.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.paw.domain.service.Product;
import ar.edu.itba.paw.domain.service.ProductRepo;
import ar.edu.itba.paw.presentation.command.ProductForm;

@Component
public class ProductFormValidator implements Validator {

	private final ProductRepo productRepo;

	@Autowired
	public ProductFormValidator(final ProductRepo productRepo) {
		super();
		this.productRepo = productRepo;
	}
	
	@Override
	public boolean supports(final Class<?> clazz) {
		return ProductForm.class.equals(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors) {
		final ProductForm target = (ProductForm) obj;
		if (target.getName() == null || target.getName() == "") {
			errors.rejectValue("name", "invalid");
		}
		final Product product = productRepo.get(target.getName());
		if (product != null) {
			errors.rejectValue("name", "existent");			
		}
		try {
			final Double value = Double.parseDouble(target.getValue());
			if (value < 0) {
				errors.rejectValue("value", "negative");
			}
		} catch (final NumberFormatException e) {
			errors.rejectValue("value", "invalid");
		}
		return;		
	}

}
