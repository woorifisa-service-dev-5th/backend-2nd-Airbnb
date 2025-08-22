package config;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 애플리케이션 시작 시 필요한 초기화가 있다면 여기에
        System.out.println(">>> T1F4_AIRBNB 시작");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println(">>> T1F4_AIRBNB 종료, DB 리소스 정리 시작");

        // 1) HikariCP 종료
        try {
            DB.closeDataSource();
            System.out.println("HikariCP 종료 완료");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2) MySQL cleanup thread 종료
        try {
            AbandonedConnectionCleanupThread.uncheckedShutdown();
            System.out.println("MySQL cleanup thread 종료 완료");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3) JDBC 드라이버 등록 해제
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver d = drivers.nextElement();
                if (d.getClass().getClassLoader() == cl) {
                    DriverManager.deregisterDriver(d);
                    System.out.println("드라이버 해제: " + d);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
