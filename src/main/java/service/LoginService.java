package service;

import model.DTO.UserSessionDto;

public interface LoginService {
	
	// 회원 조회
	public UserSessionDto findUser(String id, String pw);

}
