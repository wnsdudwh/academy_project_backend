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
public class Member {

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

    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.ROLE_USER;

    @Column
    @CreatedDate
    private LocalDateTime regDate;

}
