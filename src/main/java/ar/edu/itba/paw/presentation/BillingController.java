package ar.edu.itba.paw.presentation;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.domain.enrollment.EnrollmentRepo;
import ar.edu.itba.paw.domain.enrollment.Purchase;
import ar.edu.itba.paw.domain.enrollment.PurchaseRepo;
import ar.edu.itba.paw.domain.payment.Debt;
import ar.edu.itba.paw.domain.payment.DebtRepo;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.service.ServiceRepo;
import ar.edu.itba.paw.domain.user.PersonRepo;
import ar.edu.itba.paw.domain.user.UserAction;
import ar.edu.itba.paw.domain.user.Person.PaymentMethod;
import ar.edu.itba.paw.domain.user.UserAction.Action;
import ar.edu.itba.paw.domain.user.UserAction.ControllerType;
import ar.edu.itba.paw.domain.user.UserActionRepo;
import ar.edu.itba.paw.domain.user.UserRepo;

@Controller
public class BillingController {

	private final EnrollmentRepo enrollmentRepo;
	private final ServiceRepo serviceRepo;
	private final DebtRepo debtRepo;
	private final PersonRepo personRepo;
	private final PurchaseRepo purchaseRepo;
	private final UserActionRepo userActionRepo;
	private final UserRepo userRepo;
	private final ControllerUtils controllerUtils;

	@Autowired
	public BillingController(final EnrollmentRepo enrollmentRepo, final ServiceRepo serviceRepo, final DebtRepo debtRepo,
			final PersonRepo personRepo, final PurchaseRepo purchaseRepo, final UserActionRepo userActionRepo, 
			final UserRepo userRepo, final ControllerUtils controllerUtils) {
		this.enrollmentRepo = enrollmentRepo;
		this.serviceRepo = serviceRepo;
		this.personRepo = personRepo;
		this.debtRepo = debtRepo;
		this.purchaseRepo = purchaseRepo;
		this.userActionRepo = userActionRepo;
		this.userRepo = userRepo;
		this.controllerUtils = controllerUtils;
	}
	
