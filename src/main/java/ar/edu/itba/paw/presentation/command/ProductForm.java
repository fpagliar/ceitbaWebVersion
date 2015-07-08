package ar.edu.itba.paw.presentation.command;

public class ProductForm {

	private String name;
	private String value;
	private String personId;
	
	public ProductForm() {
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public void setValue(final String value) {
		this.value = value;
	}
	
	public void setPersonId(final String personId) {
		this.personId = personId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getPersonId() {
		return personId;
	}
}
