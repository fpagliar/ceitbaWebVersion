package ar.edu.itba.paw.presentation;

import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.domain.assistance.Assistance;
import ar.edu.itba.paw.domain.assistance.AssistanceRepo;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.service.ServiceRepo;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.PersonRepo;
import ar.edu.itba.paw.domain.user.UserRepo;
import ar.edu.itba.paw.lib.DateHelper;
import ar.edu.itba.paw.presentation.command.RegisterAssistanceForm;
import ar.edu.itba.paw.presentation.command.validator.RegisterAssistanceFormValidator;

@Controller
public class AssistanceController {

	private ServiceRepo serviceRepo;
	private PersonRepo personRepo;
	private AssistanceRepo assistanceRepo;
	private RegisterAssistanceFormValidator validator;
	private UserRepo userRepo;

	@Autowired
	public AssistanceController(UserRepo userRepo, ServiceRepo serviceRepo,
			PersonRepo personRepo, AssistanceRepo assistanceRepo,
			RegisterAssistanceFormValidator validator) {
		super();
		this.userRepo = userRepo;
		this.serviceRepo = serviceRepo;
		this.personRepo = personRepo;
		this.validator = validator;
		this.assistanceRepo = assistanceRepo;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listAll(
			HttpSession session,
			@RequestParam(value = "msg", required = false) String successMsg,
			@RequestParam(value = "start_date", required = false) DateTime startDate,
			@RequestParam(value = "end_date", required = false) DateTime endDate,
			@RequestParam(value = "person", required = false) Person person) {

		try{
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		ModelAndView mav = new ModelAndView();

		if (userRepo.get(usr.getUsername()) != null
				&& !userRepo.get(usr.getUsername()).isModerator()
				&& !userRepo.get(usr.getUsername()).isAdmin())
			mav.addObject("basicUser", true);

		mav.addObject("successMsg", successMsg);
		mav.addObject("service", serviceRepo.get("Gimnasio"));
		if (endDate == null)
			endDate = DateHelper.today();
		if (startDate == null)
			startDate = DateHelper.today();

		if (person == null)
			mav.addObject("assistances",
					assistanceRepo.getAll(startDate, endDate));
		else
			mav.addObject("assistances",
					assistanceRepo.getAll(person, startDate, endDate));
		return mav;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView register(HttpSession session,
			RegisterAssistanceForm form, Errors errors) {

		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		ModelAndView mav = new ModelAndView();
		if (userRepo.get(usr.getUsername()) != null
				&& !userRepo.get(usr.getUsername()).isModerator()
				&& !userRepo.get(usr.getUsername()).isAdmin())
			mav.addObject("basicUser", true);

		validator.validate(form, errors);

		Service service = serviceRepo.get(form.getServiceName());
		if (errors.hasErrors()) {
			mav.addObject("service", service);
			mav.addObject("registerAssistanceForm",
					new RegisterAssistanceForm());
			return mav;
		}
		Integer legacy = Integer.parseInt(form.getLegacy());
		Person person = personRepo.getByLegacy(legacy);
		Assistance a = new Assistance(person, service);
		assistanceRepo.add(a);
		return new ModelAndView("redirect:listAll?end_date="
				+ DateHelper.getDateString(DateHelper.today()));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView register(HttpSession session,
			@RequestParam("service_id") Service service) {

		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		ModelAndView mav = new ModelAndView();
		if (userRepo.get(usr.getUsername()) != null
				&& !userRepo.get(usr.getUsername()).isModerator()
				&& !userRepo.get(usr.getUsername()).isAdmin())
			mav.addObject("basicUser", true);

		mav.addObject("service", service);
		mav.addObject("registerAssistanceForm", new RegisterAssistanceForm());
		return mav;
	}
}