package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.DB;
import model.DTO.UserSessionDto;

public class LoginDao {
	
	public LoginDao() {}
	
	public static UserSessionDto existsUser(String inputEmail, String inputPassword) {
		String sql = "SELECT id, name FROM users WHERE email = ? and password = ?";
		UserSessionDto user = null;
		
		try(Connection conn = DB.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);){
			
			stmt.setString(1, inputEmail);
			stmt.setString(2, inputPassword);
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
	            user = new UserSessionDto(rs.getInt("id"), rs.getString("name"), inputEmail);
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
}
