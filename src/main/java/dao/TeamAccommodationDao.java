package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import config.DB;
import model.TeamAccommodation;

public class TeamAccommodationDao {
	
	// 팀에 숙소 추가 (위시리스트 담기)
    private static final String SQL_INSERT = """
        INSERT INTO team_accom (accom_id, team_id, `like`)
        VALUES (?, ?, 0)
        ON DUPLICATE KEY UPDATE `like` = `like`
    """;

    public boolean add(int teamId, int accomId) {
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, accomId);
            ps.setInt(2, teamId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 팀이 추가한 숙소 목록 조회
    private static final String SQL_FIND_BY_TEAM = """
        SELECT team_id, accom_id, `like`
          FROM team_accom
         WHERE team_id = ?
    """;

    public List<TeamAccommodation> findByTeam(int teamId) {
        List<TeamAccommodation> list = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_TEAM)) {
            ps.setInt(1, teamId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TeamAccommodation ta = new TeamAccommodation(
                        rs.getInt("team_id"),
                        rs.getInt("accom_id"),
                        rs.getInt("like")
                    );
                    list.add(ta);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // 좋아요 +1 (증가)
    private static final String SQL_INC_LIKE = """
        UPDATE team_accom SET `like` = `like` + 1 WHERE accom_id = ? AND team_id = ?
    """;

    public boolean incrementLike(int teamId, int accomId) {
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INC_LIKE)) {
            ps.setInt(1, accomId);
            ps.setInt(2, teamId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
