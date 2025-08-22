package service.impl;

import dao.LoginDao;
import model.DTO.UserSessionDto;
import service.LoginService;

public class LoginServiceImpl implements LoginService{
	
	private static LoginService service = new LoginServiceImpl();
	private LoginServiceImpl() {}
	
	public static LoginService getInstance() {
		return service;
	}

	public UserSessionDto findUser(String inputId, String inputPw) {
		
		// 유저 존재 여부 확인
		UserSessionDto user = LoginDao.existsUser(inputId, inputPw);
		
		// id, 이름, email 반환
		return user;
	}
}
