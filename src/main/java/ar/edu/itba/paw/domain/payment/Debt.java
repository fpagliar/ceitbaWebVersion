package ar.edu.itba.paw.domain.payment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.PersistentEntity;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.lib.DateHelper;

@Entity
@Table(name = "debt")
public class Debt extends PersistentEntity {

	@ManyToOne(optional = false)
	private Person person;
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name = "billingdate", nullable = false)
	private DateTime billingDate;
	@Column(name = "amount", nullable = false)
	private int amount;

	protected Debt() {
	}

	public Debt(Person person, int amount, DateTime date) {
		this.person = person;
		this.amount = amount;
		this.billingDate = date;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public DateTime getBillingDate() {
		return billingDate;
	}

	public void setBillingDate(DateTime billingDate) {
		this.billingDate = billingDate;
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
	
	public String getFormatedDate() {
		try {
			return DateHelper.getDateString(billingDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
