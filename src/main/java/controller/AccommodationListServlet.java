package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.TeamAccommodationDao;
import model.TeamAccommodation;
import service.TeamAccommodationService;
import service.impl.TeamAccommodationImpl;

@WebServlet("/accomodations")
public class AccommodationListServlet extends HttpServlet {
	
	@Override
    public void init() throws ServletException {
        TeamAccommodationDao teamAccomDao = new TeamAccommodationDao();
        this.teamAccommodationService = new TeamAccommodationImpl(teamAccomDao);
    }
	
	private TeamAccommodationService teamAccommodationService;
	
	// 팀 숙소 정보 조회
	// "/accomodations?team={teamID}"
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String team = req.getParameter("team");
		int teamID = Integer.parseInt(team);
		
		List<TeamAccommodation> accomList = teamAccommodationService.getTeamAccommodationsWithLikes(teamID);
		
		req.setAttribute("team", teamID);
		req.setAttribute("accomList", accomList);
		
		req.getRequestDispatcher("/WEB-INF/views/teams/favorites.jsp").forward(req, resp); // TODO : app.jsp로 변경하기
		
	}

	// 팀 숙소 추가
	// /accomodations?team={teamID}&accom={accomID}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String team = req.getParameter("team");
		int teamID = Integer.parseInt(team);
		
		String accom = req.getParameter("accom");
		int accomID = Integer.parseInt(accom);

		teamAccommodationService.addTeamAccommodation(teamID, accomID);
	}
	
}