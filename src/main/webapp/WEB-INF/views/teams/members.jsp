<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, model.User" %>
<%
  Integer teamId = (Integer) request.getAttribute("team");
  List<User> users = (List<User>) request.getAttribute("users");
  if (teamId == null) teamId = -1;
  if (users == null) users = Collections.emptyList();
  String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>팀 멤버 관리 - Team <%= teamId %></title>
  <style>
    body { font-family: system-ui, sans-serif; margin: 24px; }
    h1 { margin-bottom: 8px; }
    .actions { margin: 16px 0; display: flex; gap: 8px; align-items: center; }
    input, button { padding: 8px 10px; }
    table { width: 100%; border-collapse: collapse; margin-top: 12px; }
    th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
    th { background: #fafafa; }
    .muted { color: #777; }
    form.inline { display: inline; }
  </style>
</head>
<body>
  <h1>팀 멤버 관리</h1>
  <div class="muted">Team ID: <strong><%= teamId %></strong></div>

  <!-- 멤버 추가 (POST) -->
  <div class="actions">
    <form action="<%= ctx %>/members" method="post">
      <input type="hidden" name="team" value="<%= teamId %>">
      <input type="email" name="email" placeholder="이메일 입력" required>
      <button type="submit">멤버 추가</button>
    </form>

    <form action="<%= ctx %>/members" method="get" class="inline">
      <input type="hidden" name="team" value="<%= teamId %>">
      <button type="submit">새로고침</button>
    </form>

    <a href="<%= ctx %>/" style="margin-left:auto;">← 홈으로</a>
  </div>

  <!-- 멤버 목록 -->
  <table>
    <thead>
      <tr>
        <th style="width: 80px;">ID</th>
        <th style="width: 25%;">이름</th>
        <th>이메일</th>
        <th style="width: 120px;">액션</th>
      </tr>
    </thead>
    <tbody>
    <%
      if (users.isEmpty()) {
    %>
      <tr>
        <td colspan="4" class="muted">아직 팀 멤버가 없습니다.</td>
      </tr>
    <%
      } else {
        for (User u : users) {
          String uid = String.valueOf(u.getUserID());
          String uname = u.getName() == null ? "" : u.getName();
          String uemail = u.getEmail() == null ? "" : u.getEmail();
    %>
      <tr>
        <td><%= uid %></td>
        <td><%= uname %></td>
        <td><%= uemail %></td>
        <td>
          <!-- 삭제도 POST로 보내고 _method=DELETE 로 전달 -->
          <form action="<%= ctx %>/members" method="post" class="inline"
                onsubmit="return confirm('정말 삭제할까요?');">
            <input type="hidden" name="team" value="<%= teamId %>">
            <input type="hidden" name="email" value="<%= uemail %>">
            <input type="hidden" name="_method" value="DELETE">
            <button type="submit">삭제</button>
          </form>
        </td>
      </tr>
    <%
        }
      }
    %>
    </tbody>
  </table>
</body>
</html>
