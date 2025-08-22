package service;

import java.util.List;

import model.TeamAccommodation;

public interface TeamAccommodationService {

	
	// 팀 숙소 추가 (위시리스트 담기)
	boolean addTeamAccommodation(int teamId, int accomId);

	// 팀이 추가한 숙소 + 좋아요 수 조회
	List<TeamAccommodation> getTeamAccommodationsWithLikes(int teamId);
	
	void increaseLike(int teamId, int accomId);
}
