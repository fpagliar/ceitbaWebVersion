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
import ar.edu.itba.paw.domain.service.Service;
import ar.edu.itba.paw.domain.service.ServiceRepo;
import ar.edu.itba.paw.presentation.command.RegisterServiceForm;
import ar.edu.itba.paw.presentation.command.UpdateServiceForm;
import ar.edu.itba.paw.presentation.command.validator.RegisterServiceFormValidator;
import ar.edu.itba.paw.presentation.command.validator.UpdateServiceFormValidator;

@Controller
public class ServiceController {

	private ServiceRepo serviceRepo;
	private UpdateServiceFormValidator updateValidator;
	private RegisterServiceFormValidator registerValidator;
	private EnrollmentRepo enrollmentRepo;
	
	@Autowired
	public ServiceController(ServiceRepo serviceRepo, UpdateServiceFormValidator updateValidator, 
			RegisterServiceFormValidator registerValidator, EnrollmentRepo enrollmentRepo) {
		super();
		this.serviceRepo = serviceRepo;
		this.updateValidator = updateValidator;
		this.registerValidator = registerValidator;
		this.enrollmentRepo = enrollmentRepo;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listAll(HttpSession session,
			@RequestParam(value = "list", required = false) String value,
			@RequestParam(value = "search", required = false) String search) {
		ModelAndView mav = new ModelAndView();
		if("active".equals(value))
			mav.addObject("services", serviceRepo.getActive());
		else if("inactive".equals(value))
			mav.addObject("services", serviceRepo.getInactive());
		else if("SPORT".equals(value))
			mav.addObject("services", serviceRepo.getSports());
		else if("COURSE".equals(value))
			mav.addObject("services", serviceRepo.getCourses());
		else if("OTHER".equals(value))
			mav.addObject("services", serviceRepo.getOthers());
		else if("LOCKER".equals(value))
			mav.addObject("services", serviceRepo.getLockers());
		else if(search != null){
			mav.addObject("services", serviceRepo.search(search));
			mav.addObject("search", true);
		}else
			mav.addObject("services", serviceRepo.getAll());
		mav.addObject("list", value);
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView update(@RequestParam("id") Service service,
			HttpSession session,
			@RequestParam(value = "success", required = false) Boolean success,
			@RequestParam(value = "neww", required = false) Boolean neww) {
		UserManager usr = new SessionManager(session);
		ModelAndView mav = new ModelAndView();
		mav.addObject("service", service);
		if(neww != null && neww){
			mav.addObject("new", true);
			mav.addObject("newmsg", "Servicio creado correctamente");
		}
		if(success == null){
			mav.addObject("success", false);
		}else{
			mav.addObject("success", success);
			mav.addObject("successmsg", "La informacion se ha modificado correctamente");
		}
		mav.addObject("updateServiceForm", new UpdateServiceForm());
		mav.addObject("enrollments", enrollmentRepo.getActive(service));
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView update(HttpSession session, UpdateServiceForm form,
			Errors errors) {
		ModelAndView mav = new ModelAndView();
		updateValidator.validate(form, errors);
		Service updatedService = serviceRepo.get(form.getId());
		if (errors.hasErrors()) {
			mav.addObject("service", serviceRepo.get(form.getId()));
			return mav;
		}
		updatedService.setName(form.getName());
		updatedService.setValue(Double.parseDouble(form.getValue()));
		updatedService.setMonthsDuration(Integer.parseInt(form.getMonthsDuration()));
		if(form.getStatus().equals("ACTIVE"))
			updatedService.setStatus(Service.Status.ACTIVE);
		else
			updatedService.setStatus(Service.Status.INACTIVE);
		if(form.getType().equals("SPORT"))
			updatedService.setType(Service.Type.SPORT);
		else if(form.getType().equals("COURSE"))
			updatedService.setType(Service.Type.COURSE);
		else if(form.getType().equals("LOCKER"))
			updatedService.setType(Service.Type.LOCKER);
		else
			updatedService.setType(Service.Type.OTHER);
		return new ModelAndView("redirect:update?id=" + updatedService.getId()
				+ "&success=true");
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView register(HttpSession session, RegisterServiceForm form,
			Errors errors) {
		ModelAndView mav = new ModelAndView();
		registerValidator.validate(form, errors);
		if (errors.hasErrors()) {
			return null;
		}
		Service.Type type = null;
		if(form.getType().equals("COURSE"))
			type = Service.Type.COURSE;
		if(form.getType().equals("LOCKER"))
			type = Service.Type.LOCKER;
		if(form.getType().equals("OTHER"))
			type = Service.Type.OTHER;
		if(form.getType().equals("SPORT"))
			type = Service.Type.SPORT;
		Service s = new Service(form.getName(),	Double.parseDouble(form.getValue()), type, Integer.parseInt(form.getMonthsDuration()));
		serviceRepo.add(s);
		return new ModelAndView("redirect:update?id=" + s.getId()
				+ "&success=false&neww=true");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView register(HttpSession session) {
		UserManager usr = new SessionManager(session);
		ModelAndView mav = new ModelAndView();
		mav.addObject("registerServiceForm", new RegisterServiceForm());
		return mav;
	}
}