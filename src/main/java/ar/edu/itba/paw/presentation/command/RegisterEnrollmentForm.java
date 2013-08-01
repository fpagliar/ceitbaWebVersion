package ar.edu.itba.paw.presentation.command;

public class RegisterEnrollmentForm {

	String legacy;
	String serviceName;
	
	public RegisterEnrollmentForm(){
		
	}

	public String getLegacy() {
		return legacy;
	}

	public void setLegacy(String legacy) {
		this.legacy = legacy;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
}
