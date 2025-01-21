package com.example.jiranbulletinboard.Domain.Title;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Title")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TitleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer titleId;

    @Column(nullable = false, unique = true)
    private String titleName;

    @Column(nullable = false, unique = true)
    private String titleCode;

    @Column(nullable = false, unique = true)
    private Integer titleLevel;

    public TitleDTO toDTO() {
        return TitleDTO.builder()
                .titleId(this.titleId)
                .titleName(this.titleName)
                .titleCode(this.titleCode)
                .titleLevel(this.titleLevel)
                .build();
    }
}
