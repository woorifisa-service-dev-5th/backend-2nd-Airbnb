package service;

import java.util.List;

import model.User;

public interface TeamUserService {

	/* 팀에 있는 전체 회원 조회 */
	public List<User> getTeamMembers(int teamId);
	
	/* 팀에 회원 추가 */
	public void addTeamMember(int teamId, String email);
	
	/* 팀에서 회원 삭제 */
	public void removeTeamMember(int teamId, String email);
}
