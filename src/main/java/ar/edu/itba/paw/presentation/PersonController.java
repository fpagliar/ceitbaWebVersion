package ar.edu.itba.paw.presentation;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.PersonRepo;
import ar.edu.itba.paw.presentation.command.LoginForm;
import ar.edu.itba.paw.presentation.command.validator.RegisterPersonFormValidator;

@Controller
public class PersonController {

	private RegisterPersonFormValidator validator;
	private PersonRepo personRepo;
	
	@Autowired
	public PersonController(RegisterPersonFormValidator validator, PersonRepo personRepo) {
		super();
		this.validator = validator;
		this.personRepo = personRepo;
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
		mav.addObject("persons", personRepo.getAll());
		System.out.println("persons:");
		for(Person p: personRepo.getAll()){
			System.out.println(p.getFirstName() + " " + p.getLastName());
		}
		return mav;
	}
}