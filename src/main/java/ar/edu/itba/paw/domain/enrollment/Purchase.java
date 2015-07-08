package ar.edu.itba.paw.domain.enrollment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.PersistentEntity;
import ar.edu.itba.paw.domain.service.Product;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.lib.DateHelper;

@Entity
@Table(name = "purchases")
public class Purchase extends PersistentEntity {

	private static final long serialVersionUID = -1803565053176306298L;

	@ManyToOne(optional = false)
	private Person person;
	@ManyToOne(optional = false)
	private Product product;
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name = "date", nullable = false)
	private DateTime date;
	@Column(name = "billed", nullable = false)
	private boolean billed;

	// Default constructor for hibernate
	Purchase() {
	}
	
	public Purchase(final Person person, final Product product) {
		this.person = person;
		this.product = product;
		this.date = DateTime.now();
		this.billed = false;
	}

	public Person getPerson() {
		return person;
	}

	public Product getProduct() {
		return product;
	}

	public DateTime getDate() {
		return date;
	}
	
	public void bill() {
		this.billed = true;
	}

	public String getFormattedDate() {
		return DateHelper.getDateString(date);
	}
	
	@Override
	public String toString() {
		return "personId: " + person + " product: " + product + " date: " + date + " billed: " + billed;
	}
}
