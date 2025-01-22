package com.example.jiranbulletinboard.Domain.Role;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RoleDTO {
    private Integer roleId;
    private String roleName;

    public RoleEntity toEntity() {
        return RoleEntity.builder()
                .roleId(this.roleId)
                .roleName(this.roleName)
                .build();
    }
}
