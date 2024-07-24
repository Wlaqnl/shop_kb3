package org.example.exception.Jwt;

import lombok.Getter;

@Getter
public class JwtTokenException extends RuntimeException {
    private JwtTokenErrorCode jwtTokenErrorCode;
    private String detailMessage;

    public JwtTokenException(JwtTokenErrorCode jwtTokenErrorCode) {
        super(jwtTokenErrorCode.getMessage());
        this.jwtTokenErrorCode = jwtTokenErrorCode;
        this.detailMessage = jwtTokenErrorCode.getMessage();
    }

    public JwtTokenException(JwtTokenErrorCode jwtTokenErrorCode, String detailMessage) {
        super(detailMessage);
        this.jwtTokenErrorCode = jwtTokenErrorCode;
        this.detailMessage = detailMessage;
    }
}

