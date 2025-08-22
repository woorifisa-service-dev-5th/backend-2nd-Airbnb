package model.DTO;

public class UserSessionDto {
	int id;
	String name;
	String email;
	
	// 생성자 추가
	public UserSessionDto(int id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}
	
}
