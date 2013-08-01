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
	@Column(name="startDate", nullable=false)
	private DateTime startDate;
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name="endDate", nullable=true)
	private DateTime endDate;
	
	Enrollment(){
	}
	
	public Enrollment(Person person, Service service){
		this.person = person;
		this.service = service;
		this.startDate = DateTime.now();
		this.endDate = null;
	}
	
	public Person getPerson(){
		return person;
	}
	
	public Service getService(){
		return service;
	}
	
	public DateTime getStartDate(){
		return startDate;
	}
	
	public DateTime getEndDate(){
		return endDate;
	}

	public boolean isActive(){
		checkExpiration();
		return endDate == null;
	}
	
	public boolean hasExpired(){
		return !isActive();
	}
	
	private void checkExpiration(){
		DateTime expirationDate = startDate.plusMonths(service.getMonthsDuration());
		if(DateTime.now().isAfter(expirationDate)){
			endDate = expirationDate;
		}
	}
	
	public void checkActive(){
		checkExpiration();
	}
	
	public void cancel(){
		if(endDate == null)
			endDate = DateTime.now();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(! (obj instanceof Enrollment))
			return false;
		Enrollment other = (Enrollment) obj;
		return person.equals(other.getPerson()) && startDate.equals(other.getStartDate()) && service.equals(other.getService());
	}
}