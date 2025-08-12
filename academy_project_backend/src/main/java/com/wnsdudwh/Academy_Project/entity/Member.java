package com.wnsdudwh.Academy_Project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Member
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(nullable = false)
    private String usernumber;

    @Column(nullable = false, length = 16, unique = true)
    private String userid;

    @Column(nullable = false)
    private String userpw;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, length = 12, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)    // 문자열로 저장되도록 변경(기존은 0~3 숫자)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.ROLE_USER; // 회원이 되면 기본값은 USER

    @Column
    @CreatedDate
    private LocalDateTime regDate;

    @Column(nullable = false) // DB에 저장할 때 null 안 됨
    @Builder.Default          // 빌더 패턴 쓸 때 기본값 자동으로 넣어줌
    private boolean delFlag = false;  // 탈퇴 여부 or 계정 활성 여부

    @Column
    private LocalDateTime lastLogin;  // 최근 로그인 시간

    @Column(nullable = false)
    @Builder.Default
    private int point = 0;

    // 휴대폰 번호
    @Column(length = 20)
    private String phone;

    // 휴대폰 등록 여부 (타사 이메일 인증 및 휴면계정 느낌)
    @Column(nullable = false)
    @Builder.Default
    private boolean enable = false;

    // DB에 저장되기 전 role 값이 null이면 기본값 ROLE_USER로 설정해줌
    // @Builder.Default가 누락되는 경우를 대비한 안전장치 역할
    @PrePersist
    public void prePersist()
    {
        if (role == null)
        {
            role = Role.ROLE_USER;
        }
    }
}
