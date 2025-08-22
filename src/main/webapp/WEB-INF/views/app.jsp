<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dao.AccommodationDao, model.Accomodation, model.AccomCategoryEnum, model.BuildingCategoryEnum, java.util.*, java.math.BigDecimal" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>숙소 검색</title>
<style>
  :root{
    --radius:12px; --border:#e5e7eb; --bg:#fff; --muted:#6b7280; --ink:#111827;
  }
  * { box-sizing: border-box; }
  body { font-family: system-ui, -apple-system, "Segoe UI", Roboto, "Noto Sans KR", sans-serif; margin: 24px; color: var(--ink); }
  h1 { margin-bottom: 12px; }

  /* form & controls */
  form { display:block; }
  .form-row { display:flex; flex-direction:column; gap:6px; min-width: 180px; }
  .form-label { font-size:12px; color: var(--muted); }
  select, input[type=number]{
    height: 36px; padding: 0 10px; border:1px solid var(--border); border-radius:10px; background:#fff; outline:none;
  }
  select:focus, input[type=number]:focus{ border-color:#4f46e5; box-shadow:0 0 0 3px rgba(79,70,229,.15); }

  /* --- 가로 한 줄 필터 바 --- */
  .filter-row{
    display:flex; gap:12px; align-items:flex-end; overflow-x:auto; padding: 8px 4px 12px;
    border-bottom:1px dashed var(--border); margin-bottom: 12px;
  }
  .filter-row::-webkit-scrollbar{ height:8px; }
  .filter-row::-webkit-scrollbar-thumb{ background:#e5e7eb; border-radius:999px; }

  /* amenities section */
  details.amenities { margin-top:10px; border:1px solid var(--border); border-radius:var(--radius); padding:12px; background:#fff; }
  details.amenities > summary { cursor:pointer; font-weight:600; list-style:none; }
  details.amenities > summary::-webkit-details-marker { display:none; }
  .chips { display:flex; flex-wrap:wrap; gap:8px; margin-top:8px; }
  .chip { position:relative; }
  .chip input { position:absolute; opacity:0; pointer-events:none; }
  .chip label {
    display:inline-block; padding:8px 12px; border:1px solid var(--border); border-radius:999px;
    background:#fff; cursor:pointer; user-select:none;
  }
  .chip input:checked + label { background:#111827; color:#fff; border-color:#111827; }

  .actions { display:flex; gap:8px; margin-top:10px; }
  .btn { padding: 8px 14px; border: 1px solid #ccc; border-radius: 10px; background:#fafafa; cursor:pointer; }
  .btn-primary { background:#111827; color:#fff; border-color:#111827; }

  /* table */
  table { margin-top: 18px; border-collapse: collapse; width: 100%; }
  th, td { border-bottom: 1px solid #eee; padding: 10px; text-align: left; }
  th { background: #fafafa; }
  .muted { color:#777; font-size: 12px; }

  /* heart button */
  .btn-heart{
    display:inline-grid; place-items:center;
    width:36px; height:36px;
    border:1px solid var(--border); border-radius:999px;
    background:#fff; cursor:pointer; font-size:16px;
  }
  .btn-heart.liked{ background:#ffe6ea; border-color:#ffb3c0; }
  .btn-heart[disabled]{ opacity:.6; cursor:not-allowed; }
</style>

<script>
//✅ 초기화 POST 보낼 URL (채워 넣기)
const RESET_POST_URL = "http://localhost:8080/MyAirBNB/home"; 
// 예: "<%= request.getContextPath() %>/home/reset"

function submitEmptyPost(url){
  if(!url){
    console.warn("RESET_POST_URL 미설정");
    return;
  }
  // 입력 필드 없는 폼을 동적으로 만들어 바로 제출 -> 쿼리스트링 없이 빈 바디 POST
  const f = document.createElement('form');
  f.method = 'POST';
  f.action = url;
  // 필요 시 CSRF 토큰이 있다면 여기서 hidden 추가
  // const t = document.createElement('input');
  // t.type = 'hidden'; t.name = '_csrf'; t.value = '...';
  // f.appendChild(t);

  document.body.appendChild(f);
  f.submit();
}

function resetAndPost(){
  resetFormAndAmenities();   // 기존 초기화
  submitEmptyPost(RESET_POST_URL); // 빈 POST 전송(네비게이션 발생)
}
  // === 설정: 팀에 숙소 추가 요청 URL (여기에만 경로 채워주세요) ===
  const ADD_TO_TEAM_URL = ""; // 예: "<%= request.getContextPath() %>/team/accommodations/add"

  // 편의시설 비트 매핑 (서버/DB와 반드시 동일)
  const AMENITY_BITS = { hottub:1, pool:2, wifi:4, ac:8, tv:16, beach:32 };

  function updateAmenitiesHidden() {
    const mask = Array.from(document.querySelectorAll('input[data-amenity]:checked'))
      .reduce((acc, el) => acc | (parseInt(el.value, 10) || 0), 0);
    const hidden = document.getElementById("amenities");
    if (hidden) hidden.value = mask;
  }

  function resetFormAndAmenities() {
    const form = document.getElementById("filterForm");
    form.reset();
    updateAmenitiesHidden();
  }

  // 하트 버튼 POST
  async function postLike(accomId){
    if (!ADD_TO_TEAM_URL) {
      // 경로 미설정 시 콘솔 경고만 (서버 에러 막기)
      console.warn("ADD_TO_TEAM_URL 미설정: accomId=", accomId);
      return { ok:false, message:"POST 경로가 설정되지 않았습니다." };
    }
    try{
      const res = await fetch(ADD_TO_TEAM_URL, {
        method: "POST",
        headers: { "Content-Type":"application/x-www-form-urlencoded; charset=UTF-8" },
        body: new URLSearchParams({ accomId: String(accomId) })
      });
      if (!res.ok) {
        const text = await res.text().catch(()=> "");
        return { ok:false, message: text || ("HTTP " + res.status) };
      }
      return { ok:true };
    }catch(err){
      return { ok:false, message: err?.message || "네트워크 오류" };
    }
  }

  window.addEventListener('DOMContentLoaded', () => {
    // 편의시설 체크박스 변경 감지
    document.querySelectorAll('input[type=checkbox][data-amenity]')
      .forEach(cb => cb.addEventListener('change', updateAmenitiesHidden));
    updateAmenitiesHidden();

    // 하트 클릭(이벤트 위임)
    const table = document.querySelector('#resultTable');
    if (table){
      table.addEventListener('click', async (e) => {
        const btn = e.target.closest('.btn-heart');
        if (!btn) return;

        const accomId = btn.getAttribute('data-accom-id');
        if (!accomId || btn.disabled) return;

        btn.disabled = true;
        const prevLiked = btn.classList.contains('liked');

        // 낙관적 UI 토글
        btn.classList.toggle('liked', !prevLiked);
        btn.textContent = !prevLiked ? '♥' : '♡';

        const { ok, message } = await postLike(accomId);
        if (!ok){
          // 실패 시 롤백
          btn.classList.toggle('liked', prevLiked);
          btn.textContent = prevLiked ? '♥' : '♡';
          alert("추가 실패: " + message);
        }
        btn.disabled = false;
      });
    }
  });
</script>
</head>
<body>
<h1>숙소 검색</h1>

<%
    // --------- 파라미터 파싱 & DAO 호출 ----------
    request.setCharacterEncoding("UTF-8");

    String pAccomCat = request.getParameter("accomCategory");
    String pBedrooms = request.getParameter("bedrooms");
    String pBeds = request.getParameter("beds");
    String pBathrooms = request.getParameter("bathrooms");
    String pMaxPrice = request.getParameter("maxPrice");
    String pBuildingCat = request.getParameter("buildingCategory");
    String pAmenities = request.getParameter("amenities"); // hidden (정수)

    AccomCategoryEnum accomCategory = null;
    BuildingCategoryEnum buildingCategory = null;
    int bedrooms = -1, beds = -1, bathrooms = -1, amenities = 0;
    BigDecimal maxPrice = null;

    try { if (pAccomCat != null && !pAccomCat.isBlank()) accomCategory = AccomCategoryEnum.valueOf(pAccomCat.trim()); } catch (Exception ignore) {}
    try { if (pBuildingCat != null && !pBuildingCat.isBlank()) buildingCategory = BuildingCategoryEnum.valueOf(pBuildingCat.trim()); } catch (Exception ignore) {}
    try { if (pBedrooms != null && !pBedrooms.isBlank()) bedrooms = Integer.parseInt(pBedrooms.trim()); } catch (Exception ignore) {}
    try { if (pBeds != null && !pBeds.isBlank()) beds = Integer.parseInt(pBeds.trim()); } catch (Exception ignore) {}
    try { if (pBathrooms != null && !pBathrooms.isBlank()) bathrooms = Integer.parseInt(pBathrooms.trim()); } catch (Exception ignore) {}
    try { if (pMaxPrice != null && !pMaxPrice.isBlank()) maxPrice = new BigDecimal(pMaxPrice.trim()); } catch (Exception ignore) {}
    try { if (pAmenities != null && !pAmenities.isBlank()) amenities = Integer.parseInt(pAmenities.trim()); } catch (Exception ignore) {}

    AccommodationDao dao = new AccommodationDao();
    List<Accomodation> results = dao.filterAccomm(
      accomCategory, bedrooms, beds, bathrooms, maxPrice, buildingCategory, amenities
    );
%>

<form id="filterForm" method="post"
      action="<%= request.getContextPath() %>/home"
      onsubmit="updateAmenitiesHidden()">

  <!-- ✅ 1줄 가로 정렬 필터 바 -->
  <div class="filter-row">
    <div class="form-row">
      <span class="form-label">숙소 유형</span>
      <select name="accomCategory">
        <option value="">전체</option>
        <option value="ROOM" <%= "ROOM".equals(pAccomCat) ? "selected": "" %>>방(ROOM)</option>
        <option value="ENTIRE_HOME" <%= "ENTIRE_HOME".equals(pAccomCat) ? "selected": "" %>>집 전체(ENTIRE_HOME)</option>
      </select>
    </div>

    <div class="form-row">
      <span class="form-label">건물 유형</span>
      <select name="buildingCategory">
        <option value="">전체</option>
        <option value="HOUSE" <%= "HOUSE".equals(pBuildingCat) ? "selected": "" %>>단독/다세대(HOUSE)</option>
        <option value="APARTMENT" <%= "APARTMENT".equals(pBuildingCat) ? "selected": "" %>>아파트(APARTMENT)</option>
        <option value="GUESTHOUSE" <%= "GUESTHOUSE".equals(pBuildingCat) ? "selected": "" %>>게스트용 별채(GUESTHOUSE)</option>
        <option value="HOTEL" <%= "HOTEL".equals(pBuildingCat) ? "selected": "" %>>호텔(HOTEL)</option>
      </select>
    </div>

    <div class="form-row">
      <span class="form-label">침실(이상)</span>
      <input type="number" name="bedrooms" min="0" inputmode="numeric"
             placeholder="예: 1" value="<%= pBedrooms != null ? pBedrooms : "" %>">
    </div>

    <div class="form-row">
      <span class="form-label">침대(이상)</span>
      <input type="number" name="beds" min="0" inputmode="numeric"
             placeholder="예: 1" value="<%= pBeds != null ? pBeds : "" %>">
    </div>

    <div class="form-row">
      <span class="form-label">욕실(이상)</span>
      <input type="number" name="bathrooms" min="0" inputmode="numeric"
             placeholder="예: 1" value="<%= pBathrooms != null ? pBathrooms : "" %>">
    </div>

    <div class="form-row">
      <span class="form-label">최대 가격(≤)</span>
      <input type="number" step="1" name="maxPrice" inputmode="numeric"
             placeholder="예: 70" value="<%= pMaxPrice != null ? pMaxPrice : "" %>">
    </div>
  </div>

  <!-- 편의시설: 접기/펼치기 + 칩 -->
  <%
    int amenMask = 0;
    try { if (pAmenities != null && !pAmenities.isBlank()) amenMask = Integer.parseInt(pAmenities.trim()); } catch (Exception ignore) {}
    Object[][] AMENITIES = {
      {"hottub", "대형 욕조", 1},
      {"pool",   "수영장",   2},
      {"wifi",   "와이파이", 4},
      {"ac",     "에어컨",   8},
      {"tv",     "TV",      16},
      {"beach",  "해변 인접",32}
    };
  %>
  <details class="amenities" <%= amenMask>0 ? "open": "" %>>
    <summary>편의시설 선택 (클릭 시 펼쳐짐)</summary>
    <div class="chips">
      <% for (Object[] am : AMENITIES) {
           String key   = (String) am[0];
           String label = (String) am[1];
           int    bit   = (Integer) am[2];
           String id    = "amenity_" + key;
           boolean checked = (amenMask & bit) != 0;
      %>
        <div class="chip">
          <input type="checkbox" id="<%= id %>" data-amenity value="<%= bit %>" <%= checked ? "checked": "" %> />
          <label for="<%= id %>"><%= label %></label>
        </div>
      <% } %>
    </div>
    <input type="hidden" id="amenities" name="amenities" value="<%= pAmenities != null ? pAmenities : "0" %>"/>
  </details>

  <div class="actions">
    <button type="submit" class="btn btn-primary">검색</button>
    <button type="button" class="btn" onclick="resetAndPost()">초기화</button>
  </div>
</form>

<% if (results != null) { %>
  <h2 style="margin-top:22px;">검색 결과 (<%= results.size() %>건)</h2>
  <table id="resultTable">
    <thead>
      <tr>
        <th>ID</th>
        <th>숙소유형</th>
        <th>건물유형</th>
        <th>침실</th>
        <th>침대</th>
        <th>욕실</th>
        <th>가격</th>
        <th>팀에 추가하기</th>
      </tr>
    </thead>
    <tbody>
    <%
      for (Accomodation a : results) {
    %>
      <tr>
        <td><%= a.getAccomID() %></td>
        <td><%= a.getAccomCategory() %></td>
        <td><%= a.getBuildingCategory() %></td>
        <td><%= a.getBedrooms() %></td>
        <td><%= a.getBeds() %></td>
        <td><%= a.getBathrooms() %></td>
        <td><%= a.getPrice() %></td>
        <td>
          <!-- 처음엔 빈 하트(♡). 서버에서 이미 찜한 목록을 알고 있다면 조건부로 'liked' 클래스와 ♥로 렌더링 -->
          <button type="button" class="btn-heart" data-accom-id="<%= a.getAccomID() %>" aria-label="팀에 추가">♡</button>
        </td>
      </tr>
    <%
      }
    %>
    </tbody>
  </table>
<% } %>
</body>
</html>
