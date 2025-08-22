package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.TeamUserDao;
import model.User;
import service.TeamUserService;
import service.impl.TeamUserServiceImpl;

@WebServlet("/members")
public class TeamMemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TeamUserService teamUserService;

	@Override
    public void init() throws ServletException {
        TeamUserDao teamUserDao = new TeamUserDao();
        this.teamUserService = new TeamUserServiceImpl(teamUserDao);
    }
	
	// 팀 멤버 조회
	// GET /members?team={teamId}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String team = request.getParameter("team");
		
		int teamId = Integer.parseInt(team);
		List<User> users = teamUserService.getTeamMembers(teamId);
		
		request.setAttribute("team", teamId);
		request.setAttribute("users", users);
		
		request.getRequestDispatcher("/WEB-INF/views/teams/members.jsp").forward(request, response);
	}
	
	
	// 팀에 멤버 추가
	// 팀 멤버 추가 / 삭제
	// POST /members?team={teamId}&user={userEmail}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    String team = request.getParameter("team");
	    String email = request.getParameter("email");
	    String methodOverride = request.getParameter("_method"); // 삭제 시 "DELETE"

	    int teamId = Integer.parseInt(team);

	    if ("DELETE".equalsIgnoreCase(methodOverride)) {
	        // 삭제 처리
	        teamUserService.removeTeamMember(teamId, email);
	    } else {
	        // 추가 처리
	        teamUserService.addTeamMember(teamId, email);
	    }

	    // PRG 패턴 (새로고침시 중복 방지)
	    response.sendRedirect(request.getContextPath() + "/members?team=" + teamId);
	}
	
}