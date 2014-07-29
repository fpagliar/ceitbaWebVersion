package ar.edu.itba.paw.domain.payment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.PersistentEntity;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.lib.DateHelper;

@Entity
@Table(name = "cash_payment")
public class CashPayment extends PersistentEntity {

	@ManyToOne(optional = false)
	private Person person;
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name = "payment_date", nullable = false)
	private DateTime paymentDate;
	@OneToOne(optional = false)
	private Debt debt;

	protected CashPayment() {
	}

	public CashPayment(Person person, DateTime paymentDate, Debt debt) {
		this.person = person;
		this.paymentDate = paymentDate;
		this.debt = debt;
	}

	public CashPayment(Debt debt, DateTime paymentDate) {
		this(debt.getPerson(), paymentDate, debt);
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public DateTime getDebtDate() {
		return debt.getBillingDate();
	}

	public double getAmount() {
		return debt.getAmount();
	}

	public DateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(DateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getFormatedPaymentDate() {
		try {
			return DateHelper.getDateString(paymentDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getFormatedDebtDate() {
		try {
			return DateHelper.getDateString(getDebtDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
