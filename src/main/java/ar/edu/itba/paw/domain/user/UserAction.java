package ar.edu.itba.paw.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.PersistentEntity;

@Entity
public class UserAction extends PersistentEntity {

	@ManyToOne(optional = false)
	private User user;

	@Column(name = "previous", nullable = true)
	private String previous;

	@Column(name = "subsequent", nullable = true)
	private String subsequent;

	public static enum ControllerType {
		ASSISTANCE, BILLING, ENROLLMENT, PAYMENT, PERSON, SERVICE, USER,
	};

	@Enumerated(EnumType.STRING)
	@Column(name = "controller", nullable = false)
	private ControllerType controller;

	@Column(nullable = true)
	private String className;

	@Column(nullable = false)
	private String method;

	public static enum Action {
		UPDATE, DELETE, CREATE, POST
	};

	@Enumerated(EnumType.STRING)
	@Column(name = "action", nullable = false)
	private Action action;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name = "date", nullable = false)
	private DateTime date;

	// Hibernate requirement
	UserAction() {
	}

	public UserAction(Action action, String className, String previous,
			String subsequent, ControllerType controller, String method,
			User user) {
		this.action = action;
		this.className = className;
		this.previous = previous;
		this.controller = controller;
		this.method = method;
		this.subsequent = subsequent;
		this.user = user;
		this.date = DateTime.now();
		checkAction();
	}

	private void checkAction() {
		if (action.equals(Action.CREATE)) {
			if (previous != null || subsequent == null)
				throw new IllegalArgumentException(
						"INVALID ACTION - CREATE WITH previous:" + previous
								+ " subsequent:" + subsequent);
		} else if (action.equals(Action.DELETE)) {
			if (previous == null || subsequent != null)
				throw new IllegalArgumentException(
						"INVALID ACTION - DELETE WITH previous:" + previous
								+ " subsequent:" + subsequent);
		} else if (action.equals(Action.UPDATE)) {
			if (previous == null || subsequent == null)
				throw new IllegalArgumentException(
						"INVALID ACTION - UPDATE WITH previous:" + previous
								+ " subsequent:" + subsequent);
		}
		return;
	}

	public User getUser() {
		return user;
	}

	public String getPrevious() {
		return previous;
	}

	public String getSubsequent() {
		return subsequent;
	}

	public ControllerType getController() {
		return controller;
	}

	public Action getAction() {
		return action;
	}

}
