package ar.edu.itba.paw.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageNotFoundController {

	@RequestMapping
	public ModelAndView pageNotFound() {
		final ModelAndView mav = new ModelAndView();
		mav.setViewName("pageNotFound");
		return mav;
	}
}