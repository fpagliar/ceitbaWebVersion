package ar.edu.itba.paw.domain;

public class DuplicatedDataException extends RuntimeException{

	private Object object;
	
	public DuplicatedDataException(Object o) {
		this.object = o;
	}
	
	public Object getObject(){
		return object;
	}
}
