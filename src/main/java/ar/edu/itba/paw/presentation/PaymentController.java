package ar.edu.itba.paw.presentation;

import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.domain.assistance.AssistanceRepo;
import ar.edu.itba.paw.domain.payment.CashPaymentRepo;
import ar.edu.itba.paw.domain.service.ServiceRepo;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.PersonRepo;
import ar.edu.itba.paw.domain.user.UserRepo;
import ar.edu.itba.paw.presentation.command.validator.RegisterAssistanceFormValidator;

@Controller
public class PaymentController {

//	private ServiceRepo serviceRepo;
//	private PersonRepo personRepo;
//	private AssistanceRepo assistanceRepo;
//	private RegisterAssistanceFormValidator validator;
//	private UserRepo userRepo;
	private CashPaymentRepo paymentRepo;

	@Autowired
	public PaymentController(UserRepo userRepo, ServiceRepo serviceRepo,
			PersonRepo personRepo, AssistanceRepo assistanceRepo,
			RegisterAssistanceFormValidator validator, CashPaymentRepo paymentRepo) {
		super();
//		this.userRepo = userRepo;
//		this.serviceRepo = serviceRepo;
//		this.personRepo = personRepo;
//		this.validator = validator;
//		this.assistanceRepo = assistanceRepo;
		this.paymentRepo = paymentRepo;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listAll(HttpSession session, 
			@RequestParam(value = "person_id", required = false) Person person, 
			@RequestParam(value = "start", required = false) DateTime start, 
			@RequestParam(value = "end", required = false) DateTime end) {

		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		ModelAndView mav = new ModelAndView();
		mav.addObject("payments", paymentRepo.getAll(person, start, end));
		return mav;
	}
}
