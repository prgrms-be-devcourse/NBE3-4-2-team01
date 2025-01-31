package com.ll.hotel.domain.member.member.entity;

import com.ll.hotel.domain.member.member.type.MemberStatus;
import com.ll.hotel.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member")
public class Member extends BaseTime {

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus memberStatus;

    @OneToOne(mappedBy = "member")
    private Business business;

    public String getUserRole() {
        return this.role.name();
    }
}
