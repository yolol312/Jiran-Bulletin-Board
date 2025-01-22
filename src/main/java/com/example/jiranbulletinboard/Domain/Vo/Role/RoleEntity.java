package com.example.jiranbulletinboard.Domain.Vo.Role;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Role")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Column(nullable = false)
    private String roleName;

    public RoleDTO toDTO() {
        return RoleDTO.builder()
                .roleId(this.roleId)
                .roleName(this.roleName)
                .build();
    }
}
