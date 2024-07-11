package org.example.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.util.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter { // OncePerRequestFilter -> 한 번 실행 보장

    private final JwtUtil jwtUtil;

    private static final List<String> NO_NEED_LOGIN = List.of(
            "/api/v1/auth/no-need-login"
    );

    @Override
    /**
     * JWT 토큰 검증 필터 수행
     */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String uri = request.getRequestURI();
        System.out.println("HERE");
        System.out.println("======================");
        System.out.println(authorizationHeader);
        System.out.println(uri);
        System.out.println("======================");
        if (NO_NEED_LOGIN.contains(uri)) {
            System.out.println("PASS");
            filterChain.doFilter(request, response); // 다음 필터로 넘기기
        } else {
            //JWT가 헤더에 있는 경우
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                //JWT 유효성 검증
                if (jwtUtil.validateToken(token)) {
                    Long userId = jwtUtil.getUserId(token);
                    String email = jwtUtil.getEmail(token);

                    System.out.println("!!!!!!!!!!!!!!!!!!!!");
                    System.out.println(userId);
                    System.out.println(email);
                    System.out.println("!!!!!!!!!!!!!!!!!!!!");

                    filterChain.doFilter(request, response); // 다음 필터로 넘기기
                }
                System.out.println("TOKEN IS WRONG");
            }
        }
    }
}
