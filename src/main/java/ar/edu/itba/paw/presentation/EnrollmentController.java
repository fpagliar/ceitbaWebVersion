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
import ar.edu.itba.paw.domain.payment.Debt;
import ar.edu.itba.paw.domain.payment.DebtRepo;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.service.Service.Type;
import ar.edu.itba.paw.domain.service.ServiceRepo;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.PersonRepo;
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

	@Autowired
	public EnrollmentController(UserRepo userRepo,
			EnrollmentRepo enrollmentRepo, ServiceRepo serviceRepo,
			PersonRepo personRepo, DebtRepo debtRepo, 
			RegisterEnrollmentFormValidator validator) {
		super();
		this.userRepo = userRepo;
		this.enrollmentRepo = enrollmentRepo;
		this.serviceRepo = serviceRepo;
		this.personRepo = personRepo;
		this.debtRepo = debtRepo;
		this.validator = validator;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listAll(
			HttpSession session,
			@RequestParam(value = "list", required = false) String value,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "serviceName", required = false) String serviceName) {

		UserManager usr = new SessionManager(session);
		if (!usr.existsUser() || !userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		ModelAndView mav = new ModelAndView();
		if ("history".equals(value)) {
			mav.addObject("enrollments", enrollmentRepo.getExpired());
			mav.addObject("history", true);
		}
		if (serviceName != null) {
			mav.addObject("enrollments",
					enrollmentRepo.getActive(serviceRepo.get(serviceName)));
			mav.addObject("service", serviceName);
		}
		if (search != null) {
			mav.addObject("personEnrollments", enrollmentRepo
					.getActivePersonsList(personRepo.search(search)));
			mav.addObject("serviceEnrollments", enrollmentRepo
					.getActiveServiceList(serviceRepo.search(search)));
			mav.addObject("search", true);
		}
		if (value == null && search == null && serviceName == null)
			mav.addObject("enrollments", enrollmentRepo.getActive());
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView show(@RequestParam("id") Enrollment enrollment,
			HttpSession session,
			@RequestParam(value = "neww", required = false) Boolean neww) {

		UserManager usr = new SessionManager(session);
		if (!usr.existsUser() || !userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		if(enrollment == null)
			return new ModelAndView("redirect:listAll");
		ModelAndView mav = new ModelAndView();
		mav.addObject("enrollment", enrollment);
		if (neww != null && neww) {
			mav.addObject("new", true);
			mav.addObject("newmsg", "Subscripcion creada correctamente");
		}
		mav.addObject("isActive", enrollment.isActive());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView register(HttpSession session,
			RegisterEnrollmentForm form, Errors errors) {

		UserManager usr = new SessionManager(session);
		if (!usr.existsUser() || !userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		ModelAndView mav = new ModelAndView();
		validator.validate(form, errors);
		if (errors.hasErrors()) {
			mav.addObject("courses", serviceRepo.getActiveCourses());
			mav.addObject("sports", serviceRepo.getActiveSports());
			mav.addObject("others", serviceRepo.getActiveOthers());
			mav.addObject("lockers", serviceRepo.getActiveLockers());
			return mav;
		}
		Integer legacy = Integer.parseInt(form.getLegacy());
		Service service = serviceRepo.get(form.getServiceName());
		Person person = personRepo.getByLegacy(legacy);
		if (service.getType().equals(Type.OTHER)) {
			Debt debt = person.consume(service);
			debtRepo.add(debt);
			return new ModelAndView("redirect:../payment/listAll?legacy=" + person.getLegacy());
		} else {
			Enrollment e = new Enrollment(person,
					service);
			enrollmentRepo.add(e);
			return new ModelAndView("redirect:show?id=" + e.getId() + "&neww=true");
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView register(
			HttpSession session,
			@RequestParam(value = "service", required = false) String serviceCategory) {

		UserManager usr = new SessionManager(session);
		if (!usr.existsUser() || !userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		ModelAndView mav = new ModelAndView();
		if ("sport".equals(serviceCategory)) {
			mav.addObject("services", serviceRepo.getActiveSports());
			mav.addObject("sport", "sport");
		} else if ("course".equals(serviceCategory)) {
			mav.addObject("services", serviceRepo.getActiveCourses());
			mav.addObject("course", "course");
		} else if ("other".equals(serviceCategory)) {
			mav.addObject("services", serviceRepo.getActiveOthers());
			mav.addObject("other", "other");
		} else if ("common".equals(serviceCategory)) {
			mav.addObject("services", serviceRepo.getActiveCommons());
			mav.addObject("common", "common");
		} else {
			mav.addObject("services", serviceRepo.getActive());
			mav.addObject("all", "all");
		}
		mav.addObject("registerCategory", serviceCategory);
		mav.addObject("registerEnrollmentForm", new RegisterEnrollmentForm());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView delete(HttpSession session,
			@RequestParam(value = "person", required = true) Person person,
			@RequestParam(value = "service", required = true) Service service) {

		UserManager usr = new SessionManager(session);
		if (!usr.existsUser() || !userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		for (Enrollment e : enrollmentRepo.get(person, service)) {
			e.cancel(); // cancels all the subscriptions instead of looking for
						// the active one
		}
		return new ModelAndView("redirect:delete");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView delete(HttpSession session) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser() || !userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		ModelAndView mav = new ModelAndView();
		return mav;
	}
}