package com.example.jiranbulletinboard.Domain.User;

import com.example.jiranbulletinboard.Domain.Vo.Position.PositionEntity;
import com.example.jiranbulletinboard.Domain.Vo.Role.RoleEntity;
import com.example.jiranbulletinboard.Domain.Vo.Title.TitleEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "User")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private PositionEntity position;

    @ManyToOne
    @JoinColumn(name = "title_id", nullable = false)
    private TitleEntity title;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @Column(nullable = false)
    private LocalDate birthDate;

    public UserDTO toDTO() {
        return UserDTO.builder()
                .userId(this.userId)
                .email(this.email)
                .userName(this.userName)
                .password(this.password)
                .titleId(this.title.getTitleId())
                .positionId(this.position.getPositionId())
                .roleId(this.role.getRoleId())
                .birthDate(this.birthDate)
                .build();
    }
}
