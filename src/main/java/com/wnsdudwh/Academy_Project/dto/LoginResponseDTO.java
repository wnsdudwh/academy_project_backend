package com.wnsdudwh.Academy_Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO
{
    //로그인시 보낼 데이터들 추가 (토큰, 닉네임, 권한, ~~)
    private String token;

    private String nickname;
    
    private String role;
}
