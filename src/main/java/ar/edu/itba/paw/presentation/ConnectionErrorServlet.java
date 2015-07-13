package ar.edu.itba.paw.presentation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ConnectionErrorServlet extends HttpServlet{

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setStatus(500);
		req.getRequestDispatcher("/WEB-INF/jsp/connectionError.jsp").forward(req, resp);
	}

}
