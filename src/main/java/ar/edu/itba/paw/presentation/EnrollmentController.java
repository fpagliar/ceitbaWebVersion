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
import ar.edu.itba.paw.domain.user.PersonRepo;
import ar.edu.itba.paw.presentation.command.RegisterEnrollmentForm;
import ar.edu.itba.paw.presentation.command.SearchServiceForm;
import ar.edu.itba.paw.presentation.command.validator.RegisterEnrollmentFormValidator;

@Controller
public class EnrollmentController {

	private EnrollmentRepo enrollmentRepo;
	private ServiceRepo serviceRepo;
	private PersonRepo personRepo;
	private RegisterEnrollmentFormValidator validator;

	@Autowired
	public EnrollmentController(EnrollmentRepo enrollmentRepo,
			ServiceRepo serviceRepo, PersonRepo personRepo,
			RegisterEnrollmentFormValidator validator) {
		super();
		this.enrollmentRepo = enrollmentRepo;
		this.serviceRepo = serviceRepo;
		this.personRepo = personRepo;
		this.validator = validator;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listAll(
			HttpSession session,
			@RequestParam(value = "list", required = false) String value,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "serviceName", required = false) String serviceName) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("courses", serviceRepo.getActiveCourses());
		mav.addObject("sports", serviceRepo.getActiveSports());
		mav.addObject("others", serviceRepo.getActiveOthers());
		mav.addObject("lockers", serviceRepo.getActiveLockers());
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
			mav.addObject("personEnrollments",
					enrollmentRepo.getActivePersonsList(personRepo.search(search)));
			mav.addObject("serviceEnrollments",
					enrollmentRepo.getActiveServiceList(serviceRepo.search(search)));
			mav.addObject("search", true);
		}
		if (value == null && search == null && serviceName == null)
			mav.addObject("enrollments", enrollmentRepo.getActive());
		mav.addObject("searchEnrollmentForm", new SearchServiceForm());
		return mav;
	}

//	@RequestMapping(method = RequestMethod.POST)
//	public ModelAndView listAll(HttpSession session, SearchServiceForm form){
//		return new ModelAndView("redirect:listAll?serviceName=" + form.getService());
//	}
			
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView show(@RequestParam("id") Enrollment enrollment,
			HttpSession session,
			@RequestParam(value = "neww", required = false) Boolean neww) {
		UserManager usr = new SessionManager(session);
		ModelAndView mav = new ModelAndView();
		mav.addObject("enrollment", enrollment);
		if (neww != null && neww) {
			mav.addObject("new", true);
			mav.addObject("newmsg", "Subscripcion creada correctamente");
		}
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView register(HttpSession session,
			RegisterEnrollmentForm form, Errors errors) {
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
		Enrollment e = new Enrollment(personRepo.getByLegacy(legacy),
				serviceRepo.get(form.getServiceName()));
		enrollmentRepo.add(e);
		
		return new ModelAndView("redirect:show?id=" + e.getId() + "&neww=true");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView register(HttpSession session) {
		UserManager usr = new SessionManager(session);
		ModelAndView mav = new ModelAndView();
		mav.addObject("courses", serviceRepo.getActiveCourses());
		mav.addObject("sports", serviceRepo.getActiveSports());
		mav.addObject("others", serviceRepo.getActiveOthers());
		mav.addObject("lockers", serviceRepo.getActiveLockers());
		mav.addObject("registerEnrollmentForm", new RegisterEnrollmentForm());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView delete(HttpSession session,
			@RequestParam(value = "person", required = true) Person person,
			@RequestParam(value = "service", required = true) Service service) {
		UserManager usr = new SessionManager(session);
		for (Enrollment e : enrollmentRepo.get(person, service)) {
			e.cancel(); // cancels all the subscriptions instead of looking for
						// the active one
		}
		return new ModelAndView("redirect:delete");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView delete(HttpSession session) {
		UserManager usr = new SessionManager(session);
		ModelAndView mav = new ModelAndView();
		return mav;
	}
}