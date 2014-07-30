package ar.edu.itba.paw.presentation;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.domain.enrollment.EnrollmentRepo;
import ar.edu.itba.paw.domain.payment.Debt;
import ar.edu.itba.paw.domain.payment.DebtRepo;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.service.ServiceRepo;
import ar.edu.itba.paw.domain.user.PersonRepo;
import ar.edu.itba.paw.lib.DateHelper;

@Controller
public class BillingController {

	private EnrollmentRepo enrollmentRepo;
	private ServiceRepo serviceRepo;
	private DebtRepo debtRepo;
	private PersonRepo personRepo;

	@Autowired
	public BillingController(EnrollmentRepo enrollmentRepo,
			ServiceRepo serviceRepo, DebtRepo debtRepo, PersonRepo personRepo) {
		this.enrollmentRepo = enrollmentRepo;
		this.serviceRepo = serviceRepo;
		this.personRepo = personRepo;
		this.debtRepo = debtRepo;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listNewEnrollments(
			HttpSession session,
			@RequestParam(value = "service_id", required = false) Service service,
			@RequestParam(value = "start", required = false) DateTime start,
			@RequestParam(value = "end", required = false) DateTime end,
			@RequestParam(value = "personnel", required = false, defaultValue = "false") Boolean personnel) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		if (service == null)
			service = serviceRepo.get("ceitba");

		if (start == null)
			start = DateTime.now().minusMonths(1);
		if (end == null)
			end = DateTime.now();
		ModelAndView mav = new ModelAndView();
		mav.addObject("query", "Inicio: " + DateHelper.getDateString(start)
				+ " Fin:" + DateHelper.getDateString(end) + " Servicio:"
				+ service.getName() + " Personal:" + personnel);
		mav.addObject("services", serviceRepo.getActive());
		mav.addObject("newEnrollments", enrollmentRepo.getBilledNewEnrollments(
				personnel, service, start, end));
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listCancelledEnrollments(
			HttpSession session,
			@RequestParam(value = "service_id", required = false) Service service,
			@RequestParam(value = "start", required = false) DateTime start,
			@RequestParam(value = "end", required = false) DateTime end,
			@RequestParam(value = "personnel", required = false, defaultValue = "false") Boolean personnel) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		if (service == null)
			service = serviceRepo.get("ceitba");

		if (start == null)
			start = DateTime.now().minusMonths(1);
		if (end == null)
			end = DateTime.now();
		ModelAndView mav = new ModelAndView();
		mav.addObject("query", "Inicio: " + DateHelper.getDateString(start)
				+ " Fin:" + DateHelper.getDateString(end) + " Servicio:"
				+ service.getName() + " Personal:" + personnel);
		mav.addObject("services", serviceRepo.getActive());
		mav.addObject("cancelledEnrollments", enrollmentRepo
				.getBilledCancelledEnrollments(personnel, service, start, end));
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listOtherDebts(
			HttpSession session,
			@RequestParam(value = "start", required = false) DateTime start,
			@RequestParam(value = "end", required = false) DateTime end,
			@RequestParam(value = "personnel", required = false, defaultValue = "false") Boolean personnel) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		if (start == null)
			start = DateTime.now().minusMonths(1);
		if (end == null)
			end = DateTime.now();
		ModelAndView mav = new ModelAndView();
		mav.addObject("query", "Inicio: " + DateHelper.getDateString(start)
				+ " Fin:" + DateHelper.getDateString(end) + " Personal:" + personnel);
		mav.addObject("debts", debtRepo.getBilledDebts(personnel, start, end));
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView deleteDebts(HttpSession session) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		debtRepo.removeBilledDebts();
		return new ModelAndView("redirect:../billing/listOtherDebts");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listEnrolled(
			HttpSession session,
			@RequestParam(value = "service_id", required = false) Service service,
			@RequestParam(value = "personnel", required = false, defaultValue = "false") Boolean personnel) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		if (service == null)
			service = serviceRepo.get("ceitba");

		ModelAndView mav = new ModelAndView();
		mav.addObject("query", " Servicio:" + service.getName() + " Personal:"
				+ personnel);
		mav.addObject("services", serviceRepo.getActive());
		mav.addObject("enrolled", enrollmentRepo.getBilledActive(service));
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView billCashPayments(HttpSession session) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		List<Debt> debts = personRepo.billCashPayments();
		debtRepo.add(debts);
		return new ModelAndView("redirect:bill");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView bill(HttpSession session) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		ModelAndView mav = new ModelAndView();
		return mav;
	}

}