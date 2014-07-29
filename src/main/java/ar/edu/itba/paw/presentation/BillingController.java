package ar.edu.itba.paw.presentation;

import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.domain.enrollment.EnrollmentRepo;
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.service.ServiceRepo;

@Controller
public class BillingController {

	private EnrollmentRepo enrollmentRepo;
	private ServiceRepo serviceRepo;

	@Autowired
	public BillingController(EnrollmentRepo enrollmentRepo, ServiceRepo serviceRepo) {
		this.enrollmentRepo = enrollmentRepo;
		this.serviceRepo = serviceRepo;
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

		if(service == null)
			service = serviceRepo.get("ceitba");
		
		if(start == null)
			start = DateTime.now().minusMonths(1);
		if(end == null)
			end = DateTime.now();
		ModelAndView mav = new ModelAndView();
		mav.addObject("personnel", personnel);
		mav.addObject("services", serviceRepo.getActive());
		mav.addObject("newEnrollments", enrollmentRepo.getNewEnrollments(personnel, service, start, end));
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

		if(service == null)
			service = serviceRepo.get("ceitba");
		
		if(start == null)
			start = DateTime.now().minusMonths(1);
		if(end == null)
			end = DateTime.now();
		ModelAndView mav = new ModelAndView();
		mav.addObject("personnel", personnel);
		mav.addObject("services", serviceRepo.getActive());
		mav.addObject("cancelledEnrollments", enrollmentRepo.getCancelledEnrollments(personnel, service, start, end));
		return mav;
	}
		
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listEnrolled(HttpSession session,
			@RequestParam(value = "service_id", required = false) Service service,
			@RequestParam(value = "start", required = false) DateTime start,
			@RequestParam(value = "end", required = false) DateTime end, 
			@RequestParam(value = "personnel", required = false, defaultValue = "false") Boolean personnel) {
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		if(service == null)
			service = serviceRepo.get("ceitba");

		ModelAndView mav = new ModelAndView();
		mav.addObject("personnel", personnel);
		mav.addObject("services", serviceRepo.getActive());
		mav.addObject("enrolled", enrollmentRepo.getActive(service));
		return mav;
	}
}