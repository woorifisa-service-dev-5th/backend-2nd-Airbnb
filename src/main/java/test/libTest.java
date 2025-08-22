package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.DB;

public class libTest {
    private static final Logger logger = LoggerFactory.getLogger(libTest.class);

    public static void main(String[] args) {
        String sql = "SELECT NOW()";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            logger.info("✅ DB.getConnection() 성공! (autoCommit={}, catalog={})",
                    conn.getAutoCommit(), conn.getCatalog());

            if (rs.next()) {
                logger.info("DB 응답 시간: {}", rs.getString(1));
            }
        } catch (Exception e) {
            logger.error("❌ DB 테스트 실패: {}", e.getMessage(), e);
        } finally {
            // 단일 실행 테스트(main)라면 풀 종료해도 OK.
            // 서블릿/웹앱에서는 컨테이너 종료 시 한 번만 호출하세요.
            DB.closeDataSource();
        }
    }
}