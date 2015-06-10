package ar.edu.itba.paw.presentation;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.domain.enrollment.Enrollment;
import ar.edu.itba.paw.domain.enrollment.EnrollmentRepo;
import ar.edu.itba.paw.domain.payment.Debt;
import ar.edu.itba.paw.domain.payment.DebtRepo;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.service.ServiceRepo;
import ar.edu.itba.paw.domain.user.PersonRepo;
import ar.edu.itba.paw.domain.user.UserAction;
import ar.edu.itba.paw.domain.user.UserAction.Action;
import ar.edu.itba.paw.domain.user.UserAction.ControllerType;
import ar.edu.itba.paw.domain.user.UserActionRepo;
import ar.edu.itba.paw.domain.user.UserRepo;
import ar.edu.itba.paw.lib.DateHelper;

@Controller
public class BillingController {

	private EnrollmentRepo enrollmentRepo;
	private ServiceRepo serviceRepo;
	private DebtRepo debtRepo;
	private PersonRepo personRepo;
	private UserActionRepo userActionRepo;
	private UserRepo userRepo;

	@Autowired
	public BillingController(EnrollmentRepo enrollmentRepo, ServiceRepo serviceRepo, DebtRepo debtRepo,
			PersonRepo personRepo, UserActionRepo userActionRepo, UserRepo userRepo) {
		this.enrollmentRepo = enrollmentRepo;
		this.serviceRepo = serviceRepo;
		this.personRepo = personRepo;
		this.debtRepo = debtRepo;
		this.userActionRepo = userActionRepo;
		this.userRepo = userRepo;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listNewEnrollments(HttpSession session,
			@RequestParam(value = "service_id", required = false) Service service,
			@RequestParam(value = "start", required = false) DateTime start,
			@RequestParam(value = "end", required = false) DateTime end,
			@RequestParam(value = "personnel", required = false, defaultValue = "false") Boolean personnel) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

		if (service == null)
			service = serviceRepo.get("ceitba");

		if (start == null)
			start = DateTime.now().minusMonths(1);
		if (end == null)
			end = DateTime.now();
		ModelAndView mav = new ModelAndView();
		mav.addObject("query", "Inicio: " + DateHelper.getDateString(start) + " Fin:" + DateHelper.getDateString(end)
				+ " Servicio:" + service.getName() + " Personal:" + personnel);
		mav.addObject("services", serviceRepo.getActive());
		mav.addObject("newEnrollments", enrollmentRepo.getBilledNewEnrollments(personnel, service, start, end));
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listCancelledEnrollments(HttpSession session,
			@RequestParam(value = "service_id", required = false) Service service,
			@RequestParam(value = "start", required = false) DateTime start,
			@RequestParam(value = "end", required = false) DateTime end,
			@RequestParam(value = "personnel", required = false, defaultValue = "false") Boolean personnel) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

		if (service == null)
			service = serviceRepo.get("ceitba");

		if (start == null)
			start = DateTime.now().minusMonths(1);
		if (end == null)
			end = DateTime.now();
		ModelAndView mav = new ModelAndView();
		mav.addObject("query", "Inicio: " + DateHelper.getDateString(start) + " Fin:" + DateHelper.getDateString(end)
				+ " Servicio:" + service.getName() + " Personal:" + personnel);
		mav.addObject("services", serviceRepo.getActive());
		mav.addObject("cancelledEnrollments",
				enrollmentRepo.getBilledCancelledEnrollments(personnel, service, start, end));
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listOtherDebts(HttpSession session,
			@RequestParam(value = "start", required = false) DateTime start,
			@RequestParam(value = "end", required = false) DateTime end,
			@RequestParam(value = "personnel", required = false, defaultValue = "false") Boolean personnel) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

		if (start == null)
			start = DateTime.now().minusMonths(1);
		if (end == null)
			end = DateTime.now();
		ModelAndView mav = new ModelAndView();
		mav.addObject("query", "Inicio: " + DateHelper.getDateString(start) + " Fin:" + DateHelper.getDateString(end)
				+ " Personal:" + personnel);
		mav.addObject("debts", debtRepo.getBilledDebts(personnel, start, end));
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView deleteDebts(HttpSession session) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

		List<Debt> debts = debtRepo.removeBilledDebts();
		String ids = "";
		for (Debt debt : debts) {
			ids += debt.getId() + "-";
		}
		userActionRepo.add(new UserAction(Action.POST, Debt.class.getName(), ids, null, ControllerType.BILLING,
				"deleteDebts", userRepo.get(usr.getUsername())));
		return new ModelAndView("redirect:../billing/listOtherDebts");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listEnrolled(HttpSession session,
			@RequestParam(value = "service_id", required = false) Service service,
			@RequestParam(value = "personnel", required = false, defaultValue = "false") Boolean personnel) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

		ModelAndView mav = new ModelAndView();
		mav.addObject("services", serviceRepo.getActive());

//		if (service == null)
//			service = serviceRepo.get("ceitba");
		if (service != null) {
			mav.addObject("query", " Servicio:" + service.getName() + " Personal:" + personnel);
			mav.addObject("enrolled", enrollmentRepo.getBilledActive(service));			
		}

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public void downloadBilling(final HttpServletResponse response, final HttpSession session) throws IOException {
		response.setHeader("Content-disposition","attachment; filename=facturacion.xls");
		final HSSFWorkbook workbook = new HSSFWorkbook();
		createNewEnrollmentsSheet(workbook);
		createCancelledEnrollmentsSheet(workbook);
		createEnrollmentsSheet(workbook);
		
		workbook.write(response.getOutputStream());
	}
	
	private void createNewEnrollmentsSheet(final HSSFWorkbook workbook) {
		final Service service = serviceRepo.get("ceitba");
		final Collection<Enrollment> enrollments = enrollmentRepo.getBilledNewEnrollments(false, service,
				DateTime.now(), DateTime.now().minusMonths(1));
		createSheet(workbook, "Altas", enrollments);
	}

	private void createCancelledEnrollmentsSheet(final HSSFWorkbook workbook) {
		final Service service = serviceRepo.get("ceitba");
		final Collection<Enrollment> enrollments = enrollmentRepo.getBilledCancelledEnrollments(false, service,
				DateTime.now(), DateTime.now().minusMonths(1));
		createSheet(workbook, "Bajas", enrollments);
	}

	private void createEnrollmentsSheet(final HSSFWorkbook workbook) {
		for (final Service service : serviceRepo.getActive()) {
			final Collection<Enrollment> enrollments = enrollmentRepo.getBilledActive(service);
			createSheet(workbook, service.getName(), enrollments);
		}
	}

	private void createSheet(final HSSFWorkbook workbook, final String name, final Collection<Enrollment> enrollments) {
		final HSSFSheet sheet = workbook.createSheet(name);
		//Create a new row in current sheet
		int rowNum = 0;
		final HSSFRow firstRow = sheet.createRow(rowNum);
		firstRow.createCell(0).setCellValue("Legajo");
		firstRow.createCell(1).setCellValue("Nombre");
		firstRow.createCell(2).setCellValue("Apellido");
		firstRow.createCell(3).setCellValue("Inicio");
		firstRow.createCell(4).setCellValue("Fin");

		for (final Enrollment e : enrollments) {
			rowNum++;
			final HSSFRow row = sheet.createRow(rowNum);
			row.createCell(0).setCellValue(e.getPerson().getLegacy());
			row.createCell(1).setCellValue(e.getPerson().getFirstName());
			row.createCell(2).setCellValue(e.getPerson().getLastName());
			row.createCell(3).setCellValue(e.getFormatedStartDate());
			row.createCell(4).setCellValue(e.getFormatedEndDate());
		}
		return;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView billCashPayments(HttpSession session) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

		List<Debt> debts = personRepo.billCashPayments();
		debtRepo.add(debts);
		String ids = "";
		for (Debt debt : debts) {
			ids += debt.getId() + "-";
		}
		userActionRepo.add(new UserAction(Action.POST, Debt.class.getName(), null, ids, ControllerType.BILLING,
				"billCashPayments", userRepo.get(usr.getUsername())));
		return new ModelAndView("redirect:bill");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView bill(HttpSession session) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isModerator())
			return new ModelAndView("unauthorized");

		ModelAndView mav = new ModelAndView();
		return mav;
	}

}