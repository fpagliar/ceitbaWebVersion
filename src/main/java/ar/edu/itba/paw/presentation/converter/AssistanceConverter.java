package ar.edu.itba.paw.presentation.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.domain.assistance.Assistance;
import ar.edu.itba.paw.domain.assistance.AssistanceRepo;

@Component
public class AssistanceConverter implements Converter<String, Assistance> {

	private AssistanceRepo assistances;

	@Autowired
	public AssistanceConverter(AssistanceRepo assistance) {
		this.assistances = assistance;
	}

	@Override
	public Assistance convert(String source) {
		try {
			return assistances.get(Integer.valueOf(source));
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
