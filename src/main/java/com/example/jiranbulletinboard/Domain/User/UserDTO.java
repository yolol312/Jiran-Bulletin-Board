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
    private TitleEntity title;
    private PositionEntity position;
    private RoleEntity role;
    private LocalDate birthDate;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .userId(this.userId)
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .title(this.title)
                .position(this.position)
                .role(this.role)
                .birthDate(this.birthDate)
                .build();
    }
}
