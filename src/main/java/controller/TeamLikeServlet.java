package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.TeamAccommodationDao;
import service.TeamAccommodationService;
import service.impl.TeamAccommodationImpl;

@WebServlet("/like")
public class TeamLikeServlet extends HttpServlet {
	
	@Override
    public void init() throws ServletException {
        TeamAccommodationDao teamAccomDao = new TeamAccommodationDao();
        this.teamAccommodationService = new TeamAccommodationImpl(teamAccomDao);
    }
	
	private TeamAccommodationService teamAccommodationService;

	// 팀 숙소 추가
	// /like?team={teamID}&accom={accomID}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		String team = req.getParameter("team");
		int teamID = Integer.parseInt(team);

		String accom = req.getParameter("accom");
		int accomID = Integer.parseInt(accom);

		teamAccommodationService.increaseLike(teamID, accomID);
	}
}