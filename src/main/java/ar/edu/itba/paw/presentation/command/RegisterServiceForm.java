package ar.edu.itba.paw.presentation.command;


public class RegisterServiceForm {

	private String name;
	private String value;
	private String status;
	private String monthsDuration;

	public RegisterServiceForm() {
		this.monthsDuration = "0"; // Default value, implies a service that doesn't end 
								   // (Enrollments don't expire).
	}
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public String getMonthsDuration() {
		return monthsDuration;
	}
	public void setMonthsDuration(final String monthsDuration) {
		this.monthsDuration = monthsDuration;
	}
}