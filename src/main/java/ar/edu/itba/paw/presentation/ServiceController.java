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
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.service.ServiceRepo;
import ar.edu.itba.paw.domain.user.UserAction;
import ar.edu.itba.paw.domain.user.UserAction.ControllerType;
import ar.edu.itba.paw.domain.user.UserActionRepo;
import ar.edu.itba.paw.domain.user.UserRepo;
import ar.edu.itba.paw.domain.user.UserAction.Action;
import ar.edu.itba.paw.presentation.command.RegisterServiceForm;
import ar.edu.itba.paw.presentation.command.UpdateServiceForm;
import ar.edu.itba.paw.presentation.command.validator.RegisterServiceFormValidator;
import ar.edu.itba.paw.presentation.command.validator.UpdateServiceFormValidator;

@Controller
public class ServiceController {

	private final ServiceRepo serviceRepo;
	private final UpdateServiceFormValidator updateValidator;
	private final RegisterServiceFormValidator registerValidator;
	private final EnrollmentRepo enrollmentRepo;
	private final UserRepo userRepo;
	private final UserActionRepo userActionRepo;

	@Autowired
	public ServiceController(final UserRepo userRepo, final ServiceRepo serviceRepo, 
			final UpdateServiceFormValidator updateValidator, final RegisterServiceFormValidator registerValidator, 
			final EnrollmentRepo enrollmentRepo, final UserActionRepo userActionRepo) {
		super();
		this.userRepo = userRepo;
		this.serviceRepo = serviceRepo;
		this.updateValidator = updateValidator;
		this.registerValidator = registerValidator;
		this.enrollmentRepo = enrollmentRepo;
		this.userActionRepo = userActionRepo;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listAll(final HttpSession session, @RequestParam(value = "list", required = false) final String value,
			@RequestParam(value = "search", required = false) final String search, 
			@RequestParam(value = "page", required = false, defaultValue = "1") final int page) {

		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser()) {
			return new ModelAndView("redirect:../user/login?error=unauthorized");			
		}

		final ModelAndView mav = new ModelAndView();
		if ("active".equals(value)) {
			mav.addObject("services", serviceRepo.getActive(page));
			mav.addObject("menu", "listActive");
		} else if ("inactive".equals(value)) {
			mav.addObject("services", serviceRepo.getInactive(page));
			mav.addObject("menu", "listInactive");
		} else if (search != null && search != "") {
			mav.addObject("services", serviceRepo.search(search, page));
			mav.addObject("search", true);
			mav.addObject("menu", "listAll");
		} else {
			mav.addObject("services", serviceRepo.getAll(page));
			mav.addObject("menu", "listAll");
		}
		mav.addObject("list", value);
		mav.addObject("searchParam", search);
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView update(@RequestParam("id") final Service service, final HttpSession session,
			@RequestParam(value = "success", required = false) final Boolean success,
			@RequestParam(value = "neww", required = false) final Boolean neww, 
			@RequestParam(value = "page", required = false, defaultValue = "1") final int page) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser()) {
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		}
		if (!userRepo.get(usr.getUsername()).isModerator()) {
			return new ModelAndView("unauthorized");			
		}

		final ModelAndView mav = new ModelAndView();
		mav.addObject("service", service);
		if (neww != null && neww) {
			mav.addObject("neww", true);
			mav.addObject("newmsg", "Servicio creado correctamente");
		}
		if (success == null) {
			mav.addObject("success", false);
		} else {
			mav.addObject("success", success);
			mav.addObject("successmsg", "La informacion se ha modificado correctamente");
		}
		mav.addObject("updateServiceForm", new UpdateServiceForm());
		mav.addObject("enrollments", enrollmentRepo.getActive(service, page));
		mav.addObject("menu", "update");
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView update(final HttpSession session, final UpdateServiceForm form, final Errors errors) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser()) {
			return new ModelAndView("redirect:../user/login?error=unauthorized");			
		}

		if(!userRepo.get(usr.getUsername()).isModerator()) {
			return new ModelAndView("unauthorized");			
		}

		updateValidator.validate(form, errors);

		final ModelAndView mav = new ModelAndView();
		final Service updatedService = serviceRepo.get(form.getId());
		final String previousService = updatedService.toString();

		if (errors.hasErrors()) {
			mav.addObject("service", serviceRepo.get(form.getId()));
			return mav;
		}
		updatedService.setName(form.getName());
		updatedService.setValue(Double.parseDouble(form.getValue()));
		updatedService.setMonthsDuration(Integer.parseInt(form.getMonthsDuration()));
		if (form.getStatus().equals("ACTIVE")) {
			updatedService.activate();
		} else {
			updatedService.deactivate();
		}
		userActionRepo.add(new UserAction(Action.UPDATE, Service.class.getName(), previousService, updatedService.toString(), 
				ControllerType.SERVICE, "update", userRepo.get(usr.getUsername())));
		return new ModelAndView("redirect:update?id=" + updatedService.getId() + "&success=true");
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView register(final HttpSession session, final RegisterServiceForm form, final Errors errors) {

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
		final Service s = new Service(form.getName(), Double.parseDouble(form.getValue()), 
				Integer.parseInt(form.getMonthsDuration()));
		serviceRepo.add(s);

		userActionRepo.add(new UserAction(Action.CREATE, Service.class.getName(), null, s.toString(),
				ControllerType.SERVICE, "register", userRepo.get(usr.getUsername())));
		return new ModelAndView("redirect:update?id=" + s.getId() + "&success=false&neww=true");
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
		mav.addObject("registerServiceForm", new RegisterServiceForm());
		mav.addObject("menu", "newService");
		return mav;
	}
}