package com.wnsdudwh.Academy_Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordchangeDTO
{
    private String currentPassword;     //현재 비밀번호
    private String newPassword;             //새 비밀번호
}
