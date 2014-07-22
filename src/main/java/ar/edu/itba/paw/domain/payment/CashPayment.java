package ar.edu.itba.paw.domain.payment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.user.Person;

@Entity
@Table(name = "cash_payment")
public class CashPayment {

	@ManyToOne(optional = false)
	private Person person;
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name = "debt_date", nullable = false)
	private DateTime debtDate;
	@Column(name = "amount", nullable = false)
	private int amount;
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name = "payment_date", nullable = false)
	private DateTime paymentDate;

	protected CashPayment() {
	}

	public CashPayment(Person person, int amount, DateTime paymentDate,
			DateTime debtDate) {
		this.person = person;
		this.amount = amount;
		this.paymentDate = paymentDate;
		this.debtDate = debtDate;
	}

	public CashPayment(Debt debt, DateTime paymentDate) {
		this(debt.getPerson(), debt.getAmount(), paymentDate, debt.getDate());
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public DateTime getDebtDate() {
		return debtDate;
	}

	public void setDebtDate(DateTime debtDate) {
		this.debtDate = debtDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public DateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(DateTime paymentDate) {
		this.paymentDate = paymentDate;
	}
}
