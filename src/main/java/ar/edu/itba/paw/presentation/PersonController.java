package ar.edu.itba.paw.presentation;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.domain.enrollment.Enrollment;
import ar.edu.itba.paw.domain.enrollment.EnrollmentRepo;
import ar.edu.itba.paw.domain.enrollment.Purchase;
import ar.edu.itba.paw.domain.enrollment.PurchaseRepo;
import ar.edu.itba.paw.domain.service.Product;
import ar.edu.itba.paw.domain.service.ProductRepo;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.service.ServiceRepo;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.Person.PaymentMethod;
import ar.edu.itba.paw.domain.user.PersonRepo;
import ar.edu.itba.paw.domain.user.UserActionRepo;
import ar.edu.itba.paw.domain.user.UserRepo;
import ar.edu.itba.paw.presentation.command.ProductForm;
import ar.edu.itba.paw.presentation.command.RegisterPersonForm;
import ar.edu.itba.paw.presentation.command.UpdatePersonForm;
import ar.edu.itba.paw.presentation.command.validator.ProductFormValidator;
import ar.edu.itba.paw.presentation.command.validator.RegisterPersonFormValidator;
import ar.edu.itba.paw.presentation.command.validator.UpdatePersonFormValidator;

@Controller
public class PersonController {

	private final PersonRepo personRepo;
	private final UserRepo userRepo;
	private final UserActionRepo userActionRepo;
	private final ServiceRepo serviceRepo;
	private final EnrollmentRepo enrollmentRepo;
	private final PurchaseRepo purchaseRepo;
	private final ProductRepo productRepo;
	private final RegisterPersonFormValidator registerValidator;
	private final UpdatePersonFormValidator updateValidator;
	private final ProductFormValidator productValidator;

	@Autowired
	public PersonController(final UserRepo userRepo, final ServiceRepo serviceRepo,
			final PersonRepo personRepo, final EnrollmentRepo enrollmentRepo,
			final PurchaseRepo purchaseRepo, final ProductRepo productRepo, final UserActionRepo userActionRepo, 
			final UpdatePersonFormValidator updateValidator, final RegisterPersonFormValidator registerValidator, 
			final ProductFormValidator productValidator) {
		super();
		this.userRepo = userRepo;
		this.registerValidator = registerValidator;
		this.updateValidator = updateValidator;
		this.productValidator = productValidator;
		this.personRepo = personRepo;
		this.userActionRepo = userActionRepo;
		this.serviceRepo = serviceRepo;
		this.enrollmentRepo = enrollmentRepo;
		this.purchaseRepo = purchaseRepo;
		this.productRepo = productRepo;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listAll(final HttpSession session, 
			@RequestParam(value = "search", required = false) final String search,
			@RequestParam(value = "page", required = false, defaultValue = "1") final int page) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser()) {
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		}