	private ModelAndView checkForModerator(final UserManager user) {
		if (!user.existsUser()) {
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		}
		if (!userRepo.get(user.getUsername()).isModerator()) {
			return new ModelAndView("unauthorized");
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listNewEnrollments(final HttpSession session,
			@RequestParam(value = "service_id", required = false) Service service,
			@RequestParam(value = "start", required = false) DateTime start,
			@RequestParam(value = "end", required = false) DateTime end,
			@RequestParam(value = "personnel", required = false, defaultValue = "false") final Boolean personnel) {
		final UserManager usr = new SessionManager(session);
		if (checkForModerator(usr) != null) {
			return checkForModerator(usr);
		}
		if (service == null) {
			service = serviceRepo.get("ceitba");			
		}
		if (start == null) {
			start = DateTime.now().minusMonths(1);
		}
		if (end == null) {
			end = DateTime.now();
		}
		final ModelAndView mav = new ModelAndView();
		mav.addObject("services", serviceRepo.getActive());
		mav.addObject("newEnrollments", enrollmentRepo.getBilledNewEnrollments(service, start, end));
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listCancelledEnrollments(final HttpSession session,
			@RequestParam(value = "service_id", required = false) Service service,
			@RequestParam(value = "start", required = false) DateTime start,
			@RequestParam(value = "end", required = false) DateTime end) {
		final UserManager usr = new SessionManager(session);
		if (checkForModerator(usr) != null) {
			return checkForModerator(usr);
		}
		
		if (service == null) {
			service = serviceRepo.get("ceitba");
		}
		if (start == null) {
			start = DateTime.now().minusMonths(1);
		}
		if (end == null) {
			end = DateTime.now();
		}

		final ModelAndView mav = new ModelAndView();
		mav.addObject("services", serviceRepo.getActive());
		mav.addObject("cancelledEnrollments", enrollmentRepo.getBilledCancelledEnrollments(service, start, end));
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listEnrolled(final HttpSession session, 
			@RequestParam(value = "service_id", required = false) final Service service) {
		final UserManager usr = new SessionManager(session);
		if (checkForModerator(usr) != null) {
			return checkForModerator(usr);
		}
		final ModelAndView mav = new ModelAndView();
		mav.addObject("services", serviceRepo.getActive());
		if (service != null) {
			mav.addObject("enrolled", enrollmentRepo.getBilledActive(service));			
		}
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public void downloadBilling(final HttpServletResponse response, final HttpSession session) throws IOException {
		response.setHeader("Content-disposition","attachment; filename=facturacion.xls");
		final HSSFWorkbook workbook = new HSSFWorkbook();
		controllerUtils.createNewEnrollmentsSheet(workbook);
		controllerUtils.createCancelledEnrollmentsSheet(workbook);
		controllerUtils.createActiveEnrollmentsSheet(workbook);
		controllerUtils.createPurchaseSheet(workbook);
		workbook.write(response.getOutputStream());
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView pendingProducts(final HttpSession session) {
		final UserManager usr = new SessionManager(session);
		if (checkForModerator(usr) != null) {
			return checkForModerator(usr);
		}
		final ModelAndView mav = new ModelAndView();
		mav.addObject("products", purchaseRepo.getPending());
		return mav;
	}
	
	/**
	 * Informs with success msg.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView bill(final HttpSession session, @RequestParam(value = "msg") final String msg) {
		final UserManager usr = new SessionManager(session);
		if (checkForModerator(usr) != null) {
			return checkForModerator(usr);
		}
		final ModelAndView mav = new ModelAndView();
		mav.addObject("message", msg);
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView pendingPurchases(final HttpSession session) {
		final UserManager usr = new SessionManager(session);
		if (checkForModerator(usr) != null) {
			return checkForModerator(usr);
		}
		final ModelAndView mav = new ModelAndView();
		mav.addObject("billPurchases", purchaseRepo.getPending(PaymentMethod.BILL));
		mav.addObject("cashPurchases", purchaseRepo.getPending(PaymentMethod.CASH));
		return mav;
	}
	
	/**
	 * Clears all the purchases made by people that get charged by bill.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView clearBilledPurchases(final HttpSession session) {
		final UserManager usr = new SessionManager(session);
		if (checkForModerator(usr) != null) {
			return checkForModerator(usr);
		}
		
		String ids = "";
		for (final Purchase p : purchaseRepo.getPending()) {
			if (p.getPerson().getPaymentMethod() == PaymentMethod.BILL) {
				ids += p.getId() + "-";				
				p.bill();
				purchaseRepo.update(p);
			}
		}
		userActionRepo.add(new UserAction(Action.POST, Purchase.class.getName(), null, ids, ControllerType.BILLING,
				"clearBilledPurchases", userRepo.get(usr.getUsername())));
		return new ModelAndView("redirect:bill?msg=Las compras facturadas se han archivado correctamente.");
	}
	
	// Cash Payments
	
	/**
	 * Selects the people who pay in cash, and creates them a debt for each service enrolled.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView billCashPayments(final HttpSession session) {
		final UserManager usr = new SessionManager(session);
		if (checkForModerator(usr) != null) {
			return checkForModerator(usr);
		}

		final List<Debt> debts = personRepo.billCashPayments();
		debtRepo.add(debts);
		String ids = "";
		for (final Debt debt : debts) {
			ids += debt.getId() + "-";
		}
		userActionRepo.add(new UserAction(Action.POST, Debt.class.getName(), null, ids, ControllerType.BILLING,
				"billCashPayments", userRepo.get(usr.getUsername())));
		return new ModelAndView("redirect:bill?msg=Se ha facturado con exito");
	}
	
	/**
	 * Lists all the pending debts, that is, the ones that haven't been payed.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listDebts(final HttpSession session,
			@RequestParam(value = "start", required = false) DateTime start,
			@RequestParam(value = "end", required = false) DateTime end) {
		final UserManager usr = new SessionManager(session);
		if (checkForModerator(usr) != null) {
			return checkForModerator(usr);
		}
		if (start == null) {
			start = DateTime.now().minusMonths(1);
		}
		if (end == null) {
			end = DateTime.now();
		}
		final ModelAndView mav = new ModelAndView();
		mav.addObject("debts", debtRepo.getPendingDebts(start, end));
		return mav;
	}
	
	/**
	 * Deletes a specific debt.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView deleteDebt(final HttpSession session, @RequestParam(value = "debt") final Debt debt) {
		final UserManager usr = new SessionManager(session);
		if (checkForModerator(usr) != null) {
			return checkForModerator(usr);
		}
		debtRepo.delete(debt);
		userActionRepo.add(new UserAction(Action.DELETE, Debt.class.getName(), debt.toString(), null, ControllerType.BILLING,
				"deleteDebt", userRepo.get(usr.getUsername())));
		return new ModelAndView("redirect:bill?msg=Deuda eliminada con exito");
	}
}