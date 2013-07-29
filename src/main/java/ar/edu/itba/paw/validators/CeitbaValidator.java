package ar.edu.itba.paw.validators;

import java.util.regex.Pattern;

public class CeitbaValidator {
	public static boolean validateMail(String mail) {
		if(mail == null || mail == "")
			return true;
		return Pattern.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*" +
				"@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", mail);
	}
	
	public static boolean validatePhone(String phone){
		if(phone == null || phone == "")
			return true;
		return Pattern.matches("[0-9 -]+", phone);		
	}
}