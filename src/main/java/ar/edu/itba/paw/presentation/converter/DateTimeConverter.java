package ar.edu.itba.paw.presentation.converter;

import org.joda.time.DateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.lib.DateHelper;

@Component
public class DateTimeConverter implements Converter<String, DateTime> {

	@Override
	public DateTime convert(String source) {
		try {
			return DateHelper.getDate(source);
		} catch (Exception e) {
			return null;
		}
	}
}