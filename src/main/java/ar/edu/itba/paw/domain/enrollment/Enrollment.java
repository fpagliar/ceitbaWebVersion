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
import ar.edu.itba.paw.lib.DateHelper;

@Entity
@Table(name = "enrollments")
public class Enrollment extends PersistentEntity {

	private static final long serialVersionUID = -9003737173615892492L;

	@ManyToOne(optional = false)
	private Person person;
	@ManyToOne(optional = false)
	private Service service;
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name = "startDate", nullable = false)
	private DateTime startDate;
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name = "endDate", nullable = true)
	private DateTime endDate;

	Enrollment() {
	}

	public Enrollment(Person person, Service service) {
		this.person = person;
		this.service = service;
		this.startDate = DateTime.now();
		this.endDate = null;
	}

	public Person getPerson() {
		return person;
	}

	public Service getService() {
		return service;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public String getFormatedStartDate() {
		return DateHelper.getDateString(startDate);
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public String getFormatedEndDate() {
		return DateHelper.getDateString(endDate);
	}

	public boolean isActive() {
		checkExpiration();
		return endDate == null;
	}

	public boolean hasExpired() {
		return !isActive();
	}

	private void checkExpiration() {
		// It means that the service is infinite, the enrollment does not expire
		if (service.getMonthsDuration() == 0)
			return;
		DateTime expirationDate = startDate.plusMonths(service
				.getMonthsDuration());
		if (DateTime.now().isAfter(expirationDate)) {
			endDate = expirationDate;
		}
	}

	public void checkActive() {
		checkExpiration();
	}

	public void cancel() {
		if (endDate == null)
			endDate = DateTime.now();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Enrollment))
			return false;
		Enrollment other = (Enrollment) obj;
		return person.equals(other.getPerson())
				&& startDate.equals(other.getStartDate())
				&& service.equals(other.getService());
	}
	
	@Override
	public String toString() {
		return "id: " + getId() + " start_date: "
				+ DateHelper.getDateString(startDate) + " end_date: "
				+ DateHelper.getDateString(endDate);
	}
}