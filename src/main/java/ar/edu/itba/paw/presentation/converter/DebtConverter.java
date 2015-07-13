package ar.edu.itba.paw.presentation.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.domain.payment.Debt;
import ar.edu.itba.paw.domain.payment.DebtRepo;

@Component
public class DebtConverter implements Converter<String, Debt> {

	private final DebtRepo debtRepo;

	@Autowired
	public DebtConverter(DebtRepo persons) {
		this.debtRepo = persons;
	}

	@Override
	public Debt convert(final String source) {
		try {
			return debtRepo.get(Integer.valueOf(source));
		} catch (final NumberFormatException e) {
			return null;
		}
	}
}
