package com.ll.hotel.domain.member.member.entity;

import com.ll.hotel.domain.member.member.type.MemberStatus;
import com.ll.hotel.global.security.oauth2.entity.OAuth;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long memberId;

    @Column(unique = true, nullable = false)
    private String memberEmail; // 실제 로그인 ID

    @Column(nullable = false)
    private String password; // 민감한 정보에 접두사를 붙여야 할지 고민

    @Column(nullable = false)
    private String memberName; // 유저 닉네임

    @Column(nullable = false)
    private String memberPhoneNumber;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus memberStatus;

    @OneToMany(mappedBy = "member")
    private List<OAuth> oAuthList;

    public String getUserRole() {
        return this.role.name();
    }

}
