package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.DTO.UserSessionDto;
import service.LoginService;
import service.impl.LoginServiceImpl;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
	
	// 의존성 주입
	private static LoginService service = LoginServiceImpl.getInstance();
	
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/views/users/login.jsp")
        .forward(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 사용자가 입력한 아이디, 비밀번호 추출
		String inputId = req.getParameter("email");
		String inputPw = req.getParameter("password");
		
		logger.info("로그인 요청: email={}, pw={}", inputId, inputPw);
		
		// TODO: 추후 DB에서 조회, 공백값 검증 등
		UserSessionDto user = service.findUser(inputId, inputPw);
		
		if (user == null) {
		    // 로그인 실패
		    req.setAttribute("loginError", "이메일 또는 비밀번호가 올바르지 않습니다.");
		    req.getRequestDispatcher("/WEB-INF/views/users/login.jsp").forward(req, resp);
		    return;
		}
		
		logger.info(user.toString());

		// 로그인 성공
		HttpSession session = req.getSession();
		session.setAttribute("user", user);

		resp.sendRedirect(req.getContextPath() + "/index.jsp");
	}
}