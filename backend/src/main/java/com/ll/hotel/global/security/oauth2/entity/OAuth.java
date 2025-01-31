package com.ll.hotel.global.security.oauth2.entity;

import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "oauth")
public class OAuth extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String oauthId;
}
