package servlet.lifecycle;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/lifeCycle.jsp")
public class LifeCycle extends HttpServlet {
	@Override
	public void init() throws ServletException {
		System.out.println("init call!");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("doGet() call!");
	}
	
	@Override
	public void destroy() {
		System.out.println("destory() call!");
	}
}
