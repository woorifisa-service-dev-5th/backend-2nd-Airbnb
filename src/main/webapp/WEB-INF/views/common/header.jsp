<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header>
  <div class="container">
    <h1 class="logo">Airbnb Clone</h1>
    <nav>
      <a href="<%=request.getContextPath()%>/">홈</a>

      <c:choose>
        <c:when test="${empty sessionScope.user}">
          <a href="<%=request.getContextPath()%>/login">로그인</a>
        </c:when>
        <c:otherwise>
          <a href="<%=request.getContextPath()%>/members?team=${sessionScope.user.id}" class="welcome">${sessionScope.user.name}님</a>
          <a href="<%=request.getContextPath()%>/logout">로그아웃</a>
        </c:otherwise>
      </c:choose>
    </nav>
  </div>
</header>

<style>
  header {
    background-color: #f9f9f9;
    border-bottom: 1px solid #e5e7eb;
    padding: 8px 12px; /* 오른쪽으로 더 붙게 좌우 패딩 소폭 축소 */
  }

  header .container {
    display: flex;
    align-items: center;
    justify-content: flex-start;   /* 로고는 왼쪽 정렬 */
    max-width: 1240px;             /* 화면이 넓으면 더 시원하게 */
    margin: 0 auto;
  }

  .logo {
    font-size: 18px;
    font-weight: 700;
    margin: 0;
  }

  nav {
    margin-left: auto;             /* 메뉴를 오른쪽 끝으로 밀기 */
    display: flex;
    align-items: center;
    gap: 12px;                     /* 항목 간 간격을 조금 줄임 */
  }

  nav a {
    text-decoration: none;
    color: #111827;
    font-size: 14px;
    padding: 4px 6px;              /* 클릭 영역은 유지하되 시각적 간격 최소화 */
  }

  nav a:hover { color: #4f46e5; }

  .welcome {
    font-size: 14px;
    color: #374151;
    margin-right: 4px;             /* 텍스트와 로그아웃 사이 간격 최소화 */
  }
</style>
