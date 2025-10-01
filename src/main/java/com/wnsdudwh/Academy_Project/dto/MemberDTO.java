package com.wnsdudwh.Academy_Project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO
{
    private String userid;

    private String userpw;

    private String username;

    private String nickname;

    private String phone;

    private boolean enable;
}
