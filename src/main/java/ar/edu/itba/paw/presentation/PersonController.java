package ar.edu.itba.paw.presentation;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.domain.enrollment.EnrollmentRepo;
import ar.edu.itba.paw.domain.user.Person;
import ar.edu.itba.paw.domain.user.PersonRepo;
import ar.edu.itba.paw.presentation.command.RegisterPersonForm;
import ar.edu.itba.paw.presentation.command.UpdatePersonForm;
import ar.edu.itba.paw.presentation.command.validator.RegisterPersonFormValidator;
import ar.edu.itba.paw.presentation.command.validator.UpdatePersonFormValidator;

@Controller
public class PersonController {

	private RegisterPersonFormValidator registerValidator;
	private UpdatePersonFormValidator updateValidator;
	private PersonRepo personRepo;
	private EnrollmentRepo enrollmentRepo;

	@Autowired
	public PersonController(RegisterPersonFormValidator validator,
			UpdatePersonFormValidator updateValidator, PersonRepo personRepo, EnrollmentRepo enrollmentRepo) {
		super();
		this.registerValidator = validator;
		this.personRepo = personRepo;
		this.updateValidator = updateValidator;
		this.enrollmentRepo = enrollmentRepo;
	}

//	@RequestMapping(method = RequestMethod.GET)
//	public ModelAndView login(HttpSession session) {
//		ModelAndView mav;
//		mav = new ModelAndView();
//		mav.addObject("loginForm", new LoginForm());
//		return mav;
//	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listAll(HttpSession session,
		@RequestParam(value = "search", required = false) String search){
		ModelAndView mav = new ModelAndView();
		if(search != null){
			mav.addObject("persons", personRepo.search(search));
			mav.addObject("search", true);
		}else
			mav.addObject("persons", personRepo.getAll());
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView update(@RequestParam("id") Person person,
			HttpSession session,
			@RequestParam(value = "success", required = false) Boolean success,
			@RequestParam(value = "neww", required = false) Boolean neww) {
		UserManager usr = new SessionManager(session);
		ModelAndView mav = new ModelAndView();
		mav.addObject("person", person);
		if(neww != null && neww){
			mav.addObject("new", true);
			mav.addObject("newmsg", "Usuario creado correctamente");
		}
		if(success == null){
			mav.addObject("success", false);
		}else{
			mav.addObject("success", success);
			mav.addObject("successmsg", "La informacion se ha modificado correctamente");
		}
		mav.addObject("updatePersonForm", new UpdatePersonForm());
		mav.addObject("enrollments", enrollmentRepo.getActive(person));
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView update(HttpSession session, UpdatePersonForm form,
			Errors errors) {
		ModelAndView mav = new ModelAndView();
		updateValidator.validate(form, errors);
		Person updatedPerson = personRepo.getById(form.getId());
		if (errors.hasErrors()) {
			return null;
		}
		updatedPerson.setLegacy(Integer.parseInt(form.getLegacy()));
		updatedPerson.setFirstName(form.getFirstName());
		updatedPerson.setLastName(form.getLastName());
		updatedPerson.setEmail(form.getEmail());
		updatedPerson.setPhone(form.getPhone());
		updatedPerson.setCellphone(form.getCellphone());
		updatedPerson.setDni(form.getDni());
		updatedPerson.setEmail2(form.getEmail2());
		return new ModelAndView("redirect:update?id=" + updatedPerson.getId()
				+ "&success=true");
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView register(HttpSession session, RegisterPersonForm form,
			Errors errors) {
		ModelAndView mav = new ModelAndView();
		registerValidator.validate(form, errors);
		if (errors.hasErrors()) {
			return null;
		}
		Person p = new Person(form.getFirstName(), form.getLastName(), Integer.valueOf(form.getLegacy()));
		p.setCellphone(form.getCellphone());
		p.setDni(form.getDni());
		p.setEmail(form.getEmail());
		p.setEmail2(form.getEmail2());
		p.setPhone(form.getPhone());
		personRepo.add(p);
		return new ModelAndView("redirect:update?id=" + p.getId()
				+ "&success=false&neww=true");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView register(HttpSession session) {
		UserManager usr = new SessionManager(session);
		ModelAndView mav = new ModelAndView();
		mav.addObject("registerPersonForm", new RegisterPersonForm());
		return mav;
	}
}