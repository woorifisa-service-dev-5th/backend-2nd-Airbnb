<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>로그인</title>
<style>
  :root{
    --radius:12px; --border:#e5e7eb; --bg:#fff; --muted:#6b7280; --ink:#111827;
  }
  *{ box-sizing:border-box; }
  body{ font-family:system-ui,-apple-system,"Segoe UI",Roboto,"Noto Sans KR",sans-serif; margin:24px; color:var(--ink); background:#fff; }

  .wrap{ max-width:360px; margin:48px auto; }
  h1{ margin:0 0 12px; font-size:22px; }
  p.muted{ margin:0 0 18px; color:var(--muted); font-size:13px; }

  .card{
    border:1px solid var(--border);
    border-radius:var(--radius);
    padding:20px;
    background:#fff;
    box-shadow:0 1px 2px rgba(0,0,0,.03);
  }

  .form-row{ display:flex; flex-direction:column; gap:6px; margin-bottom:12px; }
  .form-label{ font-size:12px; color:var(--muted); }
  input[type=text], input[type=password]{
    height:40px; padding:0 12px; border:1px solid var(--border); border-radius:10px; outline:none;
  }
  input:focus{
    border-color:#4f46e5; box-shadow:0 0 0 3px rgba(79,70,229,.15);
  }

  .actions{ display:flex; gap:8px; margin-top:6px; }
  .btn{ flex:1; padding:10px 14px; border:1px solid #ccc; border-radius:10px; background:#fafafa; cursor:pointer; }
  .btn-primary{ background:#111827; color:#fff; border-color:#111827; }
  .helper{ margin-top:10px; display:flex; justify-content:space-between; font-size:12px; color:var(--muted); }
  .helper a{ color:#4f46e5; text-decoration:none; }

  .error{
    margin:0 0 12px; padding:10px 12px;
    border:1px solid #fecaca; background:#fff1f2; color:#b91c1c; border-radius:10px; font-size:13px;
  }
  .success{
    margin:0 0 12px; padding:10px 12px;
    border:1px solid #bbf7d0; background:#ecfdf5; color:#065f46; border-radius:10px; font-size:13px;
  }
</style>
</head>
<body>
  <div class="wrap">
    <h1>로그인</h1>
    <p class="muted">이메일과 비밀번호를 입력해 주세요.</p>

    <!-- 메시지 영역 -->
    <c:if test="${not empty loginError}">
      <div class="error">${loginError}</div>
    </c:if>
    <c:if test="${not empty loginMsg}">
      <div class="success">${loginMsg}</div>
    </c:if>

    <div class="card">
      <form action="${pageContext.request.contextPath}/login" method="post" autocomplete="on">
        <div class="form-row">
          <label class="form-label" for="email">이메일</label>
          <input id="email" name="email" type="text" placeholder="you@example.com" required autofocus />
        </div>

        <div class="form-row">
          <label class="form-label" for="password">비밀번호</label>
          <input id="password" name="password" type="password" placeholder="••••••••" required />
        </div>

        <!-- 로그인 후 돌아갈 경로를 넘기고 싶으면 서버에서 redirect 파라미터를 세팅해 hidden으로 전달 -->
        <c:if test="${not empty param.redirect}">
          <input type="hidden" name="redirect" value="${fn:escapeXml(param.redirect)}" />
        </c:if>

        <div class="actions">
          <button type="submit" class="btn btn-primary">로그인</button>
          <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/index.jsp'">취소</button>
        </div>

        <div class="helper">
          <span><a href="#">비밀번호 찾기</a></span>
          <span><a href="#">회원가입</a></span>
        </div>
      </form>
    </div>
  </div>
</body>
</html>
