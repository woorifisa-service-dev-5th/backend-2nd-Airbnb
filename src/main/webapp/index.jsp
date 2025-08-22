<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // 로그인 안 했으면 /login 으로 리다이렉트
    if (session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    // 로그인 했으면 /home 서블릿으로 내부 포워드 (여기서 화면 렌더링)
    request.getRequestDispatcher("/home").forward(request, response);
%>
