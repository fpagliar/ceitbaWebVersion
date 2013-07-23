package ar.edu.itba.paw.domain.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import ar.edu.itba.paw.domain.PersistentEntity;

@Entity
@Table(name="services")
public class Service extends PersistentEntity {

	@Column(name="name", unique=true, nullable=false)
	private String name;
	@Column(name="value", nullable=false)
	private double value;
	public static enum Type {SPORT, COURSE, LOCKER, OTHER};
	@Enumerated(EnumType.STRING)
	@Column(name="type", nullable=false)
	private Type type;
	public static enum Status {ACTIVE, INACTIVE};
	@Enumerated(EnumType.STRING)
	@Column(name="status", nullable=false)
	private Status status;
	private int monthsDuration;

	Service(){
	}
	
	public Service(String name, Double value, Type type, int monthsDuration){
		this.name = name;
		this.value = value;
		this.type = type;
		this.status = Status.ACTIVE;
		this.monthsDuration = monthsDuration;
	}
	
	public String getName(){
		return name;
	}
	
	public double getValue(){
		return value;
	}
	
	public Type getType(){
		return type;
	}
	
	public boolean isActive(){
		return status == Status.ACTIVE;
	}
	
	public Status getStatus(){
		return status;
	}
	
	public int getMonthsDuration(){
		return monthsDuration;
	}
}
