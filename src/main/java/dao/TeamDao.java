package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.DB;

public class TeamDao {
	private static final Logger log = LoggerFactory.getLogger(TeamDao.class);

    private static final String SQL_FIND_BY_USER =
            "SELECT team_id FROM team_users WHERE user_id = ?";

    private static final String SQL_FIND_TEAM_NAME_BY_ID =
            "SELECT name FROM team WHERE id = ?";

    /** userId가 속한 모든 team_id 반환 (없으면 빈 리스트) */
    public static List<Integer> findTeamIdsByUserId(int userId) {
        List<Integer> result = new ArrayList<>();
        long start = System.nanoTime();

        // 🔎 디버그: 실행할 SQL과 파라미터
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL: {}", SQL_FIND_BY_USER);
            log.debug("With params: userId={}", userId);
        }

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USER)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                int row = 0;
                while (rs.next()) {
                    int teamId = rs.getInt("team_id");
                    result.add(teamId);
                    row++;

                    // ✅ 각 결과 행 디버그 출력
                    if (log.isDebugEnabled()) {
                        log.debug("Row {} -> team_id={}", row, teamId);
                    }
                }
            }

            if (log.isDebugEnabled()) {
                double ms = (System.nanoTime() - start) / 1_000_000.0;
                log.debug("Query done. rows={}, elapsedMs={}, result={}", result.size(), ms, result);
            }

            return result;

        } catch (SQLException e) {
            log.error("findTeamIdsByUserId 실패 (userId={})", userId, e);
            throw new RuntimeException("팀 조회 실패", e);
        }
    }

    /** teamID로 teams.name을 조회 (없으면 null) */
    public String idToName(int teamID) {
        long start = System.nanoTime();

        if (log.isDebugEnabled()) {
            log.debug("Executing SQL: {}", SQL_FIND_TEAM_NAME_BY_ID);
            log.debug("With params: teamID={}", teamID);
        }

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_TEAM_NAME_BY_ID)) {

            ps.setInt(1, teamID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    if (log.isDebugEnabled()) {
						log.debug("Fetched row -> id={}, name={}", teamID, name);
					}

                    double ms = (System.nanoTime() - start) / 1_000_000.0;
                    if (log.isDebugEnabled()) {
						log.debug("idToName done. elapsedMs={}", ms);
					}
                    return name;
                } else {
                    if (log.isDebugEnabled()) {
						log.debug("No team found for id={}", teamID);
					}
                    return null;
                }
            }

        } catch (SQLException e) {
            log.error("idToName 실패 (teamID={})", teamID, e);
            throw new RuntimeException("팀 조회 실패", e);
        }
    }
}