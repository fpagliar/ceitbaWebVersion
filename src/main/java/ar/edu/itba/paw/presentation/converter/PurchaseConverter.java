package ar.edu.itba.paw.presentation.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.domain.enrollment.Purchase;
import ar.edu.itba.paw.domain.enrollment.PurchaseRepo;

@Component
public class PurchaseConverter implements Converter<String, Purchase> {

	private final PurchaseRepo purchaseRepo;

	@Autowired
	public PurchaseConverter(final PurchaseRepo productRepo) {
		this.purchaseRepo = productRepo;
	}

	@Override
	public Purchase convert(final String source) {
		try {
			return purchaseRepo.get(Integer.valueOf(source));
		} catch (final NumberFormatException e) {
			return null;
		}
	}
}
