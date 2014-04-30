package ar.edu.itba.paw.domain.assistance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.PersistentEntity;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.lib.DateHelper;

@Entity
@Table(name="assistances")
public class Assistance extends PersistentEntity {

	@ManyToOne(optional=false)
	private Person person;
	@ManyToOne(optional=false)
	private Service service;
	
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name="date", nullable=false)
	private DateTime date;
	
	Assistance(){
	}
	
	public Assistance(Person person, Service service){
		this(person, service, DateTime.now());
	}

	public Assistance(Person person, Service service, DateTime date){
		this.person = person;
		this.service = service;
		setDate(date);
	}
	
	private void setDate(DateTime date){
		this.date = DateHelper.getNormalizedDate(date);
	}
	
	public Person getPerson() {
		return person;
	}

	public Service getService() {
		return service;
	}

	public DateTime getDate() {
		return date;
	}
	
	public String getFormatedDate(){
		try{
		return DateHelper.getDateString(date);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "person:" + person.getLegacy() + " - service:" + service.getName() + " - date:" + date.toString();
	}
}