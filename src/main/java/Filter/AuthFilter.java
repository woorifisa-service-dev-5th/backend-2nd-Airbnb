package Filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.DTO.UserSessionDto;

@WebFilter("/*")
public class AuthFilter implements Filter {
    
    // 인증이 필요하지 않은 URL 패턴들
    private static final List<String> PUBLIC_URLS = Arrays.asList(
        "/login",
        "/logout", 
        "/index.jsp",
        "/WEB-INF/views/users/login.jsp",
        "/css/",
        "/js/",
        "/images/",
        "/favicon.ico"
    );
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        
        // 컨텍스트 패스를 제거한 실제 요청 경로
        String path = requestURI.substring(contextPath.length());
        
        // 공개 URL인지 확인
        if (isPublicUrl(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // 세션에서 사용자 정보 확인
        HttpSession session = httpRequest.getSession(false);
        UserSessionDto user = null;
        
        if (session != null) {
            user = (UserSessionDto) session.getAttribute("user");
        }
        
        // 인증되지 않은 사용자인 경우 로그인 페이지로 리다이렉트
        if (user == null) {
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }
        
        // 인증된 사용자인 경우 다음 필터 또는 서블릿으로 전달
        chain.doFilter(request, response);
    }
    
    /**
     * 공개 URL인지 확인하는 메서드
     */
    private boolean isPublicUrl(String path) {
        for (String publicUrl : PUBLIC_URLS) {
            if (path.equals(publicUrl) || path.startsWith(publicUrl)) {
                return true;
            }
        }
        return false;
    }
}
