package ar.edu.itba.paw.presentation.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.service.ServiceRepo;

@Component
public class ServiceConverter implements Converter<String, Service>{
	
	private ServiceRepo services;
	
	@Autowired
	public ServiceConverter(ServiceRepo services) {
		this.services = services;
	}

	@Override
	public Service convert(String source) {
		try{
			return services.get(Integer.valueOf(source));
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
