package ar.edu.itba.paw.domain.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.PersistentEntity;
import ar.edu.itba.paw.domain.enrollment.Enrollment;
import ar.edu.itba.paw.domain.enrollment.Purchase;
import ar.edu.itba.paw.domain.payment.CashPayment;
import ar.edu.itba.paw.domain.payment.Debt;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.presentation.command.RegisterPersonForm;
import ar.edu.itba.paw.presentation.command.UpdatePersonForm;
import ar.edu.itba.paw.validators.CeitbaValidator;

@Entity
@Table(name = "person")
public class Person extends PersistentEntity {

	private static final long serialVersionUID = -8435774954046238702L;
	
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

	@OneToMany(cascade = CascadeType.ALL)
	private List<Enrollment> enrollments = new ArrayList<Enrollment>();

	@OneToMany(cascade = CascadeType.ALL)
	private List<Purchase> purchases = new ArrayList<Purchase>();
	
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

	public Person(final Integer legacy) {
		this.legacy = legacy;
		this.registration = DateTime.now();
	}

	public Person(final String firstName, final String lastName, final Integer legacy) {
		this(legacy);
		setFirstName(firstName);
		setLastName(lastName);
	}
	
	public Person(final RegisterPersonForm form) {
		this(form.getFirstName(), form.getLastName(), Integer.valueOf(form.getLegacy()));
		setCellphone(form.getCellphone());
		setDni(form.getDni());
		setEmail(form.getEmail());
		setEmail2(form.getEmail2());
		setPhone(form.getPhone());
		if (form.getPaymentMethod().equals(PaymentMethod.CASH.toString())) {
			setPaymentMethod(PaymentMethod.CASH);
		} else {
			setPaymentMethod(PaymentMethod.BILL);
		}
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(final String email2) {
		if(isEmpty(email2)) {
			this.email2 = null;
		} else {			
			this.email2 = email2;
		}
	}

	public String getEmail() {
		return email;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(final String dni) {
		if(!isEmpty(dni))
			this.dni = dni;
	}

	public void setLegacy(final int legacy) {
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
		return "id: " + getId() + " first_name: " + firstName + " last_name: " + lastName + " email: " + email +
				" legacy: " + legacy + " phone: " + phone + " cellphone: " + cellphone + " email2: " + email2 +
				" dni: " + dni + " payment_method: " + paymentMethod;
	}

	/* Setters */

	public void setFirstName(final String name) {
		if(isEmpty(name)) {
			this.firstName = null;			
		} else {			
			this.firstName = name;
		}
	}

	public void setLastName(final String lastName) {
		if(isEmpty(lastName)) {
			this.lastName = null;			
		} else {			
			this.lastName = lastName;
		}
	}

	public void setEmail(final String mail) {
		if (!CeitbaValidator.validateMail(mail)) {
			throw new IllegalArgumentException();
		}
		this.email = mail;
	}

	public void setPhone(final String phone) {
		if (!CeitbaValidator.validatePhone(phone)) {
			throw new IllegalArgumentException();
		}
		this.phone = phone;
	}

	public void setCellphone(final String phone) {
		if (!CeitbaValidator.validatePhone(phone)) {
			throw new IllegalArgumentException();
		}
		this.cellphone = phone;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;			
		}
		final Person user = (Person) obj;
		return this.legacy == (user.getLegacy());
	}

	@Override
	public int hashCode() {
		return legacy;
	}

	private boolean isEmpty(final String s) {
		return s == null || s.length() == 0;
	}

	public List<Enrollment> getActiveEnrollments() {
		final List<Enrollment> ans = new ArrayList<Enrollment>();
		for (Enrollment e : enrollments) {			
			if (e.isActive()) {				
				ans.add(e);
			}
		}
		return ans;
	}

	public List<Enrollment> getEnrollmentsHistory() {
		final List<Enrollment> ans = new ArrayList<Enrollment>();
		ans.addAll(enrollments);
		return ans;
	}

	/**
	 * SHOULD ONLY BE CALLED BY THE ENROLLMENT REPO!!!
	 */
	public void enroll(final Enrollment e) {
		for (final Enrollment active : enrollments) {			
			if (active.getService().equals(e.getService()) && e.isActive()) {				
				return;
			}
		}
		enrollments.add(e);
	}

//	public void unenroll(final Service s) {
//		for (Enrollment e : enrollments)
//			if (e.getService().equals(s))
//				e.cancel();
//	}
		
//	public Debt consume(final Service s) {
//		Debt d = new Debt(this, s.getValue(), DateTime.now(), s.getName());
//		debts.add(d);
//		return d;
//	}

	public void purchase(final Purchase p) {
		purchases.add(p);
	}

	public void reimburse(final Purchase p) {
		purchases.remove(p);
	}

	public CashPayment pay(final Debt debt, final DateTime paymentDate) {
		debts.remove(debt);
		final CashPayment payment = new CashPayment(debt, paymentDate);
		payments.add(payment);
		return payment;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(final PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	public void update(final UpdatePersonForm form) {
		setLegacy(Integer.parseInt(form.getLegacy()));
		setFirstName(form.getFirstName());
		setLastName(form.getLastName());
		setEmail(form.getEmail());
		setPhone(form.getPhone());
		setCellphone(form.getCellphone());
		setDni(form.getDni());
		setEmail2(form.getEmail2());
		if (form.getPaymentMethod().equals(PaymentMethod.CASH.toString())) {
			setPaymentMethod(PaymentMethod.CASH);
		} else {
			setPaymentMethod(PaymentMethod.BILL);
		}
	}
	
	public List<Purchase> getPurchases() {
		return new ArrayList<Purchase>(purchases);
	}
	
	public void unsubscribe(final Service service) {
		for (final Enrollment e : enrollments) {
			if (e.isActive() && e.getService().equals(service)) {
				e.cancel();
				return;
			}
		}
	}
}