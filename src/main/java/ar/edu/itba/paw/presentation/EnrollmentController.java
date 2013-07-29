package ar.edu.itba.paw.presentation;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.domain.enrollment.EnrollmentRepo;

@Controller
public class EnrollmentController {

	private EnrollmentRepo enrollmentRepo;
	
	@Autowired
	public EnrollmentController(EnrollmentRepo enrollmentRepo) {
		super();
		this.enrollmentRepo = enrollmentRepo;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listAll(HttpSession session){
		ModelAndView mav = new ModelAndView();
		mav.addObject("enrollments", enrollmentRepo.getActive());
		return mav;
	}
	
}