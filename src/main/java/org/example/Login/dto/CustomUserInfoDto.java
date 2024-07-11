package org.example.Login.dto;

import lombok.*;
import org.example.Login.constant.RoleType;
import org.springframework.data.querydsl.QPageRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomUserInfoDto{
    private Long memberId;

    private String email;

    private String name;

    private String password;


}
