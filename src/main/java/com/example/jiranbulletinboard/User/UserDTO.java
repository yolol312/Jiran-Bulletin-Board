package com.example.jiranbulletinboard.User;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String title;
    private String position;
    private String role;
    private LocalDate birthDate;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(this.id)
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
