package com.ll.hotel.domain.member.admin.entity;

import com.ll.hotel.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "admin")
public class Admin extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String adminEmail;

    @Column(nullable = false)
    private String password;
}
