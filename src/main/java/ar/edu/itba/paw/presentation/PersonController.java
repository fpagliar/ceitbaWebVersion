package ar.edu.itba.paw.presentation;

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
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.service.ServiceRepo;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.Person.PaymentMethod;
import ar.edu.itba.paw.domain.user.PersonRepo;
import ar.edu.itba.paw.domain.user.UserAction;
import ar.edu.itba.paw.domain.user.UserAction.Action;
import ar.edu.itba.paw.domain.user.UserAction.ControllerType;
import ar.edu.itba.paw.domain.user.UserActionRepo;
import ar.edu.itba.paw.domain.user.UserRepo;
import ar.edu.itba.paw.presentation.command.RegisterPersonForm;
import ar.edu.itba.paw.presentation.command.UpdatePersonForm;
import ar.edu.itba.paw.presentation.command.validator.RegisterPersonFormValidator;
import ar.edu.itba.paw.presentation.command.validator.UpdatePersonFormValidator;

@Controller
public class PersonController {

	private final RegisterPersonFormValidator registerValidator;
	private final UpdatePersonFormValidator updateValidator;
	private final PersonRepo personRepo;
	private final UserRepo userRepo;
	private final UserActionRepo userActionRepo;
	private final ServiceRepo serviceRepo;
	private final EnrollmentRepo enrollmentRepo;

	@Autowired
	public PersonController(final UserRepo userRepo, final RegisterPersonFormValidator validator, final ServiceRepo serviceRepo,
			final UpdatePersonFormValidator updateValidator, final PersonRepo personRepo, final EnrollmentRepo enrollmentRepo,
			final UserActionRepo userActionRepo) {
		super();
		this.userRepo = userRepo;
		this.registerValidator = validator;
		this.personRepo = personRepo;
		this.updateValidator = updateValidator;
		this.userActionRepo = userActionRepo;
		this.serviceRepo = serviceRepo;
		this.enrollmentRepo = enrollmentRepo;
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
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView state(final HttpSession session, @RequestParam("id") final Person person, 
			@RequestParam(value = "success", required = false) final String success) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser()) {
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		}
		if (!userRepo.get(usr.getUsername()).isModerator()) {
			return new ModelAndView("unauthorized");
		}

		final ModelAndView mav = new ModelAndView();
		mav.addObject("person", person);
		mav.addObject("success", success);
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView history(final HttpSession session, @RequestParam("id") final Person person) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser()) {
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		}
		if (!userRepo.get(usr.getUsername()).isModerator()) {
			return new ModelAndView("unauthorized");
		}

		final ModelAndView mav = new ModelAndView();
		mav.addObject("person", person);
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView purchases(final HttpSession session, @RequestParam("id") final Person person) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser()) {
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		}
		if (!userRepo.get(usr.getUsername()).isModerator()) {
			return new ModelAndView("unauthorized");
		}

		final ModelAndView mav = new ModelAndView();
		mav.addObject("person", person);
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView update(@RequestParam("id") final Person person, final HttpSession session,
			@RequestParam(value = "success", required = false) final Boolean success,
			@RequestParam(value = "neww", required = false) final Boolean neww) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser()) {
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		}
		if (!userRepo.get(usr.getUsername()).isModerator()) {
			return new ModelAndView("unauthorized");
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
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView update(final HttpSession session, final UpdatePersonForm form, final Errors errors) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser()) {
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		}
		if (!userRepo.get(usr.getUsername()).isModerator()) {
			return new ModelAndView("unauthorized");
		}

		updateValidator.validate(form, errors);
		if (errors.hasErrors()) {
			return null;
		}
		final Person updatedPerson = personRepo.getById(form.getId());
		final String personBeforeUpdate = updatedPerson.toString();
		updatedPerson.update(form);
		
		userActionRepo.add(new UserAction(Action.UPDATE, Person.class.getName(), personBeforeUpdate, updatedPerson.toString(), 
				ControllerType.PERSON, "update", userRepo.get(usr.getUsername())));
		return new ModelAndView("redirect:update?id=" + updatedPerson.getId() + "&success=true");
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView register(final HttpSession session, final RegisterPersonForm form, final Errors errors) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser()) {
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		}
		if (!userRepo.get(usr.getUsername()).isModerator()) {
			return new ModelAndView("unauthorized");
		}

		registerValidator.validate(form, errors);
		if (errors.hasErrors()) {
			return null;
		}
		final Person p = new Person(form);
		personRepo.add(p);

		userActionRepo.add(new UserAction(Action.CREATE, Person.class.getName(), null, p.toString(),
				ControllerType.PERSON, "register", userRepo.get(usr.getUsername())));
		return new ModelAndView("redirect:update?id=" + p.getId() + "&success=false&neww=true");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView register(final HttpSession session) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser()) {
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		}
		if (!userRepo.get(usr.getUsername()).isModerator()) {
			return new ModelAndView("unauthorized");
		}

		final ModelAndView mav = new ModelAndView();
		mav.addObject("registerPersonForm", new RegisterPersonForm());
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView subscribe(final HttpSession session, @RequestParam("id") final Person person, 
			@RequestParam(value = "success", required = false) final String success) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser()) {
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		}
		if (!userRepo.get(usr.getUsername()).isModerator()) {
			return new ModelAndView("unauthorized");
		}

		final ModelAndView mav = new ModelAndView();
		mav.addObject("person", person);
		mav.addObject("services", serviceRepo.getActive());
		mav.addObject("success", success);
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView subscribe(final HttpSession session, @RequestParam("id") final Person person, 
			@RequestParam("service") final Service service) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser()) {
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		}
		if (!userRepo.get(usr.getUsername()).isModerator()) {
			return new ModelAndView("unauthorized");
		}
		
		enrollmentRepo.add(new Enrollment(person, service));
		final String msg = "La subscripcion fue realizada con exito";
		return new ModelAndView("redirect:subscribe?id=" + person.getId() + "&success=" + msg);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView unsubscribe(final HttpSession session, @RequestParam("id") final Person person,
			@RequestParam("service") final Service service) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser()) {
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		}
		if (!userRepo.get(usr.getUsername()).isModerator()) {
			return new ModelAndView("unauthorized");
		}
		
		person.unsubscribe(service);
		final String msg = "La subscripcion fue cancelada con exito";
		return new ModelAndView("redirect:state?id=" + person.getId() + "&success=" + msg);
	}
}