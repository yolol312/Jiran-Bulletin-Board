package com.example.jiranbulletinboard.Domain.Vo.Position;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Position")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PositionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer positionId;

    @Column(nullable = false, unique = true)
    private String positionName;

    @Column(nullable = false, unique = true)
    private String positionCode;

    @Column(nullable = false, unique = true)
    private Integer positionLevel;

    public PositionDTO toDTO() {
        return PositionDTO.builder()
                .positionId(this.positionId)
                .positionName(this.positionName)
                .positionCode(this.positionCode)
                .positionLevel(this.positionLevel)
                .build();
    }
}
