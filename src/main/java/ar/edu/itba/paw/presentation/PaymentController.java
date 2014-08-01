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

import ar.edu.itba.paw.domain.payment.CashPayment;
import ar.edu.itba.paw.domain.payment.CashPaymentRepo;
import ar.edu.itba.paw.domain.payment.Debt;
import ar.edu.itba.paw.domain.payment.DebtRepo;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.PersonRepo;
import ar.edu.itba.paw.domain.user.UserAction;
import ar.edu.itba.paw.domain.user.UserAction.Action;
import ar.edu.itba.paw.domain.user.UserAction.ControllerType;
import ar.edu.itba.paw.domain.user.UserActionRepo;
import ar.edu.itba.paw.domain.user.UserRepo;
import ar.edu.itba.paw.presentation.command.CreatePaymentForm;
import ar.edu.itba.paw.presentation.command.validator.CreatePaymentFormValidator;

@Controller
public class PaymentController {

	private CashPaymentRepo paymentRepo;
	private DebtRepo debtRepo;
	private PersonRepo personRepo;
	private CreatePaymentFormValidator validator;
	private UserActionRepo userActionRepo;
	private UserRepo userRepo;

	@Autowired
	public PaymentController(CashPaymentRepo paymentRepo, DebtRepo debtRepo,
			PersonRepo personRepo, CreatePaymentFormValidator validator, UserActionRepo userActionRepo, UserRepo userRepo) {
		super();
		this.paymentRepo = paymentRepo;
		this.debtRepo = debtRepo;
		this.personRepo = personRepo;
		this.validator = validator;
		this.userActionRepo = userActionRepo;
		this.userRepo = userRepo;
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
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

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
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

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
			mav.addObject("debts", debtRepo.getCashedDebts(start, end));
		}
		mav.addObject("createPaymentForm", new CreatePaymentForm());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView pay(HttpSession session, CreatePaymentForm form,
			Errors errors) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

		validator.validate(form, errors);
		if (errors.hasErrors()) {
			return new ModelAndView("redirect:../payment/debts?error=true");
		}

		Debt debt = debtRepo.get(Integer.parseInt(form.getDebtId()));
		CashPayment payment = debt.pay();
		paymentRepo.add(payment);
		userActionRepo.add(new UserAction(Action.CREATE, CashPayment.class.getName(), null, payment.toString(), ControllerType.PAYMENT, "pay", userRepo.get(usr.getUsername())));
		return new ModelAndView("redirect:../payment/listAll");
	}
}
