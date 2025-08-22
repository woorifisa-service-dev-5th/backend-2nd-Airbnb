package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.AccommodationDao;
import model.AccomCategoryEnum;
import model.Accomodation;
import model.BuildingCategoryEnum;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(HomeServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST 본문 한글 파라미터 처리
        request.setCharacterEncoding("UTF-8");
        doGet(request, response); // POST도 동일하게 처리
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 1) 파라미터 파싱
        String pAccomCat    = request.getParameter("accomCategory");
        String pBedrooms    = request.getParameter("bedrooms");
        String pBeds        = request.getParameter("beds");
        String pBathrooms   = request.getParameter("bathrooms");
        String pMaxPrice    = request.getParameter("maxPrice");
        String pBuildingCat = request.getParameter("buildingCategory");
        String pAmenities   = request.getParameter("amenities"); // 비트마스크 정수

        AccomCategoryEnum accomCategory = null;
        BuildingCategoryEnum buildingCategory = null;
        int bedrooms = -1, beds = -1, bathrooms = -1, amenities = 0;
        BigDecimal maxPrice = null;

        try {
            if (pAccomCat != null && !pAccomCat.isBlank()) {
                accomCategory = AccomCategoryEnum.valueOf(pAccomCat.trim());
            }
        } catch (Exception ignore) { /* 무시(전체) */ }

        try {
            if (pBuildingCat != null && !pBuildingCat.isBlank()) {
                buildingCategory = BuildingCategoryEnum.valueOf(pBuildingCat.trim());
            }
        } catch (Exception ignore) { /* 무시(전체) */ }

        try { if (pBedrooms  != null && !pBedrooms.isBlank()) {
			bedrooms  = Integer.parseInt(pBedrooms.trim());
		} }  catch (Exception ignore) {}
        try { if (pBeds      != null && !pBeds.isBlank()) {
			beds      = Integer.parseInt(pBeds.trim());
		} }      catch (Exception ignore) {}
        try { if (pBathrooms != null && !pBathrooms.isBlank()) {
			bathrooms = Integer.parseInt(pBathrooms.trim());
		} } catch (Exception ignore) {}
        try { if (pMaxPrice  != null && !pMaxPrice.isBlank()) {
			maxPrice  = new BigDecimal(pMaxPrice.trim());
		} }    catch (Exception ignore) {}
        try { if (pAmenities != null && !pAmenities.isBlank()) {
			amenities = Integer.parseInt(pAmenities.trim());
		} } catch (Exception ignore) {}

        // 2) DAO 조회
        List<Accomodation> results = null;
        try {
            AccommodationDao dao = new AccommodationDao();
            results = dao.filterAccomm(accomCategory, bedrooms, beds, bathrooms, maxPrice, buildingCategory, amenities);
        } catch (Exception e) {
            log.error("숙소 필터 조회 실패", e);
            // 필요 시 에러 페이지로 포워드하거나 메시지 전달
        }

        // 3) JSP에서 사용할 값 세팅 (폼 값 유지 + 결과)
        request.setAttribute("results", results);
        request.setAttribute("accomCategory", pAccomCat);
        request.setAttribute("bedrooms", pBedrooms);
        request.setAttribute("beds", pBeds);
        request.setAttribute("bathrooms", pBathrooms);
        request.setAttribute("maxPrice", pMaxPrice);
        request.setAttribute("buildingCategory", pBuildingCat);
        request.setAttribute("amenities", pAmenities);

        // 4) 내부 포워드
        request.getRequestDispatcher("/WEB-INF/views/app.jsp")
               .forward(request, response);
    }
}
