package ar.edu.itba.paw.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.domain.user.User;
import ar.edu.itba.paw.domain.user.User.Level;
import ar.edu.itba.paw.domain.user.UserAction;
import ar.edu.itba.paw.domain.user.UserAction.Action;
import ar.edu.itba.paw.domain.user.UserAction.ControllerType;
import ar.edu.itba.paw.domain.user.UserActionRepo;
import ar.edu.itba.paw.domain.user.UserRepo;
import ar.edu.itba.paw.presentation.command.LoginForm;
import ar.edu.itba.paw.presentation.command.RegisterUserForm;
import ar.edu.itba.paw.presentation.command.UpdateUserForm;
import ar.edu.itba.paw.presentation.command.validator.LoginFormValidator;
import ar.edu.itba.paw.presentation.command.validator.RegisterUserFormValidator;
import ar.edu.itba.paw.presentation.command.validator.UpdateUserFormValidator;

@Controller
public class UserController {

	private final UserRepo userRepo;
	private final UserActionRepo userActionRepo;
	private final LoginFormValidator loginValidator;
	private final RegisterUserFormValidator registerValidator;
	private final UpdateUserFormValidator updateValidator;

	@Autowired
	public UserController(final UserRepo userRepo, final UserActionRepo userActionRepo, final LoginFormValidator loginValidator, 
			final RegisterUserFormValidator registerValidator, final UpdateUserFormValidator updateValidator) {
		super();
		this.userRepo = userRepo;
		this.userActionRepo = userActionRepo;
		this.loginValidator = loginValidator;
		this.registerValidator = registerValidator;
		this.updateValidator = updateValidator;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView login(final HttpSession session, @RequestParam(value = "error", required = false) final String error) {
		final ModelAndView mav = new ModelAndView();
		mav.addObject("loginForm", new LoginForm());
		mav.addObject("error", error);
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView login(final HttpSession session, final LoginForm form, final Errors errors) {
		final ModelAndView mav = new ModelAndView("redirect:../person/listAll");
		final UserManager userC = new SessionManager(session);
		loginValidator.validate(form, errors);
		if (errors.hasErrors()) {
			return null;
		}
		userC.setUser(form.getUsername());
		mav.addObject("user", userC);
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView logout(final HttpSession session) {
		final UserManager userC = new SessionManager(session);
		if (userC.existsUser()) {
			userC.resetUser(null);
		}
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listAll(final HttpSession session,
			@RequestParam(value = "success", required = false) final Integer messageKey) {
		final String[] messages = { "El usuario ha sido creado correctamente", "El usuario ha sido eliminado correctamente" };
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isAdmin())
			return new ModelAndView("unauthorized");

		final ModelAndView mav = new ModelAndView();
		mav.addObject("users", userRepo.getAll());
		if (messageKey != null && messageKey >= 0 && messageKey < messages.length)
			mav.addObject("successMsg", messages[messageKey]);
		final User loggedUser = userRepo.get(usr.getUsername());
		if (loggedUser.isAdmin()) {
			mav.addObject("admin", "true");
		}
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView update(final HttpSession session,
			@RequestParam(value = "success", required = false) final Integer messageKey,
			@RequestParam(value = "id", required = false) final Integer id) {
		final String[] messages = { "El usuario ha sido actualizado correctamente", };
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		final ModelAndView mav = new ModelAndView();
		final User loggedUser = userRepo.get(usr.getUsername());
		mav.addObject("updateUserForm", new UpdateUserForm());
		mav.addObject("administrator", loggedUser.isAdmin());
		if (id == null || id == loggedUser.getId()) {
			mav.addObject("user", loggedUser);
			mav.addObject("profile", "true");
		} else {
			if (!loggedUser.isAdmin())
				return new ModelAndView("redirect:/unauthorized");
			mav.addObject("user", userRepo.get(id));
		}
		if (messageKey != null && messageKey >= 0 && messageKey < messages.length) {
			mav.addObject("successMsg", messages[messageKey]);
		}
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView update(final HttpSession session, final UpdateUserForm form, final Errors errors) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");

		final User loggedUser = userRepo.get(usr.getUsername());
		form.setAdmin(loggedUser.getLevel().equals(Level.ADMINISTRATOR));

		updateValidator.validate(form, errors);

		final ModelAndView mav = new ModelAndView();
		if (errors.hasErrors()) {
			mav.addObject("administrator", loggedUser.isAdmin());
			return mav;
		}
		
		//If I'm no admin, and I am updating a user other than myself
		if(!loggedUser.isAdmin() && !loggedUser.getUsername().equals(form.getCurrentUsername())) {
			return new ModelAndView("unauthorized");
		}

		final User userBeingUpdated = userRepo.get(form.getCurrentUsername());
		final String previousUser = userBeingUpdated.toString();

		userBeingUpdated.setUsername(form.getUsername());
		//If I'm changing my username, then change it in the UserManager
		if (loggedUser.getUsername().equals(form.getCurrentUsername())) {
			usr.setUser(form.getUsername());
		}

		if (form.getNewPassword() != "") {
			userBeingUpdated.setPassword(form.getNewPassword());
		}

		userActionRepo.add(new UserAction(Action.UPDATE, User.class.getName(), previousUser, userBeingUpdated.toString(),
				ControllerType.USER, "update", loggedUser));
		return new ModelAndView("redirect:./update?success=0&id=" + userBeingUpdated.getId());
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView remove(final HttpSession session, @RequestParam(value = "id", required = true) final String userId) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isAdmin())
			return new ModelAndView("unauthorized");

		final ModelAndView mav = new ModelAndView();
		try {
			final Integer id = Integer.valueOf(userId);
			mav.addObject("user", userRepo.get(id));
			return mav;
		} catch (final Exception e) {
			return new ModelAndView("redirect:/sibadac/notAuthorized");
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView delete(final HttpSession session, @RequestParam(value = "id", required = true) final String userId) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isAdmin())
			return new ModelAndView("unauthorized");

		try {
			final Integer id = Integer.valueOf(userId);
			final User user = userRepo.get(id);
			if (user.isAdmin()) {
				return new ModelAndView("unauthorized");
			}
			userRepo.remove(user);

			userActionRepo.add(new UserAction(Action.DELETE, User.class.getName(), user.toString(), null,
					ControllerType.USER, "delete", userRepo.get(usr.getUsername())));
			return new ModelAndView("redirect:../user/listAll?success=1");
		} catch (final Exception e) {
			return new ModelAndView("unauthorized");
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView register(final HttpSession session) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isAdmin())
			return new ModelAndView("unauthorized");

		final ModelAndView mav = new ModelAndView();
		mav.addObject("registerUserForm", new RegisterUserForm());
		final List<String> levels = new ArrayList<String>();
		levels.add("ADMINISTRATOR");
		levels.add("MODERATOR");
		levels.add("REGULAR");
		mav.addObject("levels", levels);
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView register(final HttpSession session, final RegisterUserForm form, final Errors errors) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isAdmin())
			return new ModelAndView("unauthorized");

		registerValidator.validate(form, errors);
		if (errors.hasErrors()) {
			return new ModelAndView();
		}
		final User newUser = new User(form.getUsername(), form.getPassword());
		if (form.getLevel().equals(User.Level.ADMINISTRATOR.toString())) {
			newUser.setLevel(User.Level.ADMINISTRATOR);
		} else if (form.getLevel().equals(User.Level.MODERATOR.toString())) {
			newUser.setLevel(User.Level.MODERATOR);
		} else {
			newUser.setLevel(User.Level.REGULAR);
		}
		userRepo.add(newUser);
		userActionRepo.add(new UserAction(Action.CREATE, User.class.getName(), null, newUser.toString(),
				ControllerType.USER, "register", userRepo.get(usr.getUsername())));
		return new ModelAndView("redirect:../user/listAll?success=0");
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listActions(final HttpSession session, @RequestParam(value = "user", required = false) final User user, 
			@RequestParam(value = "start", required = false) DateTime start,
			@RequestParam(value = "end", required = false) DateTime end,
			@RequestParam(value = "controller", required = false) String controllerString,
			@RequestParam(value = "action", required = false) String actionString) {
		final UserManager usr = new SessionManager(session);
		if (!usr.existsUser())
			return new ModelAndView("redirect:../user/login?error=unauthorized");
		if(!userRepo.get(usr.getUsername()).isAdmin())
			return new ModelAndView("unauthorized");

		Action action = null;
		if(actionString != null) {
			if(actionString.equals(Action.CREATE.toString()))
				action = Action.CREATE;
			else if(actionString.equals(Action.DELETE.toString()))
				action = Action.DELETE;
			else if(actionString.equals(Action.POST.toString()))
				action = Action.POST;
			else if(actionString.equals(Action.UPDATE.toString()))
				action = Action.UPDATE;
		}
		
		ControllerType controller = null;
		if (controllerString != null) {
			if(controllerString.equals(ControllerType.ASSISTANCE.toString()))
				controller = ControllerType.ASSISTANCE;
			else if(controllerString.equals(ControllerType.BILLING.toString()))
				controller = ControllerType.BILLING;
			else if(controllerString.equals(ControllerType.ENROLLMENT.toString()))
				controller = ControllerType.ENROLLMENT;
			else if(controllerString.equals(ControllerType.PAYMENT.toString()))
				controller = ControllerType.PAYMENT;
			else if(controllerString.equals(ControllerType.PERSON.toString()))
				controller = ControllerType.PERSON;
			else if(controllerString.equals(ControllerType.SERVICE.toString()))
				controller = ControllerType.SERVICE;
			else if(controllerString.equals(ControllerType.USER.toString()))
				controller = ControllerType.USER;
		}
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("users", userRepo.getAll());
		mav.addObject("controllers", ControllerType.values());
		mav.addObject("userActions", Action.values());
		mav.addObject("actions", userActionRepo.getAll(user, action, controller, start, end));
		return mav;
	}
}
