package org.example.exception.Jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.Login.dto.JwtTokenErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.example.exception.Jwt.JwtTokenErrorCode.INTERNAL_SERVER_ERROR;
import static org.example.exception.Jwt.JwtTokenErrorCode.INVALID_REQUEST;

@Slf4j
@RestControllerAdvice
public class JwtTokenExceptionHandler {
    @ExceptionHandler(JwtTokenException.class)
    public JwtTokenErrorResponse handlerException(
            JwtTokenException e,
            HttpServletRequest request) {
        log.error("errorCode : {}, url : {}, message : {}",
                e.getJwtTokenErrorCode(), request.getRequestURI(), e.getDetailMessage());
        return JwtTokenErrorResponse.builder()
                .jwtTokenErrorCode(e.getJwtTokenErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }

    @ExceptionHandler(value={
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class
    })
    public JwtTokenErrorResponse jwtTokenErrorResponse(
            Exception e, HttpServletRequest request
    ){
        log.error("url : {}, message : {}", request.getRequestURI(), e.getMessage());
        return JwtTokenErrorResponse.builder()
                .jwtTokenErrorCode(INVALID_REQUEST)
                .errorMessage(INVALID_REQUEST.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public JwtTokenErrorResponse handleException(
            Exception e, HttpServletRequest request
    ){
        log.error("url : {}, message : {}",
                request.getRequestURI(), e.getMessage());
        return JwtTokenErrorResponse.builder()
                .jwtTokenErrorCode(INTERNAL_SERVER_ERROR)
                .errorMessage(INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }
}
