package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.DB;
import model.AccomCategoryEnum;
import model.Accomodation;
import model.BuildingCategoryEnum;

public class AccommodationDao {
    private static final Logger log = LoggerFactory.getLogger(AccommodationDao.class);
    private static final String TABLE_NAME = "accommodation";

    Connection conn = null;

    public AccommodationDao() {
        try {
            conn = DB.getConnection();
        } catch (SQLException e) {
            log.error("DB 커넥션 획득 실패", e);
            throw new RuntimeException("DB 연결 실패", e);
        }
    }

    /**
     * 동적 필터:
     * - accom_category: null 무시, 값 있으면 '='
     * - bedrooms/beds/bathrooms: -1 무시, 0 이상이면 '>='
     * - price: null 무시, 값 있으면 '<='  (테스트 코드가 price <= maxPrice로 검증)
     * - buildingCategory: null 무시, 값 있으면 '='
     * - amenities: 0 이하면 무시, 양수면 '(amenitiesCategory & ?) = ?'
     */
    public List<Accomodation> filterAccomm(
            AccomCategoryEnum accom_category,
            int bedrooms,
            int beds,
            int bathrooms,
            BigDecimal price,
            BuildingCategoryEnum buildingCategory,
            int amenities
    ) {
        List<Accomodation> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, accom_category, bedrooms, beds, bathrooms, price, building_category, amenities ")
           .append("FROM ").append(TABLE_NAME).append(" WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (accom_category != null) {
            sql.append("AND accom_category = ? ");
            params.add(accom_category.name()); // DB에 enum을 문자열로 저장했다고 가정
        }
        if (bedrooms >= 0) {
            sql.append("AND bedrooms >= ? ");
            params.add(bedrooms);
        }
        if (beds >= 0) {
            sql.append("AND beds >= ? ");
            params.add(beds);
        }
        if (bathrooms >= 0) {
            sql.append("AND bathrooms >= ? ");
            params.add(bathrooms);
        }
        if (price != null) {
            // 테스트 코드가 price <= maxPrice를 검증하므로 <= 로 구현
            sql.append("AND price <= ? ");
            params.add(price);
        }
        if (buildingCategory != null) {
            sql.append("AND building_category = ? ");
            params.add(buildingCategory.name()); // DB에 enum을 문자열로 저장했다고 가정
        }
        if (amenities > 0) {
            sql.append("AND (amenities & ?) = ? ");
            params.add(amenities);
            params.add(amenities);
        }

        // 필요시 정렬
        sql.append("ORDER BY id ASC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            // 바인딩
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                int idx = i + 1;
                if (p instanceof Integer) {
                    ps.setInt(idx, (Integer) p);
                } else if (p instanceof BigDecimal) {
                    ps.setBigDecimal(idx, (BigDecimal) p);
                } else if (p instanceof String) {
                    ps.setString(idx, (String) p);
                } else {
                    ps.setObject(idx, p);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Accomodation a = new Accomodation(
                            rs.getInt("id"),
                            AccomCategoryEnum.valueOf(rs.getString("accom_category")),
                            rs.getInt("bedrooms"),
                            rs.getInt("beds"),
                            rs.getInt("bathrooms"),
                            rs.getBigDecimal("price"),
                            BuildingCategoryEnum.valueOf(rs.getString("building_category")),
                            rs.getInt("amenities")
                    );
                    list.add(a);

                 // ✅ 각 결과를 디버그 로그로 출력
                    if (log.isDebugEnabled()) {
                        log.debug("Fetched row: od={}, accom_category={}, bedrooms={}, beds={}, bathrooms={}, price={}, building_category={}, amenities={}",
                                a.getAccomID(), a.getAccomCategory(), a.getBedrooms(),
                                a.getBeds(), a.getBathrooms(), a.getPrice(),
                                a.getBuildingCategory(), a.getAmenitiesCategory());
                    }
                }
            }
        } catch (SQLException e) {
            log.error("filterAccomm 쿼리 실패: {}", e.getMessage(), e);
            throw new RuntimeException("숙소 필터링 조회 실패", e);
        }

        return list;
    }
}