		final ModelAndView mav = new ModelAndView();
		if (search != null && search != "") {
			mav.addObject("persons", personRepo.search(search, page));
			mav.addObject("search", true);
			mav.addObject("searchParam", search);
		} else {
			mav.addObject("persons", personRepo.getAll(page));
		}
		mav.addObject("menu", "listAll");
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView state(final HttpSession session, @RequestParam("id") final Person person, 
			@RequestParam(value = "success", required = false) final String success) {
		final ModelAndView moderatorCheck = checkForModerator(session);
		if (moderatorCheck != null) {
			return moderatorCheck;
		}

		final ModelAndView mav = new ModelAndView();
		mav.addObject("person", person);
		mav.addObject("enrollments", enrollmentRepo.getActive(person));
		mav.addObject("success", success);
		mav.addObject("menu", "state");
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView history(final HttpSession session, @RequestParam("id") final Person person) {
		final ModelAndView moderatorCheck = checkForModerator(session);
		if (moderatorCheck != null) {
			return moderatorCheck;
		}

		final ModelAndView mav = new ModelAndView();
		mav.addObject("person", person);
		mav.addObject("enrollments", enrollmentRepo.getExpired(person));
		mav.addObject("menu", "history");
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView purchases(final HttpSession session, @RequestParam("id") final Person person) {
		final ModelAndView moderatorCheck = checkForModerator(session);
		if (moderatorCheck != null) {
			return moderatorCheck;
		}

		final ModelAndView mav = new ModelAndView();
		mav.addObject("person", person);
		mav.addObject("menu", "purchases");
		mav.addObject("pendingPurchases", purchaseRepo.getPending(person));
		mav.addObject("billedPurchases", purchaseRepo.getBilled(person));
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView purchase(final HttpSession session, @RequestParam("id") final Person person) {
		final ModelAndView moderatorCheck = checkForModerator(session);
		if (moderatorCheck != null) {
			return moderatorCheck;
		}

		final ModelAndView mav = new ModelAndView();
		mav.addObject("person", person);
		mav.addObject("menu", "purchase");
		mav.addObject("products", productRepo.getAll());
		mav.addObject("productForm", new ProductForm());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView purchase(final HttpSession session, @RequestParam("id") final Person person, 
			@RequestParam("product") final Product product) {
		final ModelAndView moderatorCheck = checkForModerator(session);
		if (moderatorCheck != null) {
			return moderatorCheck;
		}
		
		final Purchase purchase = new Purchase(person, product);
		purchaseRepo.save(purchase);
		return new ModelAndView("redirect:purchase?id=" + person.getId());
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView reimburse(final HttpSession session, @RequestParam("id") final Purchase purchase) {
		final ModelAndView moderatorCheck = checkForModerator(session);
		if (moderatorCheck != null) {
			return moderatorCheck;
		}
		purchaseRepo.delete(purchase);

		return new ModelAndView("redirect:purchases?id=" + purchase.getPerson().getId());
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView newProduct(final HttpSession session, final ProductForm form, final Errors errors) {
		final ModelAndView moderatorCheck = checkForModerator(session);
		if (moderatorCheck != null) {
			return moderatorCheck;
		}

		productValidator.validate(form, errors);
		if (errors.hasErrors()) {
			return new ModelAndView("redirect:purchase?id=" + form.getPersonId());
		}
		productRepo.save(new Product(form));
		return new ModelAndView("redirect:purchase?id=" + form.getPersonId());
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView update(@RequestParam("id") final Person person, final HttpSession session,
			@RequestParam(value = "success", required = false) final Boolean success,
			@RequestParam(value = "neww", required = false) final Boolean neww) {
		final ModelAndView moderatorCheck = checkForModerator(session);
		if (moderatorCheck != null) {
			return moderatorCheck;
		}

		final ModelAndView mav = new ModelAndView();
		mav.addObject("person", person);
		if (neww != null && neww) {
			mav.addObject("neww", true);
			mav.addObject("newmsg", "Usuario creado correctamente");
		}
		if (success == null) {
			mav.addObject("success", false);
		} else {
			mav.addObject("success", success);
			mav.addObject("successmsg", "La informacion se ha modificado correctamente");
		}
		mav.addObject("updatePersonForm", new UpdatePersonForm());
		mav.addObject("isCash", person.getPaymentMethod().equals(PaymentMethod.CASH));
		mav.addObject("menu", "update");
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView update(final HttpSession session, final UpdatePersonForm form, final Errors errors) {
		final ModelAndView moderatorCheck = checkForModerator(session);
		if (moderatorCheck != null) {
			return moderatorCheck;
		}

		updateValidator.validate(form, errors);
		if (errors.hasErrors()) {
			return null;
		}
		final Person updatedPerson = personRepo.getById(form.getId());
		final String personBeforeUpdate = updatedPerson.toString();
		updatedPerson.update(form);
		
//		userActionRepo.add(new UserAction(Action.UPDATE, Person.class.getName(), personBeforeUpdate, updatedPerson.toString(), 
//				ControllerType.PERSON, "update", userRepo.get(usr.getUsername())));
		return new ModelAndView("redirect:update?id=" + updatedPerson.getId() + "&success=true");
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView register(final HttpSession session, final RegisterPersonForm form, final Errors errors) {
		final ModelAndView moderatorCheck = checkForModerator(session);
		if (moderatorCheck != null) {
			return moderatorCheck;
		}

		registerValidator.validate(form, errors);
		if (errors.hasErrors()) {
			return null;
		}
		final Person p = new Person(form);
		personRepo.add(p);

//		userActionRepo.add(new UserAction(Action.CREATE, Person.class.getName(), null, p.toString(),
//				ControllerType.PERSON, "register", userRepo.get(usr.getUsername())));
		return new ModelAndView("redirect:update?id=" + p.getId() + "&success=false&neww=true");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView register(final HttpSession session) {
		final ModelAndView moderatorCheck = checkForModerator(session);
		if (moderatorCheck != null) {
			return moderatorCheck;
		}

		final ModelAndView mav = new ModelAndView();
		mav.addObject("registerPersonForm", new RegisterPersonForm());
		mav.addObject("menu", "register");
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView subscribe(final HttpSession session, @RequestParam("id") final Person person, 
			@RequestParam(value = "success", required = false) final String success) {
		final ModelAndView moderatorCheck = checkForModerator(session);
		if (moderatorCheck != null) {
			return moderatorCheck;
		}

		final ModelAndView mav = new ModelAndView();
		mav.addObject("person", person);
		mav.addObject("services", serviceRepo.getActive());
		mav.addObject("enrollments", enrollmentRepo.getActive(person));
		mav.addObject("success", success);
		mav.addObject("menu", "subscribe");
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView subscribe(final HttpSession session, @RequestParam("id") final Person person, 
			@RequestParam("service") final Service service) {
		final ModelAndView moderatorCheck = checkForModerator(session);
		if (moderatorCheck != null) {
			return moderatorCheck;
		}

		enroll(person, service);
		final String msg = "La subscripcion fue realizada con exito";
		return new ModelAndView("redirect:subscribe?id=" + person.getId() + "&success=" + msg);
	}
	
	private void enroll(final Person person, final Service service) {
		if (!service.getName().equals("ceitba")) {
			boolean ceitbaEnrolled = false;
			for (final Enrollment e : enrollmentRepo.getActive(person)) {
				ceitbaEnrolled = ceitbaEnrolled || e.getService().getName().equals("ceitba");
			}
			if (!ceitbaEnrolled) {
				final Service ceitba = serviceRepo.get("ceitba");
				enrollmentRepo.add(new Enrollment(person, ceitba));				
			}
		}
		enrollmentRepo.add(new Enrollment(person, service));
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView unsubscribe(final HttpSession session, @RequestParam("id") final Person person,
			@RequestParam("service") final Service service) {
		final ModelAndView moderatorCheck = checkForModerator(session);
		if (moderatorCheck != null) {
			return moderatorCheck;
		}
		
		final List<Enrollment> enrollments;
		if (service.getName().equals("ceitba")) {
			enrollments = enrollmentRepo.get(person); // If it is ceitba, cancel all the enrollments
		} else {
			enrollments = enrollmentRepo.get(person, service);
		}
		for (final Enrollment e : enrollments) {
			e.cancel(); // cancels all the subscriptions instead of looking for the active one
		}
		
		final String msg = "La subscripcion fue cancelada con exito";
		return new ModelAndView("redirect:state?id=" + person.getId() + "&success=" + msg);
	}
	
	private ModelAndView checkForModerator(final HttpSession session) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser()) {
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		}
		if (!userRepo.get(usr.getUsername()).isModerator()) {
			return new ModelAndView("unauthorized");
		}
		return null;
	}
}