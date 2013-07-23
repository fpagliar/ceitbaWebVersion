package ar.edu.itba.paw.domain.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ar.edu.itba.paw.domain.PersistentEntity;
import ar.edu.itba.paw.domain.enrollment.Enrollment;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.validators.ValidateNull;
import ar.edu.itba.paw.validators.CeitbaValidator;

@Entity
@Table(name="person")
public class Person extends PersistentEntity {

	@Column(name="first_name", unique=false, nullable=true)
	private String firstName;
	@Column(name="last_name", unique=false, nullable=true)
	private String lastName;
	@Column(name="email", unique=true, nullable=true)
	private String email;
	@Column(name="legacy", unique=true, nullable=false)
	private int legacy;
	@Column(name="phone", unique=true, nullable=true)
	private String phone;
	@Column(name="cellphone", unique=true, nullable=true)
	private String cellphone;
	@Column(name="email2", unique=true, nullable=true)
	private String email2;
	@Column(name="dni", unique=true, nullable=true)
	private int dni;
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@Column(name="registration", nullable=false)
	private DateTime registration;
	
//	@Column(name="username", unique=true, nullable=false)
//	private String username;
//	@Column(name="password", nullable=false)
//	private String password;

//	public static enum Level {ADMINISTRATOR, REGULAR};
//	@Enumerated(EnumType.STRING)
//	@Column(name="level", nullable=false)
//	private Level level;
	@ManyToMany
	@JoinTable(name="service_person", inverseJoinColumns={@JoinColumn(name="person")}, joinColumns={@JoinColumn(name="id")})
	private List<Enrollment> enrolledServices = new ArrayList<Enrollment>();
//	@OneToMany(mappedBy="user", cascade = CascadeType.REMOVE)
//	private Set<Comment> comments = new HashSet<Comment>();

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
	
	public int getDni() {
		return dni;
	}
	
	public void setDni(int dni) {
		this.dni = dni;
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
	
	public int getLegacy(){
		return this.legacy;
	}
	
	public String getPhone(){
		return this.phone;
	}
	
	public String getCellphone(){
		return this.cellphone;
	}

//	public List<Comment> getComments() {
//		List<Comment> ans = new ArrayList<Comment>();
//		ans.addAll(comments);
//		Collections.sort(ans, new CommentRatingComparator());
//		return ans;
//	}

	@Override
	public String toString() {
		return "Name: " + firstName + "\n Surname: " + lastName + "\n Mail: " + email
				+ "\n Id: " + getId();
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

	public void setPhone(String phone){
		if (!CeitbaValidator.validatePhone(phone)) {
			throw new IllegalArgumentException();
		}
		this.phone = phone;
	}
	
	public void setCellphone(String phone){
		if (!CeitbaValidator.validatePhone(phone)) {
			throw new IllegalArgumentException();
		}
		this.cellphone = phone;
	}
	
	@Override
	public boolean equals(Object obj) {
		try {
			ValidateNull.validate(obj);
		} catch (NullPointerException e) {
			return false;
		}
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

	/**
	 * 
	 * @return - a list with all the active enrollments
	 */
	public List<Enrollment> getEnrollments(){
//	first update to clear the expired enrollments
		for(Enrollment e: enrolledServices){
			if(e.hasExpired()){
				enrolledServices.remove(e);
				//TODO: add the enrollments to the history section
			}
		}
		List<Enrollment> ans = new ArrayList<Enrollment>();
		ans.addAll(enrolledServices);
		return ans;
	}
	
	/**
	 * SHOULD ONLY BE CALLED BY THE ENROLLMENT REPO!!!
	 * @param s
	 */
	public void enroll(Enrollment e){
		if(!enrolledServices.contains(e))
			enrolledServices.add(e);
		return;
	}
	
	public void unenroll(Service s){
		enrolledServices.remove(s);
		//TODO: check if it has to be removed directly or it 
		// has to be saved to take it for cost in the last month
//		if(enrolledServices.remove(s))
//			TODO: add it to history
	}
	
	/**
	 * @return - the list with all the favourite {@link Restaurant} for this
	 *         {@link Person}
	 */
//	public List<Restaurant> getFavourites() {
//		List<Restaurant> list = new ArrayList<Restaurant>();
//		list.addAll(favourites);
//		return list;
//	}

	/**
	 * @param restaurant
	 *            - the restaurant that has to be considered as a favourite.
	 *            IMPORTANT: this {@link Restaurant} has to be persisted (IF
	 *            NOT, I AM NOT RESPONSIBLE FOR THE SHIT THAT WILL COME)!!!!
	 * @return - true if it was added as a favourite false if it was already a
	 *         favourite.
	 */
//	public boolean addFavourite(Restaurant restaurant) {
//		return favourites.add(restaurant);
//	}

	/**
	 * @param restaurant
	 *            - the {@link Restaurant} that has to be removed from the
	 *            {@link Person}'s favourites.
	 * @return - true if the {@link Restaurant} was removed from favourites,
	 *         false if not (the {@link Restaurant} was not in the favourites).
	 */
//	public boolean removeFavourite(Restaurant restaurant) {
//		return favourites.remove(restaurant);
//	}
//
//	public boolean isFavourite(Restaurant restaurant) {
//		return favourites.contains(restaurant);
//	}

	/**
	 * @param c
	 *            the comment made by the user
	 */
//	public void addComment(Comment c) {
//		comments.add(c);
//		return;
//	}
//
//	public void removeComment(Comment c) {
//		comments.remove(c);
//		return;
//	}

	/**
	 * @param n
	 *            - the number of favourite {@link Restaurant} required.
	 * @return a {@link List} with n restaurants taken randomly from the
	 *         {@link Person}'s favourites.
	 */
//	public List<Restaurant> getNFavouriteRestaurants(int n) {
//		return RandomSublist.getRandomNFromList(getFavourites(), n);
//	}

	/**
	 * @param restaurant
	 *            - the restaurant that has to be checked for comments.
	 * @return - a boolean representing if the {@link Person} has commented the
	 *         {@link Restaurant} provided.
	 */
//	public boolean hasCommented(Restaurant restaurant) {
//		for(Comment c: comments){
//			if(c.getRestaurant().equals(restaurant)){
//				return true;
//			}
//		}
//		return false;
//	}
}