package ar.edu.itba.paw.presentation;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.domain.enrollment.EnrollmentRepo;
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

	private RegisterPersonFormValidator registerValidator;
	private UpdatePersonFormValidator updateValidator;
	private PersonRepo personRepo;
	private EnrollmentRepo enrollmentRepo;
	private UserRepo userRepo;
	private UserActionRepo userActionRepo;

	@Autowired
	public PersonController(UserRepo userRepo, RegisterPersonFormValidator validator,
			UpdatePersonFormValidator updateValidator, PersonRepo personRepo, EnrollmentRepo enrollmentRepo,
			UserActionRepo userActionRepo) {
		super();
		this.userRepo = userRepo;
		this.registerValidator = validator;
		this.personRepo = personRepo;
		this.updateValidator = updateValidator;
		this.enrollmentRepo = enrollmentRepo;
		this.userActionRepo = userActionRepo;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listAll(final HttpSession session, 
			@RequestParam(value = "search", required = false) final String search,
			@RequestParam(value = "page", required = false, defaultValue = "1") final int page) {

		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		ModelAndView mav = new ModelAndView();
		if (search != null && search != "") {
			mav.addObject("persons", personRepo.search(search, page));
			mav.addObject("search", true);
			mav.addObject("searchParam", search);
		} else
			mav.addObject("persons", personRepo.getAll(page));
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView update(@RequestParam("id") Person person, HttpSession session,
			@RequestParam(value = "success", required = false) Boolean success,
			@RequestParam(value = "neww", required = false) Boolean neww) {

		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

		ModelAndView mav = new ModelAndView();
		mav.addObject("person", person);
		if (neww != null && neww) {
			mav.addObject("new", true);
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
		mav.addObject("enrollments", enrollmentRepo.getActive(person));
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView update(HttpSession session, UpdatePersonForm form, Errors errors) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

		updateValidator.validate(form, errors);
		Person updatedPerson = personRepo.getById(form.getId());
		if (errors.hasErrors()) {
			return null;
		}
		String personBeforeUpdate = updatedPerson.toString();

		updatedPerson.setLegacy(Integer.parseInt(form.getLegacy()));
		updatedPerson.setFirstName(form.getFirstName());
		updatedPerson.setLastName(form.getLastName());
		updatedPerson.setEmail(form.getEmail());
		updatedPerson.setPhone(form.getPhone());
		updatedPerson.setCellphone(form.getCellphone());
		updatedPerson.setDni(form.getDni());
		updatedPerson.setEmail2(form.getEmail2());
		if (form.getPaymentMethod().equals(PaymentMethod.CASH.toString())) {
			updatedPerson.setPaymentMethod(PaymentMethod.CASH);
		} else {
			updatedPerson.setPaymentMethod(PaymentMethod.BILL);
		}

		userActionRepo.add(new UserAction(Action.UPDATE, Person.class.getName(), personBeforeUpdate, updatedPerson
				.toString(), ControllerType.PERSON, "update", userRepo.get(usr.getUsername())));
		return new ModelAndView("redirect:update?id=" + updatedPerson.getId() + "&success=true");
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView register(HttpSession session, RegisterPersonForm form, Errors errors) {

		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

		registerValidator.validate(form, errors);
		if (errors.hasErrors()) {
			return null;
		}
		Person p = new Person(form.getFirstName(), form.getLastName(), Integer.valueOf(form.getLegacy()));
		p.setCellphone(form.getCellphone());
		p.setDni(form.getDni());
		p.setEmail(form.getEmail());
		p.setEmail2(form.getEmail2());
		p.setPhone(form.getPhone());
		if (form.getPaymentMethod().equals(PaymentMethod.CASH.toString())) {
			p.setPaymentMethod(PaymentMethod.CASH);
		} else {
			p.setPaymentMethod(PaymentMethod.BILL);
		}
		personRepo.add(p);

		userActionRepo.add(new UserAction(Action.CREATE, Person.class.getName(), null, p.toString(),
				ControllerType.PERSON, "register", userRepo.get(usr.getUsername())));
		return new ModelAndView("redirect:update?id=" + p.getId() + "&success=false&neww=true");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView register(HttpSession session) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

		ModelAndView mav = new ModelAndView();
		mav.addObject("registerPersonForm", new RegisterPersonForm());
		return mav;
	}
}