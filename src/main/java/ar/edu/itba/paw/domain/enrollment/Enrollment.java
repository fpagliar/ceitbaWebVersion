package ar.edu.itba.paw.domain.enrollment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.PersistentEntity;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.user.Person;

@Entity
@Table(name="enrollments")
public class Enrollment extends PersistentEntity {

	@ManyToOne(optional=false)
	private Person person;
	@ManyToOne(optional=false)
	private Service service;
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name="data", nullable=false)
	private DateTime date;
	
	Enrollment(){
	}
	
	public Enrollment(Person person, Service service){
		this.person = person;
		this.service = service;
		this.date = DateTime.now();
	}
	
	public Person getPerson(){
		return person;
	}
	
	public Service getService(){
		return service;
	}
	
	public DateTime getDate(){
		return date;
	}
	
	public boolean isActive(){
		DateTime expirationDate = date.plusMonths(service.getMonthsDuration());
		return DateTime.now().isBefore(expirationDate);
	}
	
	public boolean hasExpired(){
		return !isActive();
	}
}