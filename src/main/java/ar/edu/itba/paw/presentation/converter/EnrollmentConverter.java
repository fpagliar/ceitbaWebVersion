package ar.edu.itba.paw.presentation.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.domain.enrollment.Enrollment;
import ar.edu.itba.paw.domain.enrollment.EnrollmentRepo;

@Component
public class EnrollmentConverter implements Converter<String, Enrollment> {

	private EnrollmentRepo enrollments;

	@Autowired
	public EnrollmentConverter(EnrollmentRepo enrollments) {
		this.enrollments = enrollments;
	}

	@Override
	public Enrollment convert(String source) {
		try {
			return enrollments.get(Integer.valueOf(source));
		} catch (NumberFormatException e) {
			return null;
		}
	}
}