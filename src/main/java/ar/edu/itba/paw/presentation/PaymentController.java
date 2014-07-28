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

import ar.edu.itba.paw.domain.assistance.AssistanceRepo;
import ar.edu.itba.paw.domain.payment.CashPaymentRepo;
import ar.edu.itba.paw.domain.payment.Debt;
import ar.edu.itba.paw.domain.payment.DebtRepo;
import ar.edu.itba.paw.domain.service.ServiceRepo;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.PersonRepo;
import ar.edu.itba.paw.domain.user.UserRepo;
import ar.edu.itba.paw.presentation.command.CreatePaymentForm;
import ar.edu.itba.paw.presentation.command.RegisterEnrollmentForm;
import ar.edu.itba.paw.presentation.command.validator.CreatePaymentFormValidator;
import ar.edu.itba.paw.presentation.command.validator.RegisterAssistanceFormValidator;

@Controller
public class PaymentController {

	private CashPaymentRepo paymentRepo;
	private DebtRepo debtRepo;
	private PersonRepo personRepo;
	private CreatePaymentFormValidator validator;

	@Autowired
	public PaymentController(CashPaymentRepo paymentRepo, DebtRepo debtRepo,
			PersonRepo personRepo, CreatePaymentFormValidator validator) {
		super();
		this.paymentRepo = paymentRepo;
		this.debtRepo = debtRepo;
		this.personRepo = personRepo;
		this.validator = validator;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listAll(
			HttpSession session,
			@RequestParam(value = "legacy", required = false) String legacy,
			@RequestParam(value = "start", required = false) DateTime start,
			@RequestParam(value = "end", required = false) DateTime end,
			@RequestParam(value = "error", required = false) String error) {

		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		ModelAndView mav = new ModelAndView();
		if (legacy != null) {
			try {
				int leg = Integer.parseInt(legacy);
				Person person = personRepo.getByLegacy(leg);
				if (person == null)
					mav.addObject("error", "El legajo no corresponde a ningun alumno");
				else
					mav.addObject("payments",
							paymentRepo.getAll(person, start, end));
			} catch (Exception e) {
				mav.addObject("error", "Legajo invalido");
			}
		} else {
			mav.addObject("payments", paymentRepo.getAll(start, end));
		}
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView debts(
			HttpSession session,
			@RequestParam(value = "legacy", required = false) String legacy,
			@RequestParam(value = "start", required = false) DateTime start,
			@RequestParam(value = "end", required = false) DateTime end,
			@RequestParam(value = "error", required = false) String error) {

		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		ModelAndView mav = new ModelAndView();

		if (legacy != null) {
			try {
				int leg = Integer.parseInt(legacy);
				Person person = personRepo.getByLegacy(leg);
				if (person == null)
					mav.addObject("error", "El legajo no corresponde a ningun alumno");
				else
					mav.addObject("debts",
							debtRepo.get(person, start, end));
			} catch (Exception e) {
				mav.addObject("error", "Legajo invalido");
			}
		} else {
			mav.addObject("debts", debtRepo.get(start, end));
		}
//		mav.addObject("debts", debtRepo.get(person, start, end));
		// mav.addObject("debts", debtRepo.getAll());
		mav.addObject("createPaymentForm", new CreatePaymentForm());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView pay(HttpSession session, CreatePaymentForm form,
			Errors errors) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		validator.validate(form, errors);
		if (errors.hasErrors()) {
			return new ModelAndView("redirect:../payment/debts?error=true");
		}

		Debt debt = debtRepo.get(Integer.parseInt(form.getDebtId()));
		debt.pay();
		return new ModelAndView("redirect:../payment/listAll");
	}
}
