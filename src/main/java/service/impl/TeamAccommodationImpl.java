package service.impl;

import java.util.List;

import dao.TeamAccommodationDao;
import model.TeamAccommodation;
import service.TeamAccommodationService;

public class TeamAccommodationImpl implements TeamAccommodationService {

	private final TeamAccommodationDao teamAccommodationDao;
	 
	public TeamAccommodationImpl(TeamAccommodationDao teamAccommodationDao) {
		this.teamAccommodationDao = teamAccommodationDao;
	}

	// 팀 숙소 추가 (위시리스트 담기)
	public boolean addTeamAccommodation(int teamId, int accomId) {
		return teamAccommodationDao.add(teamId, accomId);
	}

	// 팀이 추가한 숙소 + 좋아요 수 조회
	public List<TeamAccommodation> getTeamAccommodationsWithLikes(int teamId) {
		return teamAccommodationDao.findByTeam(teamId);
	}

	public void increaseLike(int teamId, int accomId) {
		teamAccommodationDao.incrementLike(teamId, accomId);
	}

}
