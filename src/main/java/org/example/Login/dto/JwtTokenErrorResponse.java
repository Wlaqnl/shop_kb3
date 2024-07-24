package org.example.Login.dto;

import lombok.*;
import org.example.exception.Jwt.JwtTokenErrorCode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtTokenErrorResponse {
    private JwtTokenErrorCode jwtTokenErrorCode;
    private String errorMessage;
}
