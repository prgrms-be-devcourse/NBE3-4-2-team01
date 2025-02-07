package com.ll.hotel.domain.member.member.entity;

import com.ll.hotel.domain.member.member.type.MemberStatus;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.jpa.entity.BaseTime;
import com.ll.hotel.global.security.oauth2.entity.OAuth;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private String memberName;

    @Column(nullable = false)
    private String memberPhoneNumber;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(name = "password", nullable = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus memberStatus;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OAuth> oauths = new ArrayList<>();

    @OneToOne
    private Business business;

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

    public OAuth getFirstOAuth() {
        return this.oauths.isEmpty() ? null : this.oauths.get(0);
    }

    public void checkBusiness() {
        if (!this.isBusiness()) {
            throw new ServiceException("403-1", "사업가만 관리할 수 있습니다.");
        }
    }
}
