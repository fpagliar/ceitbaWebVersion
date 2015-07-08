package ar.edu.itba.paw.presentation.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.domain.service.Product;
import ar.edu.itba.paw.domain.service.ProductRepo;

@Component
public class ProductConverter implements Converter<String, Product> {

	private final ProductRepo productRepo;

	@Autowired
	public ProductConverter(final ProductRepo productRepo) {
		this.productRepo = productRepo;
	}

	@Override
	public Product convert(final String source) {
		try {
			return productRepo.getById(Integer.valueOf(source));
		} catch (final NumberFormatException e) {
			return null;
		}
	}
}
