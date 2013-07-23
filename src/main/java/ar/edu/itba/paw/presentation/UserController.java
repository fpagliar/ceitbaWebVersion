package ar.edu.itba.paw.presentation;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.domain.user.UserRepo;
import ar.edu.itba.paw.presentation.command.LoginForm;
import ar.edu.itba.paw.presentation.command.validator.LoginFormValidator;

@Controller
public class UserController {
	private LoginFormValidator loginValidator;
	private UserRepo userRepo;
	
	@Autowired
	public UserController(LoginFormValidator loginValidator, UserRepo userRepo) {
		super();
		this.loginValidator = loginValidator;
		this.userRepo = userRepo;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView login(HttpSession session) {
		ModelAndView mav;
		mav = new ModelAndView();
		mav.addObject("loginForm", new LoginForm());
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listAll(HttpSession session) {
				ModelAndView mav = new ModelAndView();
		mav.addObject("users", userRepo.getAll());
		return mav;
	}
}
