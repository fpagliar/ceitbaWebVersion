package ar.edu.itba.paw.domain.payment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	private double amount;
	@Column(name = "reason", nullable = true)
	private String reason;

	public static enum DebtStatus {
		PENDING, PAYED
	};

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private DebtStatus status;

	protected Debt() {
	}

	public Debt(Person person, double amount, DateTime date, String reason) {
		this.person = person;
		this.amount = amount;
		this.billingDate = date;
		this.reason = reason;
		this.status = DebtStatus.PENDING;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Person getPerson() {
		return person;
	}

	public DateTime getBillingDate() {
		return billingDate;
	}

	public void setBillingDate(DateTime billingDate) {
		this.billingDate = billingDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public CashPayment pay() {
		return pay(DateTime.now());
	}

	public CashPayment pay(DateTime date) {
		this.status = DebtStatus.PAYED;
		return person.pay(this, date);
	}

	public String getFormatedDate() {
		try {
			return DateHelper.getDateString(billingDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * To use instead of pay when using bill instead of cash payments.
	 * If using cash, call pay()
	 */
	public void billed() {
		this.status = DebtStatus.PAYED;
	}

	@Override
	public String toString() {
		return "id: " + getId() + " billing_date: "
				+ DateHelper.getDateString(billingDate) + " amount: " + amount
				+ " reason:" + reason + " status: " + status;
	}
}
