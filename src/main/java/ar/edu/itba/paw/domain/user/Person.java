package ar.edu.itba.paw.domain.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.PersistentEntity;
import ar.edu.itba.paw.domain.enrollment.Enrollment;
import ar.edu.itba.paw.domain.payment.CashPayment;
import ar.edu.itba.paw.domain.payment.Debt;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.validators.CeitbaValidator;

@Entity
@Table(name = "person")
public class Person extends PersistentEntity {

	@Column(name = "first_name", unique = false, nullable = true)
	private String firstName;
	@Column(name = "last_name", unique = false, nullable = true)
	private String lastName;
	@Column(name = "email", unique = false, nullable = true)
	private String email;
	@Column(name = "legacy", unique = false, nullable = false)
	private int legacy;
	@Column(name = "phone", unique = false, nullable = true)
	private String phone;
	@Column(name = "cellphone", unique = false, nullable = true)
	private String cellphone;
	@Column(name = "email2", unique = false, nullable = true)
	private String email2;
	@Column(name = "dni", unique = false, nullable = true)
	private String dni;
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name = "registration", nullable = false)
	private DateTime registration;

	@ManyToMany
	@JoinTable(name = "person_enrollment", inverseJoinColumns = { @JoinColumn(name = "person") }, joinColumns = { @JoinColumn(name = "id") })
	private List<Enrollment> enrolledServices = new ArrayList<Enrollment>();

	public static enum PaymentMethod {
		BILL, CASH
	};

	@Enumerated(EnumType.STRING)
	@Column(name = "method", nullable = false)
	private PaymentMethod paymentMethod;
	
	@OneToMany
	private List<CashPayment> payments = new ArrayList<CashPayment>();
	@OneToMany
	private List<Debt> debts = new ArrayList<Debt>();
	
	// Hibernate requirement
	Person() {
	}

	public Person(Integer legacy) {
		this.legacy = legacy;
		this.registration = DateTime.now();
	}

	public Person(String firstName, String lastName, Integer legacy) {
		this(legacy);
		setFirstName(firstName);
		setLastName(lastName);
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getEmail() {
		return email;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public void setLegacy(int legacy) {
		this.legacy = legacy;
	}

	/* Getters */

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getMail() {
		return this.email;
	}

	public DateTime getRegistration() {
		return this.registration;
	}

	public int getLegacy() {
		return this.legacy;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getCellphone() {
		return this.cellphone;
	}

	@Override
	public String toString() {
		return "Name: " + firstName + "\n Surname: " + lastName + "\n Mail: "
				+ email + "\n Id: " + getId();
	}

	/* Setters */

	public void setFirstName(String name) {
		this.firstName = name;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String mail) {
		if (!CeitbaValidator.validateMail(mail)) {
			throw new IllegalArgumentException();
		}
		this.email = mail;
	}

	public void setPhone(String phone) {
		if (!CeitbaValidator.validatePhone(phone)) {
			throw new IllegalArgumentException();
		}
		this.phone = phone;
	}

	public void setCellphone(String phone) {
		if (!CeitbaValidator.validatePhone(phone)) {
			throw new IllegalArgumentException();
		}
		this.cellphone = phone;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		Person user = (Person) obj;
		return this.legacy == (user.getLegacy());
	}

	@Override
	public int hashCode() {
		return legacy;
	}

	private void validateEmpty(String s) {
		if (s == null || s.length() == 0) {
			throw new IllegalArgumentException();
		}
		return;
	}

	public List<Enrollment> getActiveEnrollments() {
		List<Enrollment> ans = new ArrayList<Enrollment>();
		for (Enrollment e : enrolledServices)
			if (e.isActive())
				ans.add(e);
		return ans;
	}

	public List<Enrollment> getEnrollmentsHistory() {
		List<Enrollment> ans = new ArrayList<Enrollment>();
		ans.addAll(enrolledServices);
		return ans;
	}

	/**
	 * SHOULD ONLY BE CALLED BY THE ENROLLMENT REPO!!!
	 */
	public void enroll(Enrollment e) {
		for (Enrollment active : enrolledServices)
			if (active.getService().equals(e.getService()) && e.isActive())
				return;
		enrolledServices.add(e);
	}

	public void unenroll(Service s) {
		for (Enrollment e : enrolledServices)
			if (e.getService().equals(s))
				e.cancel();
	}

	public void pay(Debt debt, DateTime paymentDate) {
		debts.remove(debt);
		payments.add(new CashPayment(debt, paymentDate));
	}
	
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	
	public void setPaymentMethod(PaymentMethod paymentMethod){
		this.paymentMethod = paymentMethod;
	}	
}