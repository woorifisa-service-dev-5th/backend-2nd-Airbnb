package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import config.DB;
import dao.AccommodationDao;
import model.AccomCategoryEnum;
import model.Accomodation;
import model.BuildingCategoryEnum;

class AccommodationDaoTest {

    // 실제 테이블명에 맞게 수정
    private static final String TABLE_NAME = "accommodation";

    private int countAll() {
        try (Connection c = DB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + TABLE_NAME)) {
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            fail("전체 개수 조회 실패: " + e.getMessage());
            return -1;
        }
    }

    @AfterAll
    static void tearDownAll() {
        // 단일 실행 테스트라면 풀 종료
        DB.closeDataSource();
    }

    @Test
    @DisplayName("입력 필터가 모두 null 혹은 -1이라면 필터링되지 않은 모든 항목을 반환한다.")
    void test1() {
    	System.out.println("test1");
        AccommodationDao dao = new AccommodationDao();
        // amenities: 0 → 무시 (DAO 구현 기준)
        List<Accomodation> list = dao.filterAccomm(null, -1, -1, -1, null, null, 0);
        int total = countAll();

        assertNotNull(list);
        assertEquals(total, list.size(), "no-filter 결과 개수가 전체 행 개수와 달라요");
    }

    @Test
    @DisplayName("입력 필터가 정상적이라면 필터링된 항목을 반환한다.")
    void test2() {
    	System.out.println("test2");
        AccommodationDao dao = new AccommodationDao();

        AccomCategoryEnum accomCategory = AccomCategoryEnum.ROOM;
        int bedrooms = 1;
        int beds = 1;
        int bathrooms = 1;
        BigDecimal maxPrice = new BigDecimal("1120");
        BuildingCategoryEnum buildingCategory = BuildingCategoryEnum.HOUSE;
        int amenitiesMask = 4; // 예: 특정 편의시설 비트

        List<Accomodation> list = dao.filterAccomm(
                accomCategory, bedrooms, beds, bathrooms, maxPrice, buildingCategory, amenitiesMask);

        assertNotNull(list);

        // 반환된 모든 행이 조건을 만족하는지 검증 (비어있어도 '조건 준수' 관점에서 통과)
        for (Accomodation a : list) {
            assertEquals(accomCategory, a.getAccomCategory(), "accomCategory 불일치: " + a.getAccomID());
            assertTrue(a.getBedrooms() >= bedrooms, "bedrooms 조건 불만족: " + a.getAccomID());
            assertTrue(a.getBeds()    >= beds,     "beds 조건 불만족: " + a.getAccomID());
            assertTrue(a.getBathrooms() >= bathrooms, "bathrooms 조건 불만족: " + a.getAccomID());
            assertTrue(a.getPrice().compareTo(maxPrice) <= 0, "price 조건 불만족: " + a.getAccomID());
            assertEquals(buildingCategory, a.getBuildingCategory(), "buildingCategory 불일치: " + a.getAccomID());
            assertEquals(amenitiesMask, (a.getAmenitiesCategory() & amenitiesMask),
                    "amenities 비트 조건 불만족: " + a.getAccomID());
        }

        // 선택: 결과가 있으면 전체 개수보다 작거나 같은지 체크(필터가 적용됐는지 느슨히 확인)
        if (!list.isEmpty()) {
            assertTrue(list.size() <= countAll());
        }
    }

    @Test
    @DisplayName("입력 필터가 정상적이지 않다면 빈 항목을 반환한다.")
    void test3() {
    	System.out.println("test3");
        AccommodationDao dao = new AccommodationDao();
        // 현실적으로 매칭될 수 없는 큰 값으로 필터링
        List<Accomodation> list = dao.filterAccomm(null, 1_000_000_000, -1, -1, null, null, 0);
        assertTrue(list.isEmpty(), "불가능한 조건인데 결과가 비어있지 않습니다: " + list.size());
    }

    @Test
    @DisplayName("입력 필터 일부가 비어있다면 해당 항목을 제외한 내용을 필터링한 항목을 반환한다.")
    void test4() {
    	System.out.println("test4");
        AccommodationDao dao = new AccommodationDao();
        // bedrooms만 지정, 나머지 무시
        int bedrooms = 2;
        List<Accomodation> list = dao.filterAccomm(null, bedrooms, -1, -1, null, null, 0);

        assertNotNull(list);
        for (Accomodation a : list) {
            assertTrue(a.getBedrooms() >= bedrooms, "bedrooms 조건 불만족: " + a.getAccomID());
        }
    }
}
