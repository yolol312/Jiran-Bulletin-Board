package com.example.jiranbulletinboard.User;

import com.example.jiranbulletinboard.InterestTag.InterestTagEntity;
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
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private LocalDate birthDate;

    public UserDTO toDTO() {
        return UserDTO.builder()
                .id(this.id)
                .email(this.email)
                .password(this.password)
                .title(this.title)
                .position(this.position)
                .birthDate(this.birthDate)
                .build();
    }
}
