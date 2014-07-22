package ar.edu.itba.paw.domain.payment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.PersistentEntity;
import ar.edu.itba.paw.domain.user.Person;

@Entity
@Table(name = "debt")
public class Debt extends PersistentEntity {

	@ManyToOne(optional = false)
	private Person person;
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name = "date", nullable = false)
	private DateTime date;
	@Column(name = "amount", nullable = false)
	private int amount;

	protected Debt() {
	}

	public Debt(Person person, int amount, DateTime date) {
		this.person = person;
		this.amount = amount;
		this.date = date;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void pay() {
		pay(DateTime.now());
	}

	public void pay(DateTime date) {
		person.pay(this, date);
	}
}
