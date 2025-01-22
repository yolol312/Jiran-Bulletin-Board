package com.example.jiranbulletinboard.Domain.Vo.Position;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PositionDTO {
    private Integer positionId;
    private String positionName;
    private String positionCode;
    private Integer positionLevel;

    public PositionEntity toEntity() {
        return PositionEntity.builder()
                .positionId(this.positionId)
                .positionName(this.positionName)
                .positionCode(this.positionCode)
                .positionLevel(this.positionLevel)
                .build();
    }
}
