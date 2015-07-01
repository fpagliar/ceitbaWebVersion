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
import ar.edu.itba.paw.domain.payment.Debt;
import ar.edu.itba.paw.domain.payment.DebtRepo;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.service.Service.Type;
import ar.edu.itba.paw.domain.service.ServiceRepo;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.PersonRepo;
import ar.edu.itba.paw.domain.user.UserAction;
import ar.edu.itba.paw.domain.user.UserAction.Action;
import ar.edu.itba.paw.domain.user.UserAction.ControllerType;
import ar.edu.itba.paw.domain.user.UserActionRepo;
import ar.edu.itba.paw.domain.user.UserRepo;
import ar.edu.itba.paw.presentation.command.RegisterEnrollmentForm;
import ar.edu.itba.paw.presentation.command.validator.RegisterEnrollmentFormValidator;

@Controller
public class EnrollmentController {

	private EnrollmentRepo enrollmentRepo;
	private ServiceRepo serviceRepo;
	private PersonRepo personRepo;
	private UserRepo userRepo;
	private DebtRepo debtRepo;
	private RegisterEnrollmentFormValidator validator;
	private UserActionRepo userActionRepo;

	@Autowired
	public EnrollmentController(final UserRepo userRepo, final EnrollmentRepo enrollmentRepo, final ServiceRepo serviceRepo,
			final PersonRepo personRepo, final DebtRepo debtRepo, final RegisterEnrollmentFormValidator validator,
			final UserActionRepo userActionRepo) {
		super();
		this.userRepo = userRepo;
		this.enrollmentRepo = enrollmentRepo;
		this.serviceRepo = serviceRepo;
		this.personRepo = personRepo;
		this.debtRepo = debtRepo;
		this.validator = validator;
		this.userActionRepo = userActionRepo;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listAll(final HttpSession session, @RequestParam(value = "list", required = false) final String value,
			@RequestParam(value = "search", required = false) final String search,
			@RequestParam(value = "serviceName", required = false) final String serviceName, 
			@RequestParam(value = "page", required = false, defaultValue = "1") final int page) {

		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		final ModelAndView mav = new ModelAndView();
		if ("history".equals(value)) {
			mav.addObject("historyEnrollments", enrollmentRepo.getExpired(page));
			mav.addObject("history", true);
		}
		if (serviceName != null) {
			mav.addObject("enrollments", enrollmentRepo.getActive(serviceRepo.get(serviceName), page));
			mav.addObject("service", serviceName);
		}
//		if (search != null && search != "") {
//			mav.addObject("personEnrollments", enrollmentRepo.getActivePersonsList(personRepo.search(search, 1).getElements(), page));
//			mav.addObject("serviceEnrollments", enrollmentRepo.getActiveServiceList(serviceRepo.search(search), page));
//			mav.addObject("search", true);
//		}
		if (value == null && (search == null  || search == "") && serviceName == null)
			mav.addObject("enrollments", enrollmentRepo.getActive(page));
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView show(@RequestParam("id") final Enrollment enrollment, final HttpSession session,
			@RequestParam(value = "neww", required = false) final Boolean neww) {

		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		if (enrollment == null)
			return new ModelAndView("redirect:listAll");
		final ModelAndView mav = new ModelAndView();
		mav.addObject("enrollment", enrollment);
		if (neww != null && neww) {
			mav.addObject("neww", true);
			mav.addObject("newmsg", "Subscripcion creada correctamente");
		}
		mav.addObject("isActive", enrollment.isActive());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView register(final HttpSession session, final RegisterEnrollmentForm form, final Errors errors) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

		final ModelAndView mav = new ModelAndView();
		validator.validate(form, errors);
		if (errors.hasErrors()) {
			mav.addObject("consumables", serviceRepo.getActiveConsumables());
			mav.addObject("consumables", serviceRepo.getActiveConsumables());
			return mav;
		}
		final Integer legacy = Integer.parseInt(form.getLegacy());
		final Service service = serviceRepo.get(form.getServiceName());
		final Person person = personRepo.getByLegacy(legacy);
		if (service.getType().equals(Type.CONSUMABLE)) {
			final Debt debt = person.consume(service);
			debtRepo.add(debt);
			userActionRepo.add(new UserAction(Action.CREATE, Debt.class.getName(), null, debt.toString(),
					ControllerType.ENROLLMENT, "register", userRepo.get(usr.getUsername())));
			return new ModelAndView("redirect:../payment/listAll?legacy=" + person.getLegacy());
		} else {
			addCeitbaEnrollmentIfNecessary(person, usr.getUsername());
			final Enrollment e = new Enrollment(person, service);
			enrollmentRepo.add(e);
			userActionRepo.add(new UserAction(Action.CREATE, Enrollment.class.getName(), null, e.toString(),
					ControllerType.ENROLLMENT, "register", userRepo.get(usr.getUsername())));
			return new ModelAndView("redirect:show?id=" + e.getId() + "&neww=true");
		}
	}
	
	private void addCeitbaEnrollmentIfNecessary(final Person person, final String username) {
		for (final Enrollment e : person.getActiveEnrollments()) {
			if (e.getService().getName().equals("ceitba")) {
				return; // Already enrolled.
			}
		}
		final Service ceitba = serviceRepo.get("ceitba");
		final Enrollment e = new Enrollment(person, ceitba);
		enrollmentRepo.add(e);
		userActionRepo.add(new UserAction(Action.CREATE, Enrollment.class.getName(), null, e.toString(),
				ControllerType.ENROLLMENT, "register", userRepo.get(username)));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView register(final HttpSession session, 
			@RequestParam(value = "service", required = false) final String serviceCategory) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

		final ModelAndView mav = new ModelAndView();
		if ("consumable".equals(serviceCategory)) {
			mav.addObject("services", serviceRepo.getActiveConsumables());
			mav.addObject("consumable", "consumable");
		} else if ("subscribable".equals(serviceCategory)) {
			mav.addObject("services", serviceRepo.getActiveSubscribables());
			mav.addObject("subscribable", "subscribable");
		} else {
			mav.addObject("services", serviceRepo.getActive());
			mav.addObject("all", "all");
		}
		mav.addObject("registerCategory", serviceCategory);
		mav.addObject("registerEnrollmentForm", new RegisterEnrollmentForm());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView delete(HttpSession session, @RequestParam(value = "person", required = true) final Person person,
			@RequestParam(value = "service", required = true) final Service service) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

		String ids = "";
		final List<Enrollment> enrollments;
		if (service.getName().equals("ceitba")) {
			// If it is ceitba, cancel all the enrollments
			enrollments = enrollmentRepo.get(person);
		} else {
			enrollments = enrollmentRepo.get(person, service);
		}
		for (Enrollment e : enrollments) {
			e.cancel(); // cancels all the subscriptions instead of looking for the active one
			ids += e.getId() + "-";
		}

		userActionRepo.add(new UserAction(Action.DELETE, Enrollment.class.getName(), ids, null,
				ControllerType.ENROLLMENT, "delete", userRepo.get(usr.getUsername())));
		return new ModelAndView("redirect:delete");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView delete(HttpSession session) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

		ModelAndView mav = new ModelAndView();
		return mav;
	}
}