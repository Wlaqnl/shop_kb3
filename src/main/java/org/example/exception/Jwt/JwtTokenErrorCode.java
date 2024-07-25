package org.example.exception.Jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum JwtTokenErrorCode {
    INVALID_JWT_TOKEN(HttpStatus.BAD_REQUEST, "JWT_001", "유효하지 않은 토큰입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.BAD_REQUEST, "JWT_002", "만료된 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.BAD_REQUEST, "JWT_003", "지원하지 않는 토큰입니다."),
    EMPTY_JWT_TOKEN(HttpStatus.BAD_REQUEST, "JWT_004", "토큰 값이 비어 있습니다"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "COM-001", "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COM-002", "서버 오류입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
