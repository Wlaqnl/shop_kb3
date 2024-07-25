package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.Login.dto.CustomUserInfoDto;
import org.example.exception.Jwt.JwtTokenErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.IOException;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * [JWT 관련 메서드를 제공하는 클래스]
 */
@Slf4j
@Getter
@Component
public class JwtUtil {
    private final Key key;
    private final long accessTokenExpTime;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration_time}") long accessTokenExpTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    /**
     * Access Token 생성
     *
     * @param member
     * @return Access Token String
     */
    public String createAccessToken(CustomUserInfoDto member) {
        return createToken(member, accessTokenExpTime);
    }


    /**
     * JWT 생성
     *
     * @param member
     * @param expireTime
     * @return JWT String
     */
    private String createToken(CustomUserInfoDto member, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("memberId", member.getMemberId());
        claims.put("email", member.getEmail());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * Token에서 User ID 추출
     *
     * @param token
     * @return User ID
     */
    public Long getUserId(String token) {
        return parseClaims(token).get("memberId", Long.class);
    }

    /**
     * Token에서 Email 추출
     *
     * @param token
     * @return Email
     */
    public String getEmail(String token) {
        return parseClaims(token).get("email", String.class);
    }


    /**
     * JWT 검증
     *
     * @param token
     * @return IsValidate
     */
    public boolean validateToken(String token, HttpServletResponse response) {

        try {
            if (token.isEmpty()) {
                throw new IllegalArgumentException();
            }

            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("JwtUtil | Invalid JWT Token", e);
            setErrorResponse(response, JwtTokenErrorCode.INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("JwtUtil | Expired JWT Token", e);
            setErrorResponse(response, JwtTokenErrorCode.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("JwtUtil | Unsupported JWT Token", e);
            setErrorResponse(response, JwtTokenErrorCode.UNSUPPORTED_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JwtUtil | JWT claims string is empty.", e);
            setErrorResponse(response, JwtTokenErrorCode.EMPTY_JWT_TOKEN);
        }
        return false;
    }
    private void test(String token) throws IllegalArgumentException {

    }


    /**
     * JWT Claims 추출
     *
     * @param accessToken
     * @return JWT Claims
     */
    public Claims parseClaims(String accessToken) {

        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private void setErrorResponse(
            HttpServletResponse response,
            JwtTokenErrorCode errorCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    public static class ErrorResponse {
        private final String code;
        private final String message;
    }

}
