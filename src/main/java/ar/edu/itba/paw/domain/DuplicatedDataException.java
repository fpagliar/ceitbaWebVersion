package ar.edu.itba.paw.domain;

public class DuplicatedDataException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object object;
	
	public DuplicatedDataException(Object o) {
		this.object = o;
	}
	
	public Object getObject(){
		return object;
	}
}
