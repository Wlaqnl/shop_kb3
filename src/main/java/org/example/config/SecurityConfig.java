package org.example.config;

import jakarta.servlet.Filter;
import lombok.AllArgsConstructor;
import org.example.exception.Jwt.JwtTokenExceptionHandler;
import org.example.filter.ExceptionHandlerFilter;
import org.example.filter.JwtAuthFilter;
import org.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;

    //추가--------------------------
    @Autowired
    private ExceptionHandlerFilter exceptionHandlerFilter;
    //-------------------------------

    private static final String[] AUTH_WHITELIST = {
            "/api/v1/member/**", "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html", "/api/v1/auth/**"
    };

    private static final String[] AUTH_BLACKLIST = {
            "/api/v1/auth/test"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("filterChain");

        //CSRF, CORS
        http.csrf((csrf) -> csrf.disable());
        http.cors(Customizer.withDefaults());

        //세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 or 사용 X
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        // 권한 규칙 작성
        http.authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(AUTH_BLACKLIST).authenticated()
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        //@PreAuthrization을 사용할 것이기 때문에 모든 경로에 대한 인증처리는 Pass
                        .anyRequest().permitAll()
        );

        return http.build();
    }

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthFilter> jwtFilterBean = new FilterRegistrationBean<>();
        jwtFilterBean.setFilter(new JwtAuthFilter(jwtUtil));
        jwtFilterBean.addUrlPatterns("/api/v1/auth/*");
        jwtFilterBean.setOrder(2);
        return jwtFilterBean;
    }

    @Bean
    public FilterRegistrationBean<ExceptionHandlerFilter> AuthFailHandlerFilter() {
        FilterRegistrationBean<ExceptionHandlerFilter> authFailHandlerFilterBean = new FilterRegistrationBean<>();
        authFailHandlerFilterBean.setFilter(new ExceptionHandlerFilter());
        authFailHandlerFilterBean.setOrder(1);
        return authFailHandlerFilterBean;
    }
}
