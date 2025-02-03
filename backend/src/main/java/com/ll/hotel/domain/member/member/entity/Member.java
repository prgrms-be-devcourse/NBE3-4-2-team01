package com.ll.hotel.domain.member.member.entity;

import com.ll.hotel.domain.member.member.type.MemberStatus;
import com.ll.hotel.global.jpa.entity.BaseTime;
import com.ll.hotel.global.security.oauth2.entity.OAuth;
import jakarta.persistence.*;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member")
public class Member extends BaseTime {

    @Column(unique = true, nullable = false)
    private String memberEmail;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String memberName;

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

    @OneToMany(mappedBy = "member")
    private List<OAuth> oAuthList;

    private String provider;

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public boolean isBusiness() {
        return this.role == Role.BUSINESS;
    }

    public boolean isUser() {
        return this.role == Role.USER;
    }

    public String getUserRole() {
        return this.role.name();
    }
}
