package com.example.jiranbulletinboard.Domain.User;

import com.example.jiranbulletinboard.Domain.Position.PositionEntity;
import com.example.jiranbulletinboard.Domain.Role.RoleEntity;
import com.example.jiranbulletinboard.Domain.Title.TitleEntity;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserDTO {
    private Integer userId;
    private String name;
    private String email;
    private String password;
    private Integer titleId;
    private Integer positionId;
    private Integer roleId;
    private LocalDate birthDate;

    public UserEntity toEntity() {
        TitleEntity titleEntity = TitleEntity.builder().titleId(this.titleId).build();
        PositionEntity positionEntity = PositionEntity.builder().positionId(this.positionId).build();
        RoleEntity roleEntity = RoleEntity.builder().roleId(this.roleId).build();

        return UserEntity.builder()
                .userId(this.userId)
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .title(titleEntity)
                .position(positionEntity)
                .role(roleEntity)
                .birthDate(this.birthDate)
                .build();
    }
}
