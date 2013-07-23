package ar.edu.itba.paw.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import ar.edu.itba.paw.domain.user.UserRepo;
import ar.edu.itba.paw.presentation.SessionManager;
import ar.edu.itba.paw.presentation.UserManager;

@Component
public class TransactionFilter extends OncePerRequestFilter {

	private UserRepo userRepo;
	private SessionFactory sessionFactory;
	
	@Autowired
	public TransactionFilter(SessionFactory sessionFactory, UserRepo userRepo) {
		this.sessionFactory = sessionFactory;
		this.userRepo = userRepo;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {

			// Starting a database transaction
			sessionFactory.getCurrentSession().beginTransaction();
			
			UserManager userMan = new SessionManager(request.getSession());
			if(userMan.existsUser()){
				request.setAttribute("user", userRepo.get(userMan.getName()));
				request.setAttribute("level", userRepo.get(userMan.getName()).getLevel());
			}
//			request.setAttribute("foodTypes", foodTypeRepo.getAll());
			
			// Call the next filter (continue request processing)
			filterChain.doFilter(request, response);

			// Commit the database transaction
			sessionFactory.getCurrentSession().getTransaction().commit();

		} catch (Throwable ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			try {
				if (sessionFactory.getCurrentSession().getTransaction().isActive())
					sessionFactory.getCurrentSession().getTransaction().rollback();
			} catch (Throwable rbEx) {
				rbEx.printStackTrace(pw);
			}
			response.setStatus(500);
			request.getRequestDispatcher("/WEB-INF/jsp/connectionError.jsp").forward(request, response);
		}
	}
}