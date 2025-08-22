package service.impl;

import java.util.List;

import dao.TeamUserDao;
import model.User;
import service.TeamUserService;

public class TeamUserServiceImpl implements TeamUserService {
	
	private final TeamUserDao teamUserDao;
	
	public TeamUserServiceImpl (TeamUserDao teamUserDao) {
		this.teamUserDao = teamUserDao;
	}
	
	/* 팀에 있는 전체 회원 조회 */
	public List<User> getTeamMembers(int teamId) {
		return teamUserDao.getTeamMembers(teamId);
	}
	
	/* 팀에 회원 추가 */
	public void addTeamMember(int teamId, String email) {
		teamUserDao.addTeamMember(teamId, email);
	}
	
	/* 팀에서 회원 삭제 */
	public void removeTeamMember(int teamId, String email) {
		teamUserDao.removeTeamMember(teamId, email);
	}
	
	
}
