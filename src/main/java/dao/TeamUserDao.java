package dao;

import model.User;
import config.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamUserDao {

    /* 이메일로 회원 id 조회 */
    private Integer findUserIdByEmail(String email) {
        final String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 못 찾으면 null
    }

    /* 팀에 있는 전체 회원 조회 */
    public List<User> getTeamMembers(int teamId) {
        final String sql = """
                SELECT u.id, u.name, u.email
                FROM team_users tu
                JOIN users u ON u.id = tu.user_id
                WHERE tu.team_id = ?
        """;

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teamId);

            try (ResultSet rs = ps.executeQuery()) {
                List<User> list = new ArrayList<>();
                while (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email")
                    );
                    list.add(user);
                }
                return list;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /* 팀에 회원 추가 */
    public void addTeamMember(int teamId, String email) {
        Integer userId = findUserIdByEmail(email);
        if (userId == null) {
            throw new IllegalArgumentException("해당 이메일 사용자가 없음: " + email);
        }

        final String sql = "INSERT INTO team_users(team_id, user_id) VALUES (?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teamId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* 팀에서 회원 삭제 */
    public void removeTeamMember(int teamId, String email) {
        Integer userId = findUserIdByEmail(email);
        if (userId == null) {
            throw new IllegalArgumentException("해당 이메일 사용자가 없음: " + email);
        }

        final String sql = "DELETE FROM team_users WHERE team_id = ? AND user_id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teamId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
