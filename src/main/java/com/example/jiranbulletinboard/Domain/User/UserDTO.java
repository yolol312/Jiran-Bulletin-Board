package com.example.jiranbulletinboard.Domain.User;

import com.example.jiranbulletinboard.Domain.Vo.Position.PositionEntity;
import com.example.jiranbulletinboard.Domain.Vo.Role.RoleEntity;
import com.example.jiranbulletinboard.Domain.Vo.Title.TitleEntity;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserDTO {
    private Integer userId;
    private String userName;
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
                .userName(this.userName)
                .email(this.email)
                .password(this.password)
                .title(titleEntity)
                .position(positionEntity)
                .role(roleEntity)
                .birthDate(this.birthDate)
                .build();
    }
}
