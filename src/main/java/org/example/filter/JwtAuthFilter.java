package org.example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.Jwt.JwtTokenErrorCode;
import org.example.util.JwtUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter { // OncePerRequestFilter -> 한 번 실행 보장

    private final JwtUtil jwtUtil;

    private static final List<String> NO_NEED_JWT = List.of(
//            "/api/v1/auth/**",
            "/api/v1/auth"
    );

    @Override
    /**
     * JWT 토큰 검증 필터 수행
     */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String uri = request.getRequestURI();
        System.out.println("FILTER HERE");
        System.out.println("======================");
        System.out.println(authorizationHeader);
        System.out.println(uri);
        System.out.println("======================");
        if (isPassedFilter(uri)) {
            System.out.println("JWT 없어서 PASS");
            filterChain.doFilter(request, response); // 다음 필터로 넘기기
        } else {
            //JWT가 헤더에 있는 경우
            System.out.println("JWT 있쬬");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
                System.out.println("Bearer 있쬬");
                String token = authorizationHeader.substring(6);
                token = token.trim();

                //JWT 유효성 검증
                if (jwtUtil.validateToken(token, response)) {
                    System.out.println("validateToken 했쬬");
                    Long userId = jwtUtil.getUserId(token);
                    String email = jwtUtil.getEmail(token);

                    System.out.println("!!!!!!!!!!!!!!!!!!!!");
                    System.out.println(userId);
                    System.out.println(email);
                    System.out.println("!!!!!!!!!!!!!!!!!!!!");

                    filterChain.doFilter(request, response); // 다음 필터로 넘기기
                } else {
                    System.out.println("TOKEN IS WRONG");
                }
            }
        }
    }

    private boolean isPassedFilter(String uri) {
        boolean result = false;
        for (String passUri : NO_NEED_JWT) {
            if (passUri.contains("/**")) {
                result = uri.startsWith(passUri.substring(0, (passUri.length() - 3)));
            } else {
                result = uri.equals(passUri);
            }
            if (result) break;
        }
        return result;
    }

}
