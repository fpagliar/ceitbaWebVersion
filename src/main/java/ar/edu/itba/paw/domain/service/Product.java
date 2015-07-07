package ar.edu.itba.paw.domain.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.edu.itba.paw.domain.PersistentEntity;

@Entity
@Table(name = "products")
public class Product extends PersistentEntity { 

	private static final long serialVersionUID = 4717499775668868849L;

	@Column(name = "name", unique = true, nullable = false)
	private String name;
	@Column(name = "value", nullable = false)
	private double value;
	
	// Default constructor for hibernate
	Product() {
	}
	
	public Product(final String name, final double value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(final double value) {
		this.value = value;
	}

}
