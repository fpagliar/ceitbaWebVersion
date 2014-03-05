package ar.edu.itba.paw.validators;

public class ValidateEmptyString {

	public static void validate(String str) {
		if (str == null || str.length() == 0) {
			throw new NullPointerException();
		}
	}
}
