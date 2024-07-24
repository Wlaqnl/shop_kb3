package org.example.Login.dto;

import lombok.*;


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
