package config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * DB 연결을 관리하는 설정 클래스
 * - HikariCP 기반
 * - 서블릿에서 DbConfig.getConnection() 호출하여 사용
 */
public class DB {
	private static HikariDataSource dataSource;

    static {
        try {
            // 1. JDBC 드라이버 명시적 로딩
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. properties 파일 로드
            Properties props = new Properties();
            try (InputStream is = DB.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (is == null) {
                    throw new RuntimeException("db.properties 파일을 찾을 수 없습니다.");
                }
                props.load(is);
            }

            // 3. HikariCP 설정
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.username"));
            config.setPassword(props.getProperty("db.password"));
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

            // 커넥션 풀 세부 설정
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(300000);      // 5분
            config.setMaxLifetime(1800000);     // 30분
            config.setConnectionTimeout(10000); // 10초

            dataSource = new HikariDataSource(config);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("HikariCP 초기화 실패", e);
        }
    }

    // 커넥션 가져오기
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // 종료 시 자원 해제
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}