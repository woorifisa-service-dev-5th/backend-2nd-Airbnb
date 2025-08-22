package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
		
		// 세션 무효화
		HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        } 
        // get.ContextPath(): 프로젝트 path 가져오기
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
	}
}