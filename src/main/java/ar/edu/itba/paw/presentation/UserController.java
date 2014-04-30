package ar.edu.itba.paw.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.domain.user.User;
import ar.edu.itba.paw.domain.user.UserRepo;
import ar.edu.itba.paw.presentation.command.LoginForm;
import ar.edu.itba.paw.presentation.command.RegisterUserForm;
import ar.edu.itba.paw.presentation.command.UpdateUserForm;
import ar.edu.itba.paw.presentation.command.validator.LoginFormValidator;
import ar.edu.itba.paw.presentation.command.validator.RegisterUserFormValidator;
import ar.edu.itba.paw.presentation.command.validator.UpdateUserFormValidator;

@Controller
public class UserController {
	private LoginFormValidator loginValidator;
	private UserRepo userRepo;
	RegisterUserFormValidator registerValidator;
	UpdateUserFormValidator updateValidator;

	@Autowired
	public UserController(LoginFormValidator loginValidator, RegisterUserFormValidator registerValidator, UserRepo userRepo, UpdateUserFormValidator updateValidator) {
		super();
		this.loginValidator = loginValidator;
		this.userRepo = userRepo;
		this.registerValidator = registerValidator;
		this.updateValidator = updateValidator;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView login(HttpSession session, @RequestParam(value = "error", required = false) String error) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("loginForm", new LoginForm());
		mav.addObject("error", error);
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView login(HttpSession session, LoginForm form, Errors errors) {
		ModelAndView mav = new ModelAndView("redirect:../person/listAll");
		UserManager userC = new SessionManager(session);
		loginValidator.validate(form, errors);
		if (errors.hasErrors()) {
			return null;
		}
		userC.setUser(form.getUsername());
		mav.addObject("user", userC);

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView logout(HttpSession session) {
		UserManager userC = new SessionManager(session);
		if (userC.existsUser()) {
			userC.resetUser(null);
		}
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listAll(HttpSession session, @RequestParam(value = "success", required = false) Integer messageKey) {
		String[] messages = { "El usuario ha sido creado correctamente",
							  "El usuario ha sido eliminado correctamente" };
		UserManager usr = new SessionManager(session);
		if (! usr.existsUser() || ! userRepo.get(usr.getUsername()).isAdmin())
			return new ModelAndView("redirect:/sibadac/notAuthorized");
		ModelAndView mav = new ModelAndView();
		mav.addObject("users", userRepo.getAll());
		if(messageKey != null && messageKey >= 0 && messageKey < messages.length)
			mav.addObject("successMsg", messages[messageKey]);
		User loggedUser = userRepo.get(usr.getUsername());
		if (loggedUser.isAdmin())
			mav.addObject("admin", "true");
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView update(HttpSession session, @RequestParam(value = "success", required = false) Integer messageKey,
			@RequestParam(value = "id", required = false) Integer id) {
		String[] messages = { "El usuario ha sido actualizado correctamente",};
		UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		ModelAndView mav = new ModelAndView();
		User loggedUser = userRepo.get(usr.getUsername());
		mav.addObject("updateUserForm", new UpdateUserForm());
		if(id == null){
			mav.addObject("user", loggedUser);
			mav.addObject("profile", "true");
		}else{
			if (!loggedUser.isAdmin())
				return new ModelAndView("redirect:../user/login?error=unauthorized");
			mav.addObject("user", userRepo.get(id));
		}
		if(messageKey != null && messageKey >= 0 && messageKey < messages.length)
			mav.addObject("successMsg", messages[messageKey]);
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView update(HttpSession session, UpdateUserForm form,
			Errors errors) {
		UserManager usr = new SessionManager(session);
		if(!usr.existsUser()){
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		}
		updateValidator.validate(form, errors);

		ModelAndView mav = new ModelAndView();
		if (errors.hasErrors()) {
			return mav;
		}

		User actual = userRepo.get(usr.getUsername());
		actual.setUsername(form.getUsername());
		
		usr.setUser(form.getUsername());

		if(form.getNewPassword() != ""){
			actual.setPassword(form.getNewPassword());
		}
		return new ModelAndView("redirect:./update?success=0");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView remove(HttpSession session, @RequestParam(value = "id", required = true) String userId) {
		UserManager usr = new SessionManager(session);
		if (! usr.existsUser() || ! userRepo.get(usr.getUsername()).isAdmin())
			return new ModelAndView("redirect:/sibadac/notAuthorized");

		ModelAndView mav = new ModelAndView();
		try{
			Integer id = Integer.valueOf(userId);
			mav.addObject("user", userRepo.get(id));
			return mav;
		}catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("redirect:/sibadac/notAuthorized");
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView delete(HttpSession session, @RequestParam(value = "id", required = true) String userId) {
		UserManager usr = new SessionManager(session);
		if (! usr.existsUser() || ! userRepo.get(usr.getUsername()).isAdmin())
			return new ModelAndView("redirect:/sibadac/notAuthorized");

		try{
			Integer id = Integer.valueOf(userId);
			userRepo.remove(userRepo.get(id));			
			return new ModelAndView("redirect:../user/listAll?success=1");
		}catch (Exception e) {
			return new ModelAndView("redirect:/sibadac/notAuthorized");
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView register(HttpSession session) {
		UserManager usr = new SessionManager(session);
		if (! usr.existsUser() || ! userRepo.get(usr.getUsername()).isAdmin())
			return new ModelAndView("redirect:/sibadac/notAuthorized");

		ModelAndView mav = new ModelAndView();
		mav.addObject("registerUserForm", new RegisterUserForm());
		List<String> levels = new ArrayList<String>();
		levels.add("ADMINISTRATOR");
		levels.add("MODERATOR");
		levels.add("REGULAR");
		mav.addObject("levels", levels);
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView register(HttpSession session, RegisterUserForm form, Errors errors) {
		UserManager usr = new SessionManager(session);
		if (! usr.existsUser() || ! userRepo.get(usr.getUsername()).isAdmin())
			return new ModelAndView("redirect:/sibadac/notAuthorized");

		registerValidator.validate(form, errors);
		if (errors.hasErrors()) {
			return new ModelAndView();
		}
		User newUser = new User(form.getUsername(), form.getPassword());
		if (form.getLevel().equals(User.Level.ADMINISTRATOR.toString()))
			newUser.setLevel(User.Level.ADMINISTRATOR);
		else if (form.getLevel().equals(User.Level.MODERATOR.toString()))
			newUser.setLevel(User.Level.MODERATOR);
		else 
			newUser.setLevel(User.Level.REGULAR);
		userRepo.add(newUser);
		return new ModelAndView("redirect:../user/listAll?success=0");
	}
}
